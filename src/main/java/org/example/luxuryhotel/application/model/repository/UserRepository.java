package org.example.luxuryhotel.application.model.repository;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.entities.Role;
import org.example.luxuryhotel.entities.User;
import org.example.luxuryhotel.framework.exaptions.RepositoryException;
import org.example.luxuryhotel.framework.security.GrantedAuthority;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


public class UserRepository {
    private final Connection connection;
    private final static Logger logger = Logger.getLogger(UserRepository.class);
    private final static String findByUsernameOrEmailQueryUE="SELECT * FROM usr u WHERE u.username = ? OR u.email = ?";
    private final static String insertUserAEPU ="INSERT INTO usr (active, email,password, username) " +
            "VALUES (?, ?, ?, ?) RETURNING id";

    private final static String findUserRolesById = "SELECT * FROM user_role u WHERE u.user_id = ?";
    private final static String deleteRole= "DELETE FROM user_role WHERE user_id = ? AND roles = ?";
    private final static String insertRole= "INSERT INTO user_role (user_id, roles) VALUES (?,?)";


    public UserRepository(Connection connection){
        this.connection = connection;
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findByUsernameOrEmail(String username, String email){
        try {
            PreparedStatement statement = connection.prepareStatement(findByUsernameOrEmailQueryUE);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.executeQuery();
            if (statement.execute()){
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    Integer resultId = resultSet.getInt("id");
                    boolean resultActive = resultSet.getBoolean("active");
                    String resultEmail = resultSet.getString("email");
                    String resultPass = resultSet.getString("password");
                    String resultUsername = resultSet.getString("username");
                    User user  = new User(resultId, resultActive, resultEmail, resultPass, resultUsername);
                    user.setRoles(findUserRolesById(user.getId()));
                    return user;
                }
                else return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
        return null;
    }

    public Set<Role> findUserRolesById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement(findUserRolesById);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            Set<Role> roles= new HashSet<>();
            while (rs.next()){
                roles.add(Role.valueOf(rs.getString("roles")));
            }
            return  roles;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }


    public void insertUser(User user) {
        try{
            PreparedStatement statement = connection.prepareStatement(insertUserAEPU);
            statement.setBoolean(1,user.isActive());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getPassword());
            statement.setString(4,user.getUsername());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            user.setId(resultSet.getInt("id"));
            rewriteRoles(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void rewriteRoles(User user) throws  SQLException{
        if (user.getId() == null)
            throw new SQLException("user id cant be null while roles updating");
        if (user.getRoles() == null) {
            Set<Role> roles= new HashSet<>();
            roles.add(Role.USER);
            user.setRoles(roles);
        }
        Set<Role> roles = findUserRolesById(user.getId());
        for (Role role : roles){
            if (!user.getRoles().contains(role)){
                PreparedStatement deleteRoleStm = connection.prepareStatement(deleteRole);
                deleteRoleStm.setInt(1,user.getId());
                deleteRoleStm.setString(2,role.toString());
                deleteRoleStm.execute();
            }
            else user.getRoles().remove(role);
        }
        for (Role role : user.getRoles()){
            PreparedStatement insertRoleStm = connection.prepareStatement(insertRole);
            insertRoleStm.setInt(1,user.getId());
            insertRoleStm.setString(2,role.toString());
            insertRoleStm.execute();
        }
    }
}
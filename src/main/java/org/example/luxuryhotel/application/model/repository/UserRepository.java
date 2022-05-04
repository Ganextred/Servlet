package org.example.luxuryhotel.application.model.repository;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.entities.Role;
import org.example.luxuryhotel.entities.User;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.exaptions.RepositoryException;
import org.example.luxuryhotel.framework.security.GrantedAuthority;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


public class UserRepository extends Repository{
    public UserRepository(Connection connection){
        super(connection);
    }
    private final static Logger logger = Logger.getLogger(UserRepository.class);
    private final static String findUserById = "SELECT * FROM usr u WHERE u.id = ?";
    private final static String findByUsernameOrEmailQueryUE="SELECT * FROM usr u WHERE u.username = ? OR u.email = ?";
    private final static String insertUserAEPU ="INSERT INTO usr (active, email,password, username) " +
            "VALUES (?, ?, ?, ?) RETURNING id";

    private final static String findUserRolesById = "SELECT * FROM user_role u WHERE u.user_id = ?";
    private final static String deleteRole= "DELETE FROM user_role WHERE user_id = ? AND roles = ?";
    private final static String insertRole= "INSERT INTO user_role (user_id, roles) VALUES (?,?)";

    private User extractUser(ResultSet rs) throws SQLException {
        if (rs.next()){
            Integer resultId = rs.getInt("id");
            boolean resultActive = rs.getBoolean("active");
            String resultEmail = rs.getString("email");
            String resultPass = rs.getString("password");
            String resultUsername = rs.getString("username");
            User user  = new User(resultId, resultActive, resultEmail, resultPass, resultUsername);
            user.setRoles(findUserRolesById(user.getId()));
            return user;
        }
        else return  null;
    }

    public User findUserById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement(findUserById);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            return extractUser(rs);
        }catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public User findByUsernameOrEmail(String username, String email){
        try {
            PreparedStatement statement = connection.prepareStatement(findByUsernameOrEmailQueryUE);
            statement.setString(1, username);
            statement.setString(2, email);
            ResultSet resultSet = statement.executeQuery();;
            return extractUser(resultSet);
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
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
            ConnectionPool.getInstance().close(connection);
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
            ConnectionPool.getInstance().close(connection);
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
        Set<Role> inserted = new HashSet<>();
        for (Role role : roles){
            if (!user.getRoles().contains(role)){
                PreparedStatement deleteRoleStm = connection.prepareStatement(deleteRole);
                deleteRoleStm.setInt(1,user.getId());
                deleteRoleStm.setString(2,role.toString());
                deleteRoleStm.execute();
            }
            else inserted.add(role);
        }
        for (Role role : user.getRoles()){
            if (!inserted.contains(role)) {
                PreparedStatement insertRoleStm = connection.prepareStatement(insertRole);
                insertRoleStm.setInt(1, user.getId());
                insertRoleStm.setString(2, role.toString());
                insertRoleStm.execute();
            }
        }
    }
}
package org.example.luxuryhotel.application.model.repository;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.application.entities.User;
import org.example.luxuryhotel.framework.exaptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserRepository {
    private final static Logger logger = Logger.getLogger(UserRepository.class);
    private final static String findByUsernameOrEmailQueryUE="SELECT * FROM usr u WHERE u.username = ? OR u.email = ?";
    private final static String saveUserIAEPU="INSERT INTO usr (active, email,password, username) " +
            "VALUES (?, ?, ?, ?)";

    public User findByUsernameOrEmail(String username, String email, Connection connection){
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
                    return new User(resultId, resultActive, resultEmail, resultPass, resultUsername);
                }
                else return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
        return null;
    }



    public void save(User user, Connection connection) {
        try{
            PreparedStatement statement = connection.prepareStatement(saveUserIAEPU);
            statement.setBoolean(1,user.isActive());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getPassword());
            statement.setString(4,user.getPassword());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }
}
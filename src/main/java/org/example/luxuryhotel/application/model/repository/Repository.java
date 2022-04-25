package org.example.luxuryhotel.application.model.repository;

import java.sql.Connection;
import java.sql.SQLException;

public class Repository {
    protected final Connection connection;
    public Repository(Connection connection){
        this.connection = connection;
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection(){
        return connection;
    };
}

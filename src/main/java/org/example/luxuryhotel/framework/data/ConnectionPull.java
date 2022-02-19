package org.example.luxuryhotel.framework.data;

import org.apache.log4j.Logger;
import org.example.luxuryhotel.framework.web.HandlerMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class ConnectionPull {
    private static final int INIT_CAPACITY = 5;
    private static final int MAX_CAPACITY = 30;
    private static  String url;
    private static String user;
    private static String password;
    private final static Logger logger = Logger.getLogger(HandlerMapping.class);
    private static final ConnectionPull instance = new ConnectionPull();
    private static final Stack<Connection> connections = new Stack<>();
    private static int given = 0;

    private ConnectionPull() {}

    public static ConnectionPull getInstance(){
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connections.size()> 0) {
            Connection connection = connections.peek();
            given++;
            return connection;
        }else if (given<MAX_CAPACITY){
            Connection connection = connections.peek();
            given++;
            return connections.push(connections.push( DriverManager.getConnection(url, user, password)));
        }else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getConnection();
        }
    }
    public synchronized void close(Connection connection){
        if (connections.size()>INIT_CAPACITY) {
            try {
                connection.close();
                given--;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else connections.push(connection);
        notify();
    }
    static {

        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);
            url = property.getProperty("datasource.url");
            user = property.getProperty("datasource.username");
            password = property.getProperty("datasource.password");
            for (int i = 0; i<INIT_CAPACITY; i++){
                try {
                    connections.push( DriverManager.getConnection(url, user, password));
                } catch (SQLException e) {
                    logger.warn("Cant create connection during initialization");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }

    }


}

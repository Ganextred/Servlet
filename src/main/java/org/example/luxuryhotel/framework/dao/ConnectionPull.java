package org.example.luxuryhotel.framework.dao;

import org.apache.log4j.Logger;
import org.example.luxuryhotel.application.LuxuryHotelApplication;
import org.example.luxuryhotel.framework.Util.Pair;
import org.example.luxuryhotel.framework.annotation.Controller;
import org.example.luxuryhotel.framework.web.HandlerMapping;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class ConnectionPull {
    public static final int INIT_CAPACITY = 5;
    public static final int MAX_CAPACITY = 10;

    private final static Logger logger = Logger.getLogger(HandlerMapping.class);
    public static Stack<Connection> connections= new Stack<>();
    public static int givenConnections = 0;


    public static synchronized Connection getConnection() throws SQLException {
        if (!connections.isEmpty()) {
            givenConnections++;
            return connections.peek();
        }else if (connections.size()+givenConnections<MAX_CAPACITY){
            givenConnections++;
            return connections.pu
        }
    }

    static  {
        for (int i = 0; i<INIT_CAPACITY; i++){
            try {
                connections.push( DriverManager.getConnection("jdbc:postgresql://localhost:5432/example", "postgres", "postgres"));
            } catch (SQLException e) {
                logger.warn("Cant create connection during initialization");
                e.printStackTrace();
            }
        }
    }

}

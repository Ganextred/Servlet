package org.example.luxuryhotel.framework.data;




import org.example.luxuryhotel.framework.AppContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;

import static java.lang.Thread.State.WAITING;
import static java.lang.Thread.sleep;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class ConnectionPoolTest {
    @BeforeAll
    static void init() throws IOException {
        try (InputStream fis = new FileInputStream("src/main/webapp/framework.properties")) {
            Properties property = new Properties();
            property.load(fis);
            AppContext.property = property;
        }
    }



   @Test
   public void moreInitCapacity(){
       ConnectionPool connectionPool = ConnectionPool.getInstance();

       //get all init connections from CP
       ArrayList<Connection> connections = new ArrayList<>(5);
       for (int i=0; i<5; i++)
           connections.add(connectionPool.getConnection());
       assertEquals(0, connectionPool.getSize());
       //get 1 more connection from another thread.
       Connection extraConnection  = connectionPool.getConnection();
       Assertions.assertNotNull(extraConnection);
       //returns connection
       connectionPool.close(extraConnection);
       for (int i=0; i<5; i++)
           connectionPool.close(connections.get(i));

       assertEquals(5, connectionPool.getSize());
   }

    @Test
    public void waitMultiTread() throws InterruptedException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        //get all  connections from CP
        ArrayList<Connection> connections = new ArrayList<>(5);
        for (int i = 0; i < ConnectionPool.MAX_CAPACITY; i++) {
            connections.add(connectionPool.getConnection());
        }
        Thread thread = new Thread(() -> {
            Connection connection = ConnectionPool.getInstance().getConnection();
            ConnectionPool.getInstance().close(connection);
        });

        thread.start();
        sleep(1000);
        assertEquals(WAITING, thread.getState(),
                """
                        Tread isn't waiting, it have got connection without permission or
                        the stream didn't start waiting for a long time""");
        connectionPool.close(connections.get(ConnectionPool.MAX_CAPACITY - 1));
        thread.join(2000);
        boolean fail = false;
        if (thread.isAlive()) {
            thread.interrupt();
            fail = true;
        }
        for (int i = 0; i < ConnectionPool.MAX_CAPACITY - 1; i++)
            connectionPool.close(connections.get(i));
        assertFalse(fail, "Deadlock or problem with DB");
    }
}
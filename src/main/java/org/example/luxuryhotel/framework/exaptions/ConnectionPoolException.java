package org.example.luxuryhotel.framework.exaptions;

import java.sql.SQLException;

public class ConnectionPoolException extends RuntimeException{
    public ConnectionPoolException(){
        super();
    }
    public ConnectionPoolException(String s){
        super(s);
    }

    public ConnectionPoolException(Throwable e) {

    }

    public ConnectionPoolException(String s, SQLException e) {
    }
}

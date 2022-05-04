package org.example.luxuryhotel.application.model.service;



import org.example.luxuryhotel.application.model.repository.RequestRepository;
import org.example.luxuryhotel.entities.Clazz;
import org.example.luxuryhotel.entities.Request;
import org.example.luxuryhotel.entities.User;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.security.SecurityManager;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;


public class UserManager {


    public static List<String> sendRequest(String arrivalDay, String endDay, Clazz clazz, Integer beds, String wishes, User user){
        Validator valid = new Validator();
        List<String> messages = valid.sendRequest(arrivalDay,endDay, clazz, beds, wishes);
        if (messages.size() == 0){
            Connection conn = ConnectionPool.getInstance().getConnection();
            RequestRepository requestRepo = new RequestRepository(conn);
            Request r = new Request(user,beds,LocalDate.parse(arrivalDay), LocalDate.parse(endDay), clazz, wishes);
            requestRepo.insertRequest(r);
            ConnectionPool.getInstance().commit(conn);
            ConnectionPool.getInstance().close(conn);
        }
        return messages;
    }
}

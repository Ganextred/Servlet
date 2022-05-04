package org.example.luxuryhotel.framework;

import org.apache.log4j.Logger;
import org.example.luxuryhotel.application.model.repository.ApartmentStatusRepository;
import org.example.luxuryhotel.application.model.repository.RequestRepository;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.security.AuthorityMapping;
import org.example.luxuryhotel.framework.web.DispatcherServlet;
import org.example.luxuryhotel.framework.web.HandlerMapping;

import javax.servlet.ServletContextEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

public class FrameworkApplication {
    private final static Logger logger = Logger.getLogger(FrameworkApplication.class);
    public  static void run(Class<?> appClass, ServletContextEvent servletContextEvent){
        AppContext.appClass = appClass;
        try (InputStream fis = servletContextEvent.getServletContext().getResourceAsStream("framework.properties")){
            Properties property = new Properties();
            property.load(fis);
            AppContext.property=property;
        } catch (IOException e) {
            logger.error("Properties file not found.");
            e.printStackTrace();
        }
        init();
    }
    private static void init(){
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        HandlerMapping.getInstance();
        AuthorityMapping.getInstance();
        logger.info("Start db initialization");
        Connection conn = connectionPool.getConnection();
        RequestRepository requestRepo = new RequestRepository(conn);
        ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(conn);
        requestRepo.deleteRequestByAnswerStatus_PayTimeLimitBeforeOrEndDayBefore(LocalDateTime.now(), LocalDate.now());
        apartmentStatusRepo.deleteApartmentStatusByPayTimeLimitBeforeOrEndDayBefore(LocalDateTime.now(), LocalDate.now());
        connectionPool.commit(conn);
        connectionPool.close(conn);
    }
}
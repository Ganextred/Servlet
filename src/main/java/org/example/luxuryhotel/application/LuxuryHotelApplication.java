package org.example.luxuryhotel.application;

import org.example.luxuryhotel.framework.FrameworkApplication;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LuxuryHotelApplication implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        FrameworkApplication.run(LuxuryHotelApplication.class, servletContextEvent);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

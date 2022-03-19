package org.example.luxuryhotel.framework.Util;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.framework.exaptions.ConverterException;
import org.example.luxuryhotel.framework.web.DispatcherServlet;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    private final static Logger logger = Logger.getLogger(Converter.class);
    public static Object convert(String p, Class<?> c){
        if (p==null)
            return null;
        if (c.equals(Boolean.class)){
            return Boolean.valueOf(p);
        }
        if (c.equals(Integer.class)){
            return Integer.valueOf(p);
        }
        if (c.equals(Long.class)){
            return Long.valueOf(p);
        }
        if (c.equals(Double.class)){
            return Double.valueOf(p);
        }
        if (c.equals(String.class)){
            return p;
        }
        if (c.equals(LocalDate.class)){
            return LocalDate.parse(p);
        }
        if (c.equals(LocalDateTime.class)){
            return LocalDateTime.parse(p);
        }
        if (c.equals(Duration.class)){
            return Duration.parse(p);
        }
        logger.error("Cant convert String to "+c);
        throw new ConverterException("Cant convert String to "+c);
    }
    public static String[] convert(String[] parameters, Class<?> c){
        return parameters;
    }
}

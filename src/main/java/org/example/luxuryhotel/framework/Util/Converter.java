package org.example.luxuryhotel.framework.Util;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    public static Object convert(String p, Class<?> c){
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
        return null;
    }
    public static Object[] convert(String[] parameters, Class<?> c){
        List<Object> arr= new ArrayList<Object>();
        for (String p : parameters){
            arr.add(convert(p,c.getComponentType()));
        }
        return arr.toArray();
    }
}

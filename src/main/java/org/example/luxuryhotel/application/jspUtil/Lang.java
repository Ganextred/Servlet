package org.example.luxuryhotel.application.jspUtil;

import org.apache.log4j.Logger;
import org.example.luxuryhotel.application.model.service.ApartmentManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.example.luxuryhotel.application.model.service.CookieManager.findCookiesByName;

public class Lang {
    HttpServletRequest request;
    ResourceBundle resourceBundle;
    private final static Logger logger = Logger.getLogger(Lang.class);
    public Lang(HttpServletRequest request) {
        this.request=request;
        Cookie cookie =findCookiesByName("lang", request);
        String lang;
        if (cookie == null) {
            lang = "en";
        }
        else  lang = cookie.getValue();
        Locale locale= new Locale(lang);
        this.resourceBundle= ResourceBundle.getBundle("messages", locale);
    }
    public String gL(String message){
        try{
            return resourceBundle.getString(message);
        }catch (MissingResourceException e){
            logger.warn("MissingResourceException" + "for key"+ message+"   !!!!");
            e.printStackTrace();
        }
        return message;

    }
}
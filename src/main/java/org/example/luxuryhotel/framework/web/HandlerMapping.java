package org.example.luxuryhotel.framework.web;

import org.apache.log4j.Logger;
import org.example.luxuryhotel.application.LuxuryHotelApplication;
import org.example.luxuryhotel.framework.AppContext;
import org.example.luxuryhotel.framework.annotation.Controller;
import org.example.luxuryhotel.framework.annotation.GetMapping;
import org.example.luxuryhotel.framework.Util.Pair;
import org.example.luxuryhotel.framework.annotation.PostMapping;
import org.example.luxuryhotel.framework.exaptions.ControllerNotExist;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Синглтон
public class HandlerMapping {
    private final static Logger logger = Logger.getLogger(HandlerMapping.class);
    public static Map<String, Pair<Method, Object>> getRequests = new HashMap<>();
    public static Map<String, Pair<Method, Object>> postRequests = new HashMap<>();
    public static HandlerMapping map = new HandlerMapping();

    public static HandlerMapping getInstance() {
        return map;
    }

    private HandlerMapping() {
        System.out.println("Начало сбора методов");
        Reflections reflections = new Reflections(LuxuryHotelApplication.class.getPackage().getName());
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        System.out.println(controllers.toString() + "Обнаруженные контроллеры");
        initGet(controllers);
        initPost(controllers);
    }

    private void initGet(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            Reflections reflectCont = new Reflections(controller.getPackage().getName());
            Method[] methods = controller.getMethods();
            try {
                for (Method method : methods) {
                    if (method.isAnnotationPresent(GetMapping.class))
                        getRequests.put(method.getAnnotation(GetMapping.class).path(),
                                new Pair<Method, Object>(method, controller.getDeclaredConstructor().newInstance()));
                }
            } catch (Exception e) {
            }
        }
    }
    private void initPost(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            Reflections reflectCont = new Reflections(controller.getPackage().getName());
            Method[] methods = controller.getMethods();
            try {
                for (Method method : methods) {
                    if (method.isAnnotationPresent(PostMapping.class))
                        postRequests.put(method.getAnnotation(PostMapping.class).path(),
                                new Pair<Method, Object>(method, controller.getDeclaredConstructor().newInstance()));
                }
            } catch (Exception e) {
            }
        }
    }

    public Pair<Method,Object> getGet(String path){
        if (!getRequests.containsKey(path)) {
            logger.error("Controller fo path: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return getRequests.get(path);
    }
    public Pair<Method,Object> getGet(HttpServletRequest request){
        String path=request.getRequestURL().toString();
        if (!getRequests.containsKey(path)) {
            logger.error("Controller fo path: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return getRequests.get(path);
    }
    public Pair<Method,Object> getPost(String path){
        if (!postRequests.containsKey(path)) {
            logger.error("Controller fo path: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return postRequests.get(path);
    }
    public Pair<Method,Object> getPost(HttpServletRequest request){
        String path=request.getRequestURL().toString();
        if (!postRequests.containsKey(path)) {
            logger.error("Controller fo path: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return postRequests.get(path);
    }

}

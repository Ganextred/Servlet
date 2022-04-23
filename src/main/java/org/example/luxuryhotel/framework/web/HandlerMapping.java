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
    private Map<String, Pair<Method, Object>> getRequests = new HashMap<>();
    private Map<String, Pair<Method, Object>> postRequests = new HashMap<>();
    private static HandlerMapping map = new HandlerMapping();

    public static HandlerMapping getInstance() {
        return map;
    }

    private HandlerMapping() {
        Reflections reflections = new Reflections(AppContext.appClass.getPackage().getName());
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
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
                e.printStackTrace();

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
                e.printStackTrace();
            }
        }
    }

    public Map<String, Pair<Method, Object>> getGetRequests() {
        return getRequests;
    }

    public Map<String, Pair<Method, Object>> getPostRequests() {
        return postRequests;
    }

    public Pair<Method,Object> getGet(String path){
        if (!getRequests.containsKey(path)) {
            logger.error("Controller to path get: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return getRequests.get(path);
    }
    public Pair<Method,Object> getGet(HttpServletRequest request){
        String path=request.getRequestURI();
        if (!getRequests.containsKey(path)) {
            logger.error("Controller to path get: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return getRequests.get(path);
    }
    public Pair<Method,Object> getPost(String path){
        if (!postRequests.containsKey(path)) {
            logger.error("Controller to path post: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return postRequests.get(path);
    }
    public Pair<Method,Object> getPost(HttpServletRequest request){
        String path=request.getRequestURI();
        if (!postRequests.containsKey(path)) {
            logger.error("Controller to post path: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return postRequests.get(path);
    }

}

package org.example.luxuryhotel.framework.web;

import org.example.luxuryhotel.framework.AppContext;
import org.example.luxuryhotel.framework.annotation.Controller;
import org.example.luxuryhotel.framework.annotation.GetMapping;
import org.example.luxuryhotel.framework.Util.Pair;
import org.example.luxuryhotel.framework.annotation.PostMapping;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

//Синглтон
public class HandlerMapping {
    public static Map<String, Pair<Method, Object>> getRequests;
    public static Map<String, Pair<Method, Object>> postRequests;
    public static HandlerMapping map = new HandlerMapping();

    public static HandlerMapping getInstance() {
        return map;
    }

    private HandlerMapping() {
        System.out.println("Начало сбора методов");
        Reflections reflections = new Reflections(AppContext.appClass.getPackage().getName());
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        System.out.println(controllers);
        initGet(controllers);
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
        return getRequests.get(path);
    }
    public Pair<Method,Object> getGet(HttpServletRequest request){
        String path=request.getParameter("action");
        return getRequests.get(path);
    }
    public Pair<Method,Object> getPost(String path){
        return getRequests.get(path);
    }
    public Pair<Method,Object> getPost(HttpServletRequest request){
        String path=request.getParameter("action");
        return getRequests.get(path);
    }

}

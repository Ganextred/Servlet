package org.example.luxuryhotel.framework.web;

import org.apache.log4j.Logger;
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

/**
 * Class that define a mapping between requests and handler objects,
 * annotated with @Controller.
 * DispatcherServlet uses this class to delegate request processing
 * to methods defined in controllers and annotated with @GetMapping or @PostMapping
 * @see Controller
 * @see GetMapping
 * @see PostMapping
 * @see DispatcherServlet
 */
public class HandlerMapping {
    private final static Logger logger = Logger.getLogger(HandlerMapping.class);
    /**
     Map of GET http request URL to methods,
     which responsible for their processing.
     */
    private Map<String, Pair<Method, Object>> getRequests = new HashMap<>();
    /**
     Map of POST http request URL to methods,
     which responsible for their processing.
     */
    private Map<String, Pair<Method, Object>> postRequests = new HashMap<>();
    private static HandlerMapping map = new HandlerMapping();

    public static HandlerMapping getInstance() {
        return map;
    }

    /**
     * Constructor for instance initialization,
     * search for class annotated @Controller via reflection,
     * and maps requests to methods.
     */
    private HandlerMapping() {
        Reflections reflections = new Reflections(AppContext.appClass.getPackage().getName());
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        initGet(controllers);
        initPost(controllers);
    }

    /**
     * Initialising a map of http request URL to methods,
     * annotated @GetMapping declared inside controllers class.
     * @param controllers set of class, annotated with @Controller
     */
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

    /**
     * Initialising a map of http request URL to methods,
     * annotated @PostMapping declared inside controllers class.
     * @param controllers set of class, annotated with @Controller
     */
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

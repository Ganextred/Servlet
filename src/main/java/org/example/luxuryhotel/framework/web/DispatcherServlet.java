package org.example.luxuryhotel.framework.web;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.framework.Util.Pair;
import org.example.luxuryhotel.framework.annotation.RequestParam;
import org.example.luxuryhotel.framework.exaptions.NullParamException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static org.example.luxuryhotel.framework.Util.Converter.convert;
import static org.example.luxuryhotel.framework.web.ViewResolver.processView;

/**
 * The class DispatcherServlet process all requests for /main/*
 * It takes the path, search for corresponding controller, inject parameters
 * and invoke right method.
 */
@WebServlet("/main/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 , // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class DispatcherServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(DispatcherServlet.class);
    private final HandlerMapping  handlerMapping = HandlerMapping.getInstance();

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getRequestURI();
        Pair<Method,Object> pair=handlerMapping.getGet(path);
        Model model=new Model(request, response);
        RedirectAttributes rA = new RedirectAttributes();
        String view = doRequest(pair, model, rA);
        processView(view, model, rA);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getRequestURI();
        Pair<Method,Object> pair=handlerMapping.getPost(path);
        Model model=new Model(request, response);
        RedirectAttributes rA = new RedirectAttributes();
        String view = doRequest(pair, model, rA);
        processView(view, model, rA);
    }

    /**
     * @param pair pair of responsible controller method and controller object to invoke this method
     * @param model contains information about request and response, maps into @ RequestParam Model
     * @param rA maps into @RequestParam RedirectAttributes
     * @return name of view which can be used for future processing in ViewResolver
     * @throws ServletException throws servlet exception when can't invoke responsible method
     * @see RequestParam
     */
    private String doRequest(Pair<Method, Object> pair, Model model, RedirectAttributes rA) throws ServletException {
        Method method = pair.getFirst();
        Object controller= pair.getSecond();
        Object[] parameters= injectParameters(method, model, rA);
        try {
            return (String) method.invoke(controller, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Cant invoke request to controller: "+ pair.getSecond(), e);
            e.printStackTrace();
            throw new ServletException(e);
        } catch (IllegalArgumentException e){
            logger.error("Problem with method invoke", e);
            throw new ServletException(e);
        }
    }


    /**
     * responsible for mapping parameters in array
     *                    according to @RequestParam annotation for future method invocation
     * @param method method in according which signature parameters will be mapping
     * @param model contains information about request and response, maps into @RequestParam Model
     * @param rA maps into @RequestParam RedirectAttributes
     * @return returns array of parameters for according to method signature
     * @see RequestParam
     */
    private Object[] injectParameters(Method method, Model model, RedirectAttributes rA) {
        List<Object> result= new ArrayList<>();
        Parameter[] parameter = method.getParameters();
        for (Parameter p:parameter){
            if (p.isAnnotationPresent(RequestParam.class)){
                RequestParam rp = p.getAnnotation(RequestParam.class);
                String name = rp.name();
                if (p.getType().isArray()){
                    String[] data = model.request.getParameterValues(name);
                    data = checkDefaultAndRequiredForArr(rp, data);
                    result.add(convert(data,p.getType()));
                }else {
                    String data = model.request.getParameter(name);
                    data = checkDefaultAndRequired(rp, data);
                    result.add(convert(data,p.getType()));
                }
            }
            if (p.getType().equals(Model.class))
                result.add(model);
            else if (p.getType().equals(RedirectAttributes.class))
                result.add(rA);
            else if (p.getType().equals(HttpServletRequest.class))
                result.add(model.request);
            else if (p.getType().equals(HttpServletResponse.class))
                result.add(model.response);
        }
        return result.toArray();
    }

    /**
     * @param rp - RequestParam, object witch contains p
     * @param data - String representation of value of parameter
     * @return checks required and defaultValue constraints returns data or defaultValue or throws NullParamException
     * @see RequestParam
     */
    private String checkDefaultAndRequired(RequestParam rp, String data) {
        if (rp.required() && rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") && data == null)
            throw new NullParamException("param "+rp.name()+" not found");
        if (!rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") && data == null)
            data = rp.defaultValue();
        return data;
    }
    /**
     * @param rp - RequestParam, object witch contains p
     * @param data - String array representation of value of parameter
     * @return checks required and defaultValue constraints returns data or defaultValue or throws NullParamException
     * @see RequestParam
     */
    private String[] checkDefaultAndRequiredForArr(RequestParam rp, String[] data) {
        if (data == null) {
            if (rp.required() && rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n"))
                throw new NullParamException("param "+rp.name()+" not found");
            if (!rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n"))
                data = new String[]{rp.defaultValue()};
            else data = new String[]{};
        }
        return data;
    }

    public void destroy() {
    }
}


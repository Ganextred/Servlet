package org.example.luxuryhotel.framework.web;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.framework.Util.Pair;
import org.example.luxuryhotel.framework.annotation.RequestParam;
import org.example.luxuryhotel.framework.exaptions.NullParamException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.luxuryhotel.framework.Util.Converter.convert;

/**
 * Simple class that extends {@link HttpServlet}.
 *
 * @author Eugene Suleimanov
 */
public class DispatcherServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(DispatcherServlet.class);


    private HandlerMapping handlerMapping= HandlerMapping.getInstance();

    public void init() throws ServletException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        Pair<Method,Object> pair=handlerMapping.getGet(request);
        RedirectAttributes rA=new RedirectAttributes(request, response);
        Model model=new Model(request, response);
        String view = doRequest(pair, rA, model);


    }

    private String doRequest(Pair<Method, Object> pair, RedirectAttributes rA, Model model) {
        Method method = pair.getFirst();
        Object controller= pair.getSecond();
        Object[] parameters= injectParameters(method, model, rA);
        try {
            return (String) method.invoke(controller,parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Cant invoke request to controller: "+ pair.getSecond());
            e.printStackTrace();
        }
    }

    private Object[] injectParameters(Method method, Model model, RedirectAttributes rA) {
        List<Object> result= new ArrayList<Object>();
        Parameter[] parameter = method.getParameters();
        for (Parameter p:parameter){
            if (p.isAnnotationPresent(RequestParam.class)){
                RequestParam rp = p.getAnnotation(RequestParam.class);
                String name = rp.name();
                String data = model.request.getParameter(name);
                if (rp.required() && rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") && data == null)
                    throw new NullParamException();
                if (!rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") && data == null)
                    data = rp.defaultValue();
                result.add(convert(data,p.getClass()));
            }
            if (p.getType().equals(Model.class))
                result.add(model);
            if (p.getType().equals(RedirectAttributes.class))
                result.add(rA);
            if (p.getType().equals(HttpServletRequest.class))
                result.add(model.response);
            if (p.getType().equals(HttpServletResponse.class))
                result.add(model.request);
        }
        return result.toArray();
    }

    public void destroy() {}


}

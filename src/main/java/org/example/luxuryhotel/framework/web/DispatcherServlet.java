package org.example.luxuryhotel.framework.web;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.framework.Util.Pair;
import org.example.luxuryhotel.framework.annotation.RequestParam;
import org.example.luxuryhotel.framework.exaptions.NullParamException;

import javax.servlet.ServletException;
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

@WebServlet("/test")
public class DispatcherServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(DispatcherServlet.class);


    private HandlerMapping handlerMapping= HandlerMapping.getInstance();

    public void init() throws ServletException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String path=request.getRequestURI();
        Pair<Method,Object> pair=handlerMapping.getGet(path);
        Model model=new Model(request, response);
        String view = doRequest(pair, model);
        try {
            processView(view, model);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
//        Pair<Method,Object> pair=handlerMapping.getPost(request);
//        Model model=new Model(request, response);
//        String view = doRequest(pair, model);
    }

    private String doRequest(Pair<Method, Object> pair, Model model) {
        Method method = pair.getFirst();
        Object controller= pair.getSecond();
        Object[] parameters= injectParameters(method, model);
        try {
            return (String) method.invoke(controller,(Object[])parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Cant invoke request to controller: "+ pair.getSecond());
            e.printStackTrace();
            return "error";
        }
    }

    private Object[] injectParameters(Method method, Model model) {
        List<Object> result= new ArrayList<Object>();
        Parameter[] parameter = method.getParameters();
        for (Parameter p:parameter){
            if (p.isAnnotationPresent(RequestParam.class)){
                RequestParam rp = p.getAnnotation(RequestParam.class);
                String name = rp.name();
                if (p.getType().isArray()){
                    String[] data = model.request.getParameterValues(name);
                    for (int i = 0; i < data.length; i++){
                        data[i] = checkDefaultAndRequired(rp,data[i]);
                    }
                    result.add(convert(data,p.getType()));
                }else {
                    String data = model.request.getParameter(name);
                    data = checkDefaultAndRequired(rp, data);
                    result.add(convert(data,p.getType()));
                }
            }
            if (p.getType().equals(Model.class))
                result.add(model);
            if (p.getType().equals(HttpServletRequest.class))
                result.add(model.response);
            if (p.getType().equals(HttpServletResponse.class))
                result.add(model.request);
        }
        return result.toArray();
    }

    private String checkDefaultAndRequired(RequestParam rp, String data) {
        if (rp.required() && rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") && data == null)
            throw new NullParamException();
        if (!rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") && data == null)
            data = rp.defaultValue();
        return data;
    }

    public void destroy() {}


}

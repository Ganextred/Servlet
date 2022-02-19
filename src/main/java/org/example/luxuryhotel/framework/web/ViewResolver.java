package org.example.luxuryhotel.framework.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewResolver {
    static String processView(String view,Model model){
        if (view.startsWith("redirect:")){
            String link = view.substring(9);
            RequestDispatcher dispatcher = model.request.getRequestDispatcher(link);
            dispatcher.forward();
        }
    }
}

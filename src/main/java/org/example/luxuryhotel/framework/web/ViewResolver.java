package org.example.luxuryhotel.framework.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

public class ViewResolver {
    static void processView(String view,Model model) throws ServletException, IOException {
        model.merge();
        if (view.startsWith("redirect:")){
            String link = view.substring(9);
            model.response.sendRedirect(link);
        }else {
            view="WEB-INF/"+view;
            RequestDispatcher dispatcher = model.request.getRequestDispatcher(view);
            dispatcher.forward(model.request, model.response);
        }
    }

}

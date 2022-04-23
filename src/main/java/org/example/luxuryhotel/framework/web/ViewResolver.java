package org.example.luxuryhotel.framework.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewResolver {
    static void processView(String view,Model model, RedirectAttributes rA) throws ServletException, IOException {
        if (!view.equals("HttpStatus.ok"))
            if (view.startsWith("redirect:")){
                String link = model.request.getContextPath()+view.substring(9);
                String attributes = rA.getLinkParameters();
                model.response.sendRedirect(link+attributes);
            }else {
            model.merge();
            view="/WEB-INF/"+view;
            RequestDispatcher dispatcher = model.request.getRequestDispatcher(view);
            dispatcher.forward(model.request, model.response);
        }
    }
}



package org.example.luxuryhotel.framework.web;


import org.apache.log4j.Logger;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



import static org.example.luxuryhotel.framework.web.ViewResolver.processView;

@WebServlet("")
public class HomeServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(DispatcherServlet.class);

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processView("redirect:/main/apartments", new Model(request,response), new RedirectAttributes());
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processView("redirect:/main/apartments", new Model(request,response), new RedirectAttributes());
    }


}


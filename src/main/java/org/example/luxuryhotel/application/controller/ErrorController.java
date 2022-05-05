package org.example.luxuryhotel.application.controller;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.framework.annotation.Controller;
import org.example.luxuryhotel.framework.annotation.GetMapping;
import org.example.luxuryhotel.framework.annotation.PostMapping;
import org.example.luxuryhotel.framework.exaptions.ControllerNotExist;
import org.example.luxuryhotel.framework.web.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


@Controller
public class ErrorController {
    private final static Logger logger = Logger.getLogger(ErrorController.class);

    @GetMapping(path = "/main/error")
    @PostMapping(path = "/main/error")
    public String handleError(HttpServletRequest request, Model model) {
        Throwable throwable = (Throwable)
                request.getAttribute("javax.servlet.error.exception");
        Integer status = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (throwable!=null) {
            if (throwable.getClass().equals(ControllerNotExist.class))
                status = 404;
            else logger.error("New error", throwable);
        }
        if (status != null) {
            if(status.equals(404)) {
                model.addAttribute("message", "Page_not_found");
                model.addAttribute("error", "404");
                return "/jsp/error.jsp";
            }
        }
        model.addAttribute("message", "Something_went_wrong");
        model.addAttribute("error", "Error");
        return "/jsp/error.jsp";
    }


    @PostMapping(path = "/main/error403")
    @GetMapping(path = "/main/error403")
    public String handleError403() {
        return "/jsp/error403.jsp";
    }
}


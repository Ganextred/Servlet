package org.example.luxuryhotel.application.controller;

import org.example.luxuryhotel.application.model.service.CookieManager;
import org.example.luxuryhotel.framework.annotation.Controller;
import org.example.luxuryhotel.framework.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LangController {

    @GetMapping(path = "/main/lang")
     public String lang(HttpServletRequest request, HttpServletResponse response){
        CookieManager.changLang(request, response);
        return "HttpStatus.ok";
    }
}

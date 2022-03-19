package org.example.luxuryhotel.application;


import org.example.luxuryhotel.framework.annotation.Controller;
import org.example.luxuryhotel.framework.annotation.GetMapping;
import org.example.luxuryhotel.framework.annotation.RequestParam;
import org.example.luxuryhotel.framework.web.Model;

@Controller
public class TestController {
    @GetMapping(path = "/main/test")
    public String main(@RequestParam(name = "name") String name, Model model){
        model.addAttribute("name", name);
        return "jsp/test.jsp";
    };
}

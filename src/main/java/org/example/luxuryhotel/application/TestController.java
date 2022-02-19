package org.example.luxuryhotel.application;

import org.example.luxuryhotel.framework.annotation.Controller;
import org.example.luxuryhotel.framework.annotation.GetMapping;
import org.example.luxuryhotel.framework.annotation.RequestParam;

@Controller
public class TestController {
    @GetMapping(path = "/test")
    public String m1(@RequestParam(name = "name") String[] name){
        for (String n : name )
            System.out.println(n);
        return "test.jsp";
    };
}

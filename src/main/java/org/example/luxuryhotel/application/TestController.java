package org.example.luxuryhotel.application;

import org.example.luxuryhotel.framework.annotation.Controller;
import org.example.luxuryhotel.framework.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping
    public String m1(){
        return "";
    };
}

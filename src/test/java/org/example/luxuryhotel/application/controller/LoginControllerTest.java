package org.example.luxuryhotel.application.controller;


import org.example.luxuryhotel.framework.web.Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {


    @Test
    void loginPage() {
        LoginController loginController = new LoginController();
        Model model = mock(Model.class);
        String[] s ={"aaa","asddas"};
        assertEquals(loginController.loginPage(s,model),"/jsp/login.jsp");
        verify(model).addAttribute("message", s);
    }

}
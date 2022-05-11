package org.example.luxuryhotel.application.controller;




import org.example.luxuryhotel.framework.exaptions.ControllerNotExist;
import org.example.luxuryhotel.framework.web.Model;
import org.junit.Assert;
import org.junit.jupiter.api.Test;


import javax.servlet.http.HttpServletRequest;


import static org.mockito.Mockito.*;


class MyErrorControllerTest {



    @Test
    void handleError404() {
        ErrorController ErrorController = new ErrorController();

        HttpServletRequest request = mock(HttpServletRequest.class);
        Model model = mock(Model.class);
        when(request.getAttribute("javax.servlet.error.exception")).thenReturn(new ControllerNotExist());
        try {
            Assert.assertEquals("/jsp/error.jsp", ErrorController.handleError(request, model));
        } catch (Exception e){
            Assert.fail("Error controller throw error");
        }
         verify(model).addAttribute("error", "404");
    }

    @Test
    void handleError403() {
        ErrorController myErrorController = new ErrorController();
        try {
            Assert.assertEquals(myErrorController.handleError403(),"/jsp/error403.jsp");
        } catch (Exception e){
            Assert.fail("Error controller throw error");
        }

    }
}
package org.example.luxuryhotel.application.jspUtil;

import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.ResourceBundle;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LangTest {

    @Test
    public void gL() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Lang l = new Lang(request);

        l.resourceBundle = new ResourceBundle() {
            @Override
            protected Object handleGetObject(String key) {
                if (key.equals("b1"))
                    return "bundle1";
                else return null;
            }
            @Override
            public Enumeration<String> getKeys() {
                return null;
            }
        };

        assertEquals("bundle1",l.gL("b1"));
        assertEquals("b2", "b2");
    }
}
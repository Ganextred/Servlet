package org.example.luxuryhotel.application.model.service;

import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


import java.util.LinkedList;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CookieManagerTest {

    @Test
    public void findCookiesByName() {
        HttpServletRequest request1 = mock(HttpServletRequest.class);
        when(request1.getCookies()).thenReturn(null);
        Cookie cookie1 = CookieManager.findCookiesByName("lang",request1);
        Assert.assertNull(cookie1);

        HttpServletRequest request2 = mock(HttpServletRequest.class);
        when(request2.getCookies()).thenReturn(new Cookie[]{});
        Cookie cookie2 = CookieManager.findCookiesByName("lang",request2);
        Assert.assertNull(cookie2);

        HttpServletRequest request3 = mock(HttpServletRequest.class);
        Cookie mockCookie = mock(Cookie.class);
        when(request3.getCookies()).thenReturn(new Cookie[]{mockCookie});
        when(mockCookie.getName()).thenReturn("lang");
        Cookie cookie3 = CookieManager.findCookiesByName("lang",request3);
        Assert.assertEquals(cookie3,mockCookie);
    }
}
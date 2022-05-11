package org.example.luxuryhotel.framework.web;

import junit.framework.TestCase;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.example.luxuryhotel.framework.web.ViewResolver.processView;
import static org.mockito.Mockito.*;

public class ViewResolverTest{

    @Test
    public void testProcessViewHttpOk() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Model model = new Model(request, response);
        RedirectAttributes ra = new RedirectAttributes();
        processView("HttpStatus.ok",model, ra);
        verify(request, never()).getContextPath();
        verify(response, never()).sendRedirect(any());
        verify(request, never()).getRequestDispatcher(any());
    }
    @Test
    public void testProcessViewRedirect() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RedirectAttributes ra = mock(RedirectAttributes.class);
        Model model = new Model(request, response);

        when(request.getContextPath()).thenReturn("");
        when(ra.getLinkParameters()).thenReturn("?messages=hello");

        processView("redirect:/main/apartments",model, ra);
        verify(request).getContextPath();
        verify(response).sendRedirect("/main/apartments?messages=hello");
        verify(request, never()).getRequestDispatcher(any());
    }

    @Test
    public void testProcessViewForward() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RedirectAttributes ra = mock(RedirectAttributes.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        Model model = new Model(request, response);

        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        processView("/jsp/apartments.jsp",model, ra);
        verify(request, never()).getContextPath();
        verify(response, never()).sendRedirect(any());
        verify(request).getRequestDispatcher("/WEB-INF//jsp/apartments.jsp");
    }
}
package org.example.luxuryhotel.framework.security;

import junit.framework.TestCase;
import org.example.luxuryhotel.entities.Role;
import org.example.luxuryhotel.entities.User;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import java.util.HashSet;
import java.util.Set;

import static javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE;
import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;
import static javax.swing.text.html.CSS.getAttribute;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityTagTest {

    @Test
    public void testDoStartTag() throws JspException {
        HttpSession session = mock(HttpSession.class);
        PageContext pc = mock(PageContext.class);
        when(pc.getSession()).thenReturn(session);

        SecurityTag st1 = new SecurityTag();
        st1.setPageContext(pc);

        st1.setAuthority("Authorized");
        when(session.getAttribute("user")).thenReturn(new User());
        Assert.assertEquals(st1.doStartTag(),EVAL_BODY_INCLUDE);

        st1.setAuthority("Authorized");
        when(session.getAttribute("user")).thenReturn(null);
        Assert.assertEquals(st1.doStartTag(),SKIP_BODY);

        st1.setAuthority("Anonymous");
        when(session.getAttribute("user")).thenReturn(new User());
        Assert.assertEquals(st1.doStartTag(),SKIP_BODY);

        User user = new User();
        user.setRoles(Set.of(Role.USER));
        st1.setAuthority("USER");
        when(session.getAttribute("user")).thenReturn(user);
        Assert.assertEquals(st1.doStartTag(),EVAL_BODY_INCLUDE);

        st1.setAuthority("ADMIN");
        when(session.getAttribute("user")).thenReturn(user);
        Assert.assertEquals(st1.doStartTag(),SKIP_BODY);


    }
}
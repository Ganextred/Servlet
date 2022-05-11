package org.example.luxuryhotel.framework.web;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class RedirectAttributesTest {

    @Test
    public void testGetLinkParameters() {
        RedirectAttributes ra = new RedirectAttributes();
        Assert.assertEquals("?", ra.getLinkParameters());
        try {
            ra.addAttribute("test", null);
            Assert.fail("NullPointerException");
        }catch (NullPointerException e){};

        ra.addAttribute("test1","1");
        List<String> attributes = new LinkedList<>();
        attributes.add("2");
        attributes.add("3");
        ra.addAttributes("test2", attributes);

        String lpString = ra.getLinkParameters();
        String[] lp = lpString.substring(1, lpString.length() - 1).split("&");
        Arrays.sort(lp);

        Assert.assertArrayEquals(new String[]{"test1=1", "test2=2", "test2=3"},lp);
    }
}
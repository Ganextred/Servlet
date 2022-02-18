package org.example.luxuryhotel.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectAttributes extends Model {
    public Map<String, Object> flashAttributes;

    public RedirectAttributes(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public RedirectAttributes addFlashAttribute(String attributeName, Object attributeValue){
        return this;
    }


}

package org.example.luxuryhotel.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class Model {
    public Map <String, Object> attributes;
    public HttpServletRequest request;
    public HttpServletResponse response;

    public Model(HttpServletRequest request, HttpServletResponse response) {
        this.attributes = new HashMap<String, Object>();
        this.request = request;
        this.response = response;
    }

}

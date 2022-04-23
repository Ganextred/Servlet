package org.example.luxuryhotel.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RedirectAttributes {
    public Map<String, List<String>> attributes;


    public RedirectAttributes() {
        this.attributes = new HashMap<>();
    }

    public List<String> getAttribute(String name) {
        if (attributes.containsKey(name))
            return attributes.get(name);
        return null;
    }

    public void addAttributes(String name,  List<String> o) {
        attributes.put(name, o);
    }

    public void addAttribute(String name,  String o) {
        attributes.put(name, new LinkedList(List.of(o)));
    }

    public String getLinkParameters(){
        StringBuilder linkP = new StringBuilder("?");
        for(Map.Entry<String, List<String>> entry: attributes.entrySet()){
            for (String value: entry.getValue()) {
                linkP.append(entry.getKey())
                        .append("=")
                        .append(value)
                        .append("&");
            }
        }
        return linkP.toString();
    }
}
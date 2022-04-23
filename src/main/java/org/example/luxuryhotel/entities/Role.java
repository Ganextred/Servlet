package org.example.luxuryhotel.entities;


import org.example.luxuryhotel.framework.security.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN;


    @Override
    public String getAuthority() {
        return name();
    }
}

package org.example.luxuryhotel.framework.security;

import java.util.Collection;

public interface UserDetails {
    public Collection<? extends GrantedAuthority> getAuthorities();
}

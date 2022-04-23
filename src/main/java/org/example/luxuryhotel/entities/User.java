package org.example.luxuryhotel.entities;



import org.example.luxuryhotel.framework.security.GrantedAuthority;
import org.example.luxuryhotel.framework.security.UserDetails;

import java.util.Collection;
import java.util.Set;


public class User implements UserDetails {

    private Integer id;
    private String username;

    private String email;
    private boolean active = true;
    String password;


    private Set<Role> roles;


    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(Integer id, boolean active, String email,String password, String username) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = active;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {this.id = id; return this;}

    public String getUsername() {
        return username;
    }


    public User setUsername(String name) {this.username = name; return this;}

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {this.email = email; return this;}

    public boolean isActive() {
        return active;
    }

    public User setActive(boolean active) {this.active = active; return  this;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Set<Role> roles) {this.roles = roles; return this;}

    public Collection<? extends GrantedAuthority> getAuthorities(){
        return getRoles();
    };

}
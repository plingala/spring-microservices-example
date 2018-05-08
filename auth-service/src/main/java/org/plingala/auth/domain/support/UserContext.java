package org.plingala.auth.domain.support;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class UserContext {
    private final String username;
    private final String name;
    private final String token;
    private final List<GrantedAuthority> authorities;

    private UserContext(String username, String name, List<GrantedAuthority> authorities, String token) {
        this.username = username;
        this.authorities = authorities;
        this.name = name;
        this.token = token;
    }
    
    public static UserContext create(String username, String name, List<GrantedAuthority> authorities) {
        if (username == null || username.trim().length() == 0) throw new IllegalArgumentException("Username is blank: " + username);
        return new UserContext(username, name, authorities, null);
    }
    
    public static UserContext create(String username, String name, List<GrantedAuthority> authorities, String token) {
    	if (username == null || username.trim().length() == 0) throw new IllegalArgumentException("Username is blank: " + username);
        return new UserContext(username, name, authorities, token);
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

	@Override
	public String toString() {
		return "UserContext [username=" + username + ", name=" + name + ", authorities=" + authorities + "]";
	}
    
    
}
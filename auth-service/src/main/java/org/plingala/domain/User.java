package org.plingala.domain;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

	private String username;
	private String password;
	private String name;
	private List<String> roles;
	
	public User() {}
	
	public User(String username, String password, String name, List<String> roles) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.roles = roles;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
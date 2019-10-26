package com.mare.jovan.user;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean admin = false;
	private String username;
	private String password;
	private UserPermission permission;
	
	public User(String username, String password, UserPermission permission) {
		super();
		this.username = username;
		this.password = password;
		this.permission = permission;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public UserPermission getPermission() {
		return permission;
	}
	
	

}

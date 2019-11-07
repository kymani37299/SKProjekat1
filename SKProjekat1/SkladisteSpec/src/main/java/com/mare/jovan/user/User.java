package com.mare.jovan.user;

import java.io.Serializable;

/**
 * User class is used to communicate with storage. 
 * There is only one admin user who can request any operation regarding file manipulation.
 * Users other than admin have their permissions defined and can request operations accordingly.
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean admin = false;
	private String username;
	private String password;
	private UserPermission permission;
	
	/**
	 * Creates new User instance with provided parameters
	 * @param username the user name string
	 * @param password the password string
	 * @param permission the UserPermission instance
	 */
	public User(String username, String password, UserPermission permission) {
		super();
		this.username = username;
		this.password = password;
		this.permission = permission;
	}
	
	/**
	 * Returns true if user is defined as admin
	 * @return true if user is defined as admin;otherwise false
	 */
	public boolean isAdmin() {
		return admin;
	}
	/**
	 * Sets user admin to provided value
	 * @param admin
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	/**
	 * Returns the user name of user
	 * @return the user name of user
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * Returns the password of user
	 * @return the password of user
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Returns the UserPermission instance that defines users's permissions
	 * @return the UserPermission instance
	 */
	public UserPermission getPermission() {
		return permission;
	}
	
	

}

package com.mare.jovan.user;

import java.io.Serializable;

/**
 * UserPermission class defines which operation can User instance request.
 */
public class UserPermission implements Serializable {
	/**
	 * Defines if User can request downloading file
	 */
	public boolean download = false;
	/**
	 * Defines if User can request creating file
	 */
	public boolean create = false;
	/**
	 * Defines if User can request deleting file
	 */
	public boolean delete = false;
}

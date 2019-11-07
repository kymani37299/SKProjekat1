package com.mare.jovan;

import com.mare.jovan.user.User;

/**
 * Responsible for manipulation of users.
 *
 */

public interface IConnection {
	/**
	 * Sets current user value to user with matching user name and password
	 * @param username the user name string
	 * @param password the password string
	 * @return true if there exists user with matching user name and password; false otherwise
	 */
	boolean login(String username, String password);
	/**
	 * Returns LocalStorage instance
	 * @return null if currentUser value is null;new LocalStorage instance otherwise
	 */
	IStorage getStorage();
	/**
	 * Checks if there are any existing users
	 * @return true if there are any exiting users;false otherwise
	 */
	boolean noUsers();
	/**
	 * Adds new user to users list.
	 * @param user a new instance of user
	 * @return enum value that determines whether the operation was successful or not
	 */
	EProcessResult addUser(User user);
	/**
	 * Removes user from users list if user with given user name exists
	 * @param username the user name string
	 * @return enum value that determines whether the operation was successful or not
	 */
	EProcessResult banUser(String username);
	/**
	 * Checks if any user has ongoing session with local storage.
	 * @return true if any user is logged in; false otherwise
	 */
	boolean isLoggedIn();
	/**
	 * Logs current user out of the ongoing session with local storage.
	 */
	void logout();
}

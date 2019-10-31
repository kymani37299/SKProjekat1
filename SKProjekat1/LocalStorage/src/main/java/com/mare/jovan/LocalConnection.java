package com.mare.jovan;

import java.util.ArrayList;

import com.mare.jovan.user.User;
import com.mare.jovan.util.FileUtil;

public class LocalConnection implements IConnection{

	
	private final static String USER_DATA_PATH = "users.bin";
	
	private ArrayList<User> usersList;
	private User currentUser;
	private boolean usersListInitialized;
	
	public LocalConnection() {
		usersListInitialized = false;
		usersList = getUsersList();
	}
	
	private void updateUsersList() {
		FileUtil.serialize(usersList, USER_DATA_PATH);
	}
	
	private ArrayList<User> getUsersList() {

		if(!usersListInitialized) {
			@SuppressWarnings("unchecked")
			ArrayList<User> usersList = (ArrayList<User>) FileUtil.deserialize(USER_DATA_PATH);
			return usersList;
		} else {
			ArrayList<User> usersList = new ArrayList<User>();
			FileUtil.serialize(usersList, USER_DATA_PATH);
			usersListInitialized = true;

			return usersList;
		}
	}
	
	public boolean login(String username, String password) {
		for(User user: usersList) {
			if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
				currentUser = user;
				return true;
			}
		}
		return false;
	}

	public IStorage getStorage() {
		if(currentUser==null)
			return null;
		return new LocalStorage(currentUser);
	}

	public boolean noUsers() {
		return usersList.isEmpty();
	}

	public boolean addUser(User user) {
		if(currentUser==null || !currentUser.isAdmin()) return false;
		usersList.add(user);
		updateUsersList();
		return true;
	}

	public boolean banUser(String username) {
		if(currentUser==null || !currentUser.isAdmin()) return false;
		for(User user : usersList) {
			if(user.getUsername().equals(username)) {
				usersList.remove(user);
				updateUsersList();
				return true;
			}
		}
		return false;
	}

	public boolean logout() {
		currentUser = null;
		return false;
	}

}

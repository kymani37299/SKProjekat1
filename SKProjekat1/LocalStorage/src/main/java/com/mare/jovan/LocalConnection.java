package com.mare.jovan;

import java.util.ArrayList;

import com.mare.jovan.user.User;
import com.mare.jovan.util.FileUtil;

public class LocalConnection implements IConnection{

	
	private final static String USER_DATA_PATH = LocalStorage.ROOT_DIR_PATH.concat("users.bin");
	
	private ArrayList<User> usersList;
	private User currentUser;
	
	public LocalConnection() {
		initRoot();
		usersList = getUsersList();
	}
	
	private void updateUsersList() {
		FileUtil.serialize(usersList, USER_DATA_PATH);
	}
	
	private ArrayList<User> getUsersList() {

		if(FileUtil.fileExists(USER_DATA_PATH)) {
			@SuppressWarnings("unchecked")
			ArrayList<User> usersList = (ArrayList<User>) FileUtil.deserialize(USER_DATA_PATH);
			return usersList;
		} else {
			ArrayList<User> usersList = new ArrayList<User>();
			FileUtil.serialize(usersList, USER_DATA_PATH);
			
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

	public EProcessResult addUser(User user) {
		if(!noUsers() && (currentUser==null || !currentUser.isAdmin())) return EProcessResult.DENIED_ACCESS;
		usersList.add(user);
		updateUsersList();
		return EProcessResult.PROCESS_SUCCESS;
	}

	public EProcessResult banUser(String username) {
		if(currentUser==null || !currentUser.isAdmin()) return EProcessResult.DENIED_ACCESS;
		for(User user : usersList) {
			if(user.getUsername().equals(username)) {
				usersList.remove(user);
				updateUsersList();
				return EProcessResult.PROCESS_SUCCESS;
			}
		}
		return EProcessResult.USER_NOT_FOUND;
	}

	public boolean logout() {
		currentUser = null;
		return true;
	}

	public boolean isLoggedIn() {
		return currentUser!=null;
	}
	
	public static void initRoot() {
		java.io.File dir = new java.io.File(LocalStorage.ROOT_DIR_PATH);
		if(!dir.exists())
			dir.mkdir();
	}
}
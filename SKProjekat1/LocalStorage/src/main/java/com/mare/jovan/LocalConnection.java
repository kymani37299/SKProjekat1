package com.mare.jovan;

import java.util.ArrayList;

import com.mare.jovan.user.User;
import com.mare.jovan.util.FileUtil;

/**
*	<p>This class (@code LocalConnection) implements (@code IConnection) and 
*	is responsible for manipulation of users.</p>
*
*	@author Marko Sreckovic
*/
public class LocalConnection implements IConnection{

	
	private static String USER_DATA_PATH;
	
	private ArrayList<User> usersList;
	private User currentUser;
	
	/**
	 * Creates new LocalConnection instance and initializes root directory using given path
	 * @param rootDir path to root directory
	 */
	public LocalConnection(String rootDir) {
		LocalStorage.updateRootPath(rootDir);
		USER_DATA_PATH = LocalStorage.ROOT_DIR_PATH.concat("users.bin");
		initRoot();
		usersList = getUsersList();
		
	}
	
	/**
	 * Creates new LocalConnection instance and initializes root directory using predefined path
	 */
	public LocalConnection() {
		USER_DATA_PATH = LocalStorage.ROOT_DIR_PATH.concat("users.bin");
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

	public void logout() {
		currentUser = null;
	}

	public boolean isLoggedIn() {
		return currentUser!=null;
	}
	
	private static void initRoot() {
		java.io.File dir = new java.io.File(LocalStorage.ROOT_DIR_PATH);
		if(!dir.exists())
			dir.mkdir();
	}
}
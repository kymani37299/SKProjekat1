package com.mare.jovan;

import java.util.ArrayList;
import com.mare.jovan.user.User;
import com.mare.jovan.util.FileUtil;

public class DropboxConnection implements IConnection {

	private DropboxTransferProvider provider;
	
	private final static String USER_DATA_PATH = "users.bin";
	
	private ArrayList<User> usersList;
	private User currentUser;
	
	public DropboxConnection() {
		provider = DropboxTransferProvider.getInstance();
		provider.download(USER_DATA_PATH, USER_DATA_PATH);
		usersList = getUsersList();
	}
	
	private boolean updateUsersList() {
		FileUtil.serialize(usersList, USER_DATA_PATH);
		return provider.update(USER_DATA_PATH, USER_DATA_PATH);
	}
	
	private ArrayList<User> getUsersList() {
		boolean result = provider.download(USER_DATA_PATH, USER_DATA_PATH);
		if(result) {
			@SuppressWarnings("unchecked")
			ArrayList<User> usersList = (ArrayList<User>) FileUtil.deserialize(USER_DATA_PATH);
			return usersList;
		} else {
			ArrayList<User> usersList = new ArrayList<User>();
			FileUtil.serialize(usersList, USER_DATA_PATH);
			result = provider.upload(USER_DATA_PATH, USER_DATA_PATH);
			if(!result) {
				System.out.println("Doslo je do greske prilikom inicijalizacije DropboxConnection.");
				System.exit(1);
			}
			return usersList;
		}
	}
	
	public boolean noUsers() {
		return usersList.isEmpty();
	}
	
	public boolean login(String username, String password) {
		for(User user: usersList) {
			if(user.getUsername().equals(username) &&
					user.getPassword().equals(password)) {
				currentUser = user;
				provider.setEnabled(true);
				return true;
			}
		}
		return false;
	}

	public IStorage getStorage() {
		if(currentUser==null) return null;
		return new DropboxStorage(currentUser);
	}

	public EProcessResult addUser(User user) {
		if(!noUsers() && ( currentUser==null || !currentUser.isAdmin())) return EProcessResult.DENIED_ACCESS;
		usersList.add(user);
		return updateUsersList() ?  EProcessResult.PROCESS_SUCCESS : EProcessResult.PROCESS_FAILED;
	}

	public EProcessResult banUser(String username) {
		if(currentUser==null || !currentUser.isAdmin()) return EProcessResult.DENIED_ACCESS;
		for(User user : usersList) {
			if(user.getUsername().equals(username)) {
				usersList.remove(user);
				return updateUsersList() ?  EProcessResult.PROCESS_SUCCESS : EProcessResult.PROCESS_FAILED;
			}
		}
		return EProcessResult.USER_NOT_FOUND;
	}

	public boolean logout() {
		currentUser = null;
		provider.setEnabled(false);
		return false;
	}

	public boolean isLoggedIn() {
		return currentUser!=null;
	}


}

package com.mare.jovan;

import com.mare.jovan.user.User;

public interface IConnection {
	boolean login(String username, String password);
	IStorage getStorage();
	boolean noUsers();
	EProcessResult addUser(User user);
	EProcessResult banUser(String username);
	boolean isLoggedIn();
	boolean logout();
}

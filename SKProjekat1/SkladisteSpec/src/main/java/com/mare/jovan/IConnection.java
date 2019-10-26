package com.mare.jovan;

import com.mare.jovan.user.User;

public interface IConnection {
	boolean login(String username, String password);
	IStorage getStorage();
	boolean addUser(User user);
	boolean banUser(String username);
	boolean logout();
}

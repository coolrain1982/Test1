package com.web.login;

import com.web.entity.User;

public interface LoginDao {
	public void addUser(User user);
	public void updateUser(User user);
	public User getUser(String userName);
	public User getUser(int id);
}

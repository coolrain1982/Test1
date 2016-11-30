package com.web.user;

import java.util.List;

import com.web.entity.User;

public interface UserDao {
	public void addUser(User user);
	public void updateUser(User user);
	public User getUser(String userName);
	public User getUser(int id);
	public List<User> getUser(int flag, String role);
}

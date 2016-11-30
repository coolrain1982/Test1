package com.web.user;

import java.util.List;

import com.web.entity.User;

public interface UserService {
	public User getUser(int id);
	public User getUser(String name);
	public User addUser(User user);
	public User updateUser(User user);
	public List<User> getCS();
}

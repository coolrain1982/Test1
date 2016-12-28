package com.web.user;

import java.util.List;

import com.web.entity.User;

public interface UserDao {
	public void addUser(User user);
	public void updateUser(User user);
	public User getUser(String userName);
	public User getUser(int id);
	public List<User> getUser(int flag, String role);
	public boolean checkUserExists(String string, String lowerCase);
	public List<User> getUserByName(String lowerCase, int startIdx, int size);
	public List<User> getUserByTel(String lowerCase, int startIdx, int size);
	public List<User> getAllUser(int startIdx, int size);
	public long countUserByName(String trim);
	public long countAllUser();
	public long countUserByTel(String trim);
	public long countUserBySearch(String queryParam);
	public List<User> getUserBySearch(String queryParam, int startIdx, int size);
}

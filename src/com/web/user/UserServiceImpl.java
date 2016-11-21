package com.web.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.web.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	public UserDao userDao;
	
	@Override
	public User getUser(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User addUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}

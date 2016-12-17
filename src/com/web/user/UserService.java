package com.web.user;

import java.util.List;

import org.springframework.util.MultiValueMap;
import com.web.entity.User;

public interface UserService {
	public User getUser(String name);
	public List<User> getCS();
	public void addUser(MultiValueMap<String, Object> params) throws Exception;
	public void updateUser(String name, MultiValueMap<String, Object> params) throws Exception;
	public void changePassword(String name, MultiValueMap<String, Object> params) throws Exception;
}

package com.web.user;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.web.common.PageForQuery;
import com.web.entity.User;

public interface UserService {
	public User getUser(String name);
	public List<User> getCS();
	public void addUser(MultiValueMap<String, Object> params) throws Exception;
	public void updateUser(String name, MultiValueMap<String, Object> params) throws Exception;
	public void changePassword(String name, MultiValueMap<String, Object> params) throws Exception;
	public List<User> getUserByName(String queryParam, PageForQuery pageForQuery) throws Exception;
	public List<User> getUserByTel(String queryParam, PageForQuery pageForQuery) throws Exception;
	public List<User> getAllUser(PageForQuery pageForQuery) throws Exception;
	long countUserByName(String queryParam) throws Exception;
	long countUserByTel(String queryParam) throws Exception;
	long countAllUser() throws Exception;
	public long countUserBySearch(String queryParam) throws Exception;
	public List<User> getUserBySearch(String queryParam, PageForQuery pageForQuery) throws Exception;
	public User changeDiscount(MultiValueMap<String, Object> params) throws Exception;
}

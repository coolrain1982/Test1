package com.web.user;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.web.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	public UserDao userDao;

	@Override
	@Transactional
	public User getUser(String name) {
		return userDao.getUser(name);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void addUser(MultiValueMap<String, Object> params) throws Exception {
		String name, password, mobile, qq, email;
		
		if (params.containsKey("name") && params.get("name").size() > 0 &&
				!params.get("name").get(0).toString().trim().equals("")) {
			name = params.get("name").get(0).toString().trim();
			
			Pattern p = Pattern.compile("^[a-zA-Z_0-9]{4,20}$");
			if (!p.matcher(name).matches()) {
				throw new Exception("用户名格式不正确：只能含有字母、数字和下划线！");
			}
		} else {
			throw new Exception("用户名输入不正确！");
		}
		
		if (params.containsKey("password") && params.get("password").size() > 0 &&
				!params.get("password").get(0).toString().trim().equals("")) {
			password = params.get("password").get(0).toString().trim();
			
			if (password.length() != 32) {
				throw new Exception("密码输入不正确！");
			}
		} else {
			throw new Exception("密码输入不正确！");
		}
		
		if (params.containsKey("email") && params.get("email").size() > 0 &&
				!params.get("email").get(0).toString().trim().equals("")) {
			email = params.get("email").get(0).toString().trim();
			
			if (email.length() > 40) {
				throw new Exception("email长度不能超过40位！");
			}
		} else {
			throw new Exception("email长度不能超过40位！");
		}
		
		if (params.containsKey("mobile") && params.get("mobile").size() > 0 &&
				!params.get("mobile").get(0).toString().trim().equals("")) {
			mobile = params.get("mobile").get(0).toString().trim();
			
			if (mobile.length() != 11) {
				throw new Exception("手机号码必须为11位！");
			}
		} else {
			throw new Exception("手机号码输入不正确！");
		}
		
		if (params.containsKey("qq") && params.get("qq").size() > 0 &&
				!params.get("qq").get(0).toString().trim().equals("")) {
			qq = params.get("qq").get(0).toString().trim();
			
			if (qq.length() > 20) {
				throw new Exception("QQ号码必须小于20位！");
			}
		} else {
			throw new Exception("QQ号码输入不正确！");
		}
		
		//查询是否有相同信息的用户存在
		if (userDao.checkUserExists("name", name.toLowerCase())) {
			throw new Exception("该用户名已经存在！");
		}
		
		if (userDao.checkUserExists("email", email.toLowerCase())) {
			throw new Exception("该email已经存在！");
		}
		
		if (userDao.checkUserExists("mobile", mobile.toLowerCase())) {
			throw new Exception("该手机号码已经存在！");
		}
		
		User user = new User();
		user.setDiscount(100);
		user.setName(name);
		user.setFlag(1);
		user.setMobile(mobile);
		user.setPassword(password);
		user.setEmail(email);
		user.setQq(qq);
		user.setRole("ROLE_USER");
		
		userDao.addUser(user);	
	}

	@Override
	public List<User> getCS() {
		return userDao.getUser(1, "ROLE_CS");
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateUser(String name, MultiValueMap<String, Object> params) throws Exception {
		User user = userDao.getUser(name);
		if (user == null) {
			throw new Exception(String.format("用户[%s]不存在", name));
		}
		
		String email, mobile, qq; 
		boolean update = false;
		
		if (params.containsKey("email") && params.get("email").size() > 0) {
			email = params.get("email").get(0).toString().trim();
			if (!email.equalsIgnoreCase(user.getEmail())) {
				if (email.length() > 40 || email.length() < 6) {
					throw new Exception("email长度不能超过40位且大于6位！");
				}
				//检查是否有重复的email
				if (userDao.checkUserExists("email", email.toLowerCase())) {
					throw new Exception("该email已经存在！");
				}
				
				user.setEmail(email);
				update = true;
			}
		}
		
		if (params.containsKey("mobile") && params.get("mobile").size() > 0) {
			mobile = params.get("mobile").get(0).toString().trim();
			
			if (!mobile.equalsIgnoreCase(user.getMobile())) {
				if (mobile.length() != 11) {
					throw new Exception("手机号码必须为11位！");
				}
				
				//检查是否有重复的mobile
				if (userDao.checkUserExists("mobile", mobile.toLowerCase())) {
					throw new Exception("该手机号码已经存在！");
				}
				
				user.setMobile(mobile);
				update = true;
			}
		}
		
		if (params.containsKey("qq") && params.get("qq").size() > 0) {
			qq = params.get("qq").get(0).toString().trim();
			if (!qq.equalsIgnoreCase(user.getQq())) {
				if (qq.length() > 20 || qq.length() < 5) {
					throw new Exception("QQ号码必须小于20位且大于4位！");
				}
				
				user.setQq(qq);
				update = true;
			}
		}
		if (update) {
			userDao.updateUser(user);
		}
	}

}

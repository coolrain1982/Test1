package com.web.security;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.entity.User;
import com.web.login.LoginDao;

@Service("myUserDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {

	@Resource
	private LoginDao loginDao;
	
	@Resource
	private PasswordEncoder passwordEcoder;
	
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		if (name == null || name.equals("")) {
			throw new UsernameNotFoundException("No user name");
		}
		
		User user = loginDao.getUser(name);
		
		if (user == null || user.getFlag() == 0) {
			throw new UsernameNotFoundException("invalid user");
		}
		
		return new MyUserDetails(user.getName(), user.getPassword(), user.getId(), true);
		
	}

}

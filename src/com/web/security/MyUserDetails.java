package com.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username, password;
	private int role;
	private boolean enabled;
	
    public MyUserDetails(String name, String pwd, int role, boolean enabled) {
		super();
		username = name;
		password = pwd;
		this.role = role;
		this.enabled = enabled;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> gal = new ArrayList<>();
		switch (role) {
		case 0:
			gal.add(new SimpleGrantedAuthority("ROLE_USER"));
			break;
		case 1:
			gal.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			break;
		default:
			break;
		}
		return gal;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setUserName(String name) {
		this.username = name;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}

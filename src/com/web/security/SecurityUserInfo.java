package com.web.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("security")
public class SecurityUserInfo {
	
	@RequestMapping("/getLoginUserName.do")
	@ResponseBody
	public UserDetails getUserName() {
		
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		return null;
	}
}

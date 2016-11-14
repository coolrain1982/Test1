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
	public String getUserName() {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (userDetails != null) {
			return String.format("%s\"name\": \"%s\" %s", "{", userDetails.getUsername(), "}");
		}
		
		return String.format("%s%s", "{", "}");
	}
}

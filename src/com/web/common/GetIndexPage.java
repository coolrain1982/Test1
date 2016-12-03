package com.web.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GetIndexPage {

	@RequestMapping("index.do")
	@ResponseBody
	public Map<String, String> getIndexHtml() {
		
        Map<String, String> rtn = new HashMap<>();
        rtn.put("page", "login.html");
		
		// 先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (UserDetails.class.isInstance(o)) {
			UserDetails user = (UserDetails)o;
			for(@SuppressWarnings("rawtypes")
			Iterator it = user.getAuthorities().iterator(); it.hasNext();) {
				
				Object authObj = it.next();
				if (authObj.toString().equalsIgnoreCase("role_admin")) {
					rtn.put("page", "admin/index.html");
				} else if  (authObj.toString().equalsIgnoreCase("role_cs")) {
					rtn.put("page", "cs/index.html");
			    } else if  (authObj.toString().equalsIgnoreCase("role_user")) {
			    	rtn.put("page", "index.html");
				}
			}
		}

		return rtn;
	}
	
	@RequestMapping("loginFail.do")
	@ResponseBody
	public Map<String, String> returnFailHtml() {
		// 先取用户
		Map<String, String> rtn = new HashMap<>();
		
		rtn.put("page", "login.html?error=1");
		
		return rtn;
	}
}

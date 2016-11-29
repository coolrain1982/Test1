package com.web.common;

import java.util.Iterator;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GetIndexPage {

	@RequestMapping("index.do")
	public String getIndexHtml() {
		// 先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (UserDetails.class.isInstance(o)) {
			UserDetails user = (UserDetails)o;
			for(@SuppressWarnings("rawtypes")
			Iterator it = user.getAuthorities().iterator(); it.hasNext();) {
				
				Object authObj = it.next();
				if (authObj.toString().equalsIgnoreCase("role_admin")) {
					return "admin/index";
				} else if  (authObj.toString().equalsIgnoreCase("role_cs")) {
					return "cs/index";
			    } else if  (authObj.toString().equalsIgnoreCase("role_user")) {
					return "index";
				}
			}
		}

		return "login";
	}
}

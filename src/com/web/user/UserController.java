package com.web.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.PageForQuery;
import com.web.entity.User;

@Controller
public class UserController {

	@Resource
	public UserService userSrv;

	@RequestMapping("user/getDiscount.do")
	@ResponseBody
	public Map<String, Object> getLoginUserDiscount() {

		Map<String, Object> rtnMap = new HashMap<>();

		// 先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = "";
		if (UserDetails.class.isInstance(o)) {
			userName = ((UserDetails) o).getUsername();
		} else {
			rtnMap.put("error", 99);
			rtnMap.put("msg", "请先登录系统！");
			return rtnMap;
		}

		User user = userSrv.getUser(userName);
		if (user == null) {
			rtnMap.put("error", 99);
			rtnMap.put("msg", String.format("用户名[%s]不存在，请重新登录系统！", userName));
			return rtnMap;
		}

		if (user.getDiscount() == null) {
			rtnMap.put("discount", 100);
		} else {
			rtnMap.put("discount", user.getDiscount());
		}

		rtnMap.put("name", user.getName());
		rtnMap.put("email", user.getEmail());
		rtnMap.put("mobile", user.getMobile());
		rtnMap.put("qq", user.getQq());

		return rtnMap;
	}
	
	@RequestMapping("user/update.do")
	@ResponseBody
	public Map<String, Object> updateUser(@RequestParam MultiValueMap<String, Object> params) {

		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);

		// 先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = "";
		if (UserDetails.class.isInstance(o)) {
			userName = ((UserDetails) o).getUsername();
		} else {
			rtnMap.put("error", 99);
			rtnMap.put("msg", "请先登录系统！");
			return rtnMap;
		}
		
		try {
			userSrv.updateUser(userName, params);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
		}

		return rtnMap;
	}

	@RequestMapping("register.do")
	@ResponseBody
	public Map<String, Object> saveNewUser(@RequestParam MultiValueMap<String, Object> params) {

		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);

		try {
			userSrv.addUser(params);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
		}

		return rtnMap;
	}
	
	@RequestMapping("user/getUserInfo.do")
	@ResponseBody
	public Map<String, Object> getUserInfo(@RequestParam String username) {

		Map<String, Object> rtnMap = new HashMap<>();
		String loginUserName;
    	// 先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (UserDetails.class.isInstance(o)) {
			loginUserName = ((UserDetails) o).getUsername();
		} else {
			rtnMap.put("error", "请先登录系统！");
			return rtnMap;
		}

		User loginUser = userSrv.getUser(loginUserName);
		if (loginUser == null) {
			rtnMap.put("error", String.format("用户名[%s]不存在，请重新登录系统！", loginUserName));
			return rtnMap;
		}
		
		if (!loginUser.getRole().equalsIgnoreCase("role_admin")) {
			if (!loginUserName.equalsIgnoreCase(username)) {
				rtnMap.put("error", "您只能查看自己的详细信息");
				return rtnMap;
			}
		}
		
		User user = userSrv.getUser(username);
		if (user == null) {
			rtnMap.put("error", String.format("用户名[%s]不存在，请重新登录系统！", user));
			return rtnMap;
		}

		User rtnUser = new User();
		rtnUser.setEmail(user.getEmail());
		rtnUser.setName(user.getName());
		rtnUser.setMobile(user.getMobile());
		rtnUser.setQq(user.getQq());

		rtnMap.put("userInfo", rtnUser);

		return rtnMap;
	}
	
	@RequestMapping("changepassword.do")
	@ResponseBody
	public Map<String, Object> changePassword(@RequestParam MultiValueMap<String, Object> params) {

		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		String loginUserName;
    	// 先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (UserDetails.class.isInstance(o)) {
			loginUserName = ((UserDetails) o).getUsername();
		} else {
			rtnMap.put("error", "请先登录系统！");
			return rtnMap;
		}

		try {
			userSrv.changePassword(loginUserName, params);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
		}

		return rtnMap;
	}
	
	@RequestMapping("admin/getAllUser.do")
	@ResponseBody
	public Map<String, Object> getAllUserInfo(
			    @RequestParam int queryType, 
			    @RequestParam String queryParam, 
	            @RequestParam int page, 
	            @RequestParam int size) {

		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		String loginUserName;
    	// 先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (UserDetails.class.isInstance(o)) {
			loginUserName = ((UserDetails) o).getUsername();
		} else {
			rtnMap.put("error", "请先登录系统！");
			return rtnMap;
		}

		User loginUser = userSrv.getUser(loginUserName);
		if (loginUser == null) {
			rtnMap.put("error", String.format("用户名[%s]不存在，请重新登录系统！", loginUserName));
			return rtnMap;
		}
		
		if (!loginUser.getRole().equalsIgnoreCase("role_admin")) {
				rtnMap.put("error", "您没有权限进行此操作！");
				return rtnMap;
		}
		
		List<User> userInfoList = null;
		long recordSize = 0;
		
		switch (queryType) {
		case 1:
			//根据用户名称查询
			try {
				recordSize = userSrv.countUserByName(queryParam);
			} catch (Exception e) {
				rtnMap.put("error", "根据用户名称查询用户总数失败" + e.getMessage());
				return rtnMap;
			}
			
			rtnMap.put("count", recordSize);
			if (recordSize == 0) {
				rtnMap.put("status", 1);
				return rtnMap;
			}
			
			try {
				userInfoList = userSrv.getUserByName(queryParam, new PageForQuery(page, size));
			} catch (Exception e) {
				rtnMap.put("error", "根据用户名称查询用户信息失败" + e.getMessage());
				return rtnMap;
			}
			break;
		case 2:
			
			try {
				recordSize = userSrv.countUserByTel(queryParam);
			} catch (Exception e) {
				rtnMap.put("error", "根据电话号码查询用户总数失败" + e.getMessage());
				return rtnMap;
			}
			
			rtnMap.put("count", recordSize);
			if (recordSize == 0) {
				rtnMap.put("status", 1);
				return rtnMap;
			}
			
			try {
				userInfoList = userSrv.getUserByTel(queryParam, new PageForQuery(page, size));
			} catch (Exception e) {
				rtnMap.put("error", "根据电话号码查询用户信息失败" + e.getMessage());
				return rtnMap;
			}
			//根据电话号码查询
			break;
		case 9:
			//根据搜索内容查询
			try {
				recordSize = userSrv.countUserBySearch(queryParam);
			} catch (Exception e) {
				rtnMap.put("error", "根据搜索内容查询用户总数失败" + e.getMessage());
				return rtnMap;
			}
			
			rtnMap.put("count", recordSize);
			if (recordSize == 0) {
				rtnMap.put("status", 1);
				return rtnMap;
			}
			
			try {
				userInfoList = userSrv.getUserBySearch(queryParam, new PageForQuery(page, size));
			} catch (Exception e) {
				rtnMap.put("error", "根据搜索内容查询用户信息失败" + e.getMessage());
				return rtnMap;
			}
			
			break;
		default:
			try {
				recordSize = userSrv.countAllUser();
			} catch (Exception e) {
				rtnMap.put("error", "查询用户总数失败" + e.getMessage());
				return rtnMap;
			}
			
			rtnMap.put("count", recordSize);
			if (recordSize == 0) {
				return rtnMap;
			}
			
			//查询所有的	
			try {
				userInfoList = userSrv.getAllUser(new PageForQuery(page, size));
			} catch (Exception e) {
				rtnMap.put("error", "查询用户信息失败" + e.getMessage());
				return rtnMap;
			}
		}

		rtnMap.put("list", userInfoList);
		rtnMap.put("status", 1);

		return rtnMap;
	}
	
	@RequestMapping("admin/changediscount.do")
	@ResponseBody
	public Map<String, Object> changeDiscount(@RequestParam MultiValueMap<String, Object> params) {

		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);

		// 先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String loginUserName = "";
		if (UserDetails.class.isInstance(o)) {
			loginUserName = ((UserDetails) o).getUsername();
		} else {
			rtnMap.put("error", "请先登录系统！");
			return rtnMap;
		}
		
		User loginUser = userSrv.getUser(loginUserName);
		if (loginUser == null) {
			rtnMap.put("error", String.format("用户名[%s]不存在，请重新登录系统！", loginUserName));
			return rtnMap;
		}
		
		if (!loginUser.getRole().equalsIgnoreCase("role_admin")) {
			rtnMap.put("error", "您没有权限进行此操作！");
			return rtnMap;
		}
		
		try {
			User user = userSrv.changeDiscount(params);
			rtnMap.put("status", 1);
			rtnMap.put("discount", user.getDiscount());
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
		}

		return rtnMap;
	}
}

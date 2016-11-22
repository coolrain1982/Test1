package com.web.user;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.entity.User;
import com.web.order.ctrl.OrderCtrlResp;

@Controller
@RequestMapping("user")
public class UserController {

	@Resource
	public UserService userSrv;

	@RequestMapping("/getDiscount.do")
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
		
		return rtnMap;
	}
}

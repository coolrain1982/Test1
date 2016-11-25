package com.web.order.ctrl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.entity.Order;
import com.web.order.service.SaveNewOrderService;

@Controller
@RequestMapping("userorder")
public class UserOrderCtrl {
	
	@Resource
	public SaveNewOrderService saveSrv;
    
	@RequestMapping("/neworder.do")
	@ResponseBody
	public Map<String, Object> SaveNewOrder(@RequestParam MultiValueMap<String, Object> params, 
			@RequestParam("files")MultipartFile[] files) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		//先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName ="";
		if (UserDetails.class.isInstance(o)) {
			userName = ((UserDetails) o).getUsername();
		} else {
			rtnMap.put("error", "请先登录系统后再进行订单操作！");
			return rtnMap;
		}
		
		try {
			
			Order order = saveSrv.saveNewOrder(userName, params, files);
			
			rtnMap.put("status", 1);
			rtnMap.put("orderid", order.getOrder_id());
			rtnMap.put("createdate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreate_date().getTime()));	
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
		}
		
		return rtnMap;
	}
}
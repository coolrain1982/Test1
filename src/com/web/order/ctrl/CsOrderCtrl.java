package com.web.order.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.PageForQuery;
import com.web.entity.Order;
import com.web.entity.User;
import com.web.order.service.GetCSOrderService;
import com.web.order.service.SaveNewOrderService;
import com.web.user.UserService;

@Controller
@RequestMapping("cs")
public class CsOrderCtrl {
	
	@Resource
	public SaveNewOrderService saveSrv;
	
	@Resource
	public GetCSOrderService getSrv;
	
	@Resource 
    public UserService userSrv;
	
	@RequestMapping("/getOrder.do")
	@ResponseBody
	public Map<String, Object> getOrder(@RequestParam int status, 
			                            @RequestParam int page, 
			                            @RequestParam int size ) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		String userName = "";
		
		try {
			userName = UserOrderCtrl.getLoginName();
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
			return rtnMap;
		}
		
		//查询用户
		User user = null;
		try {
			user = userSrv.getUser(userName);
		} catch (Exception e) {
			rtnMap.put("error", String.format("未知登陆用户[%s]--%s" , userName , e.getMessage()));
			return rtnMap;
		}
		
		if (!user.getRole().equalsIgnoreCase("role_cs") &&
			!user.getRole().equalsIgnoreCase("role_admin")) {
			rtnMap.put("error", String.format("登陆用户权限错误[%s]" , userName));
			return rtnMap;
		}
		
		try {
			long count = getSrv.getOrderCount(user.getId(), status);
			rtnMap.put("count", count);
			
			if (count == 0) {
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询订单总记录数失败：%s! status[%s],page[%s],size[%s]" ,
					e.getMessage(), status, page, size));
			return rtnMap;
		}
				
		try {
			List<Order> orders = getSrv.getOrders(user.getId(), status, new PageForQuery(page, size));
			rtnMap.put("list", orders);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询订单数据失败：%s! status[%s],page[%s],size[%s]" ,
					e.getMessage(), status, page, size));
		}
		
		return rtnMap;
	}
	
	@RequestMapping("/auditOrder.do")
	@ResponseBody
	public Map<String, Object> auditOrder(@RequestParam int status, 
			                            @RequestParam int orderid,
			                            @RequestParam String auditmark) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 1);
		rtnMap.put("error", "heheheh");
		
		if (auditmark.length() > 100) {
			rtnMap.put("error", "审核意见不能大于100个字符");
			return rtnMap;
		}
		
		String userName = "";
		
		try {
			userName = UserOrderCtrl.getLoginName();
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
			return rtnMap;
		}
		
		//查询用户
		User user = null;
		try {
			user = userSrv.getUser(userName);
		} catch (Exception e) {
			rtnMap.put("error", String.format("未知登陆用户[%s]--%s" , userName , e.getMessage()));
			return rtnMap;
		}
		
		if (!user.getRole().equalsIgnoreCase("role_cs") &&
			!user.getRole().equalsIgnoreCase("role_admin")) {
			rtnMap.put("error", String.format("登陆用户权限错误[%s]" , userName));
			return rtnMap;
		}
		
		//根据订单ID查找出订单
		try {
			
		} catch (Exception e) {
			
		}
		
		return rtnMap;
	}
}
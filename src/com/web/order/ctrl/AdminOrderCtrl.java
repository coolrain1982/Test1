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
import com.web.entity.PayInfo;
import com.web.entity.User;
import com.web.order.service.AuditOrderService;
import com.web.order.service.GetOrderService;
import com.web.order.service.PayOrderService;
import com.web.order.service.SaveNewOrderService;
import com.web.user.UserService;

@Controller
@RequestMapping("admin")
public class AdminOrderCtrl {
	
	@Resource
	public SaveNewOrderService saveSrv;
	
	@Resource
	public AuditOrderService auditSrv;
	
	@Resource
	public GetOrderService getSrv;
	
	@Resource 
    public UserService userSrv;
	
	@Resource
	public PayOrderService paySrv;
	
	@RequestMapping("/getAllOrder.do")
	@ResponseBody
	public Map<String, Object> getOrder(@RequestParam int command, 
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
		
		if (!user.getRole().equalsIgnoreCase("role_admin")) {
			rtnMap.put("error", "您不是管理员，无权进行此操作！");
			return rtnMap;
		}
		
		try {
			long count = getSrv.getOrderCount();
			rtnMap.put("count", count);
			
			if (count == 0) {
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询订单总记录数失败：%s! " ,
					e.getMessage()));
			return rtnMap;
		}
				
		try {
			List<Order> orders = getSrv.getOrders(new PageForQuery(page, size));
			rtnMap.put("list", orders);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询订单数据失败：%s! status[%s],page[%s],size[%s]" ,
					e.getMessage()));
		}
		
		return rtnMap;
	}
	
	@RequestMapping("/getProcessOrder.do")
	@ResponseBody
	public Map<String, Object> getProcessOrder(@RequestParam int command, 
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
		
		if (!user.getRole().equalsIgnoreCase("role_admin")) {
			rtnMap.put("error", "您不是管理员，无权进行此操作！");
			return rtnMap;
		}
		
		//查找
		try {
			long count = getSrv.getProcessOrderCount();
			rtnMap.put("count", count);
			
			if (count == 0) {
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询订单总记录数失败：%s!" ,
					e.getMessage()));
			return rtnMap;
		}
				
		try {
			List<Order> orders = getSrv.getProcessOrders(new PageForQuery(page, size));
			rtnMap.put("list", orders);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询订单数据失败：%s!" ,
					e.getMessage()));
		}
		
		return rtnMap;
	}
	
	@RequestMapping("/getDoingOrder.do")
	@ResponseBody
	public Map<String, Object> getDoingOrder(@RequestParam int command, 
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
		
		if (!user.getRole().equalsIgnoreCase("role_admin")) {
			rtnMap.put("error", "您不是管理员，无权进行此操作！");
			return rtnMap;
		}
		
		//查找
		try {
			long count = getSrv.getDoingOrderCount();
			rtnMap.put("count", count);
			
			if (count == 0) {
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询订单总记录数失败：%s!" ,
					e.getMessage()));
			return rtnMap;
		}
				
		try {
			List<Order> orders = getSrv.getDoingOrders(new PageForQuery(page, size));
			rtnMap.put("list", orders);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询订单数据失败：%s!" ,
					e.getMessage()));
		}
		
		return rtnMap;
	}
	
	@RequestMapping("/payOrder.do")
	@ResponseBody
	public Map<String, Object> getPayment(@RequestParam long orderId,
			@RequestParam String payaccount, @RequestParam long paysn, @RequestParam double money) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		if (orderId < 1) {
			rtnMap.put("error", String.format("非法的订单编号[%s]", orderId));
			return rtnMap;
		}
		
		if (payaccount==null || payaccount.trim().equals("")) {
			rtnMap.put("error", "请填入您的支付宝账户");
			return rtnMap;
		}
		
		payaccount = payaccount.trim();
		
		if (payaccount.length() > 50) {
			rtnMap.put("error", "您的支付宝账户名称不能多于50个字符！");
			return rtnMap;
		}
		
		String paysnStr = String.valueOf(paysn).trim();
		if (paysnStr.equals("")) {
			rtnMap.put("error", "请填入付款订单号后8位!");
			return rtnMap;
		}
		
		if (paysnStr.length() != 8) {
			rtnMap.put("error", "请填入正确的付款订单号！");
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
			rtnMap.put("error", String.format("未知登陆用户[%s]:%s" , userName , e.getMessage()));
			return rtnMap;
		}
		
		PayInfo payInfo = new PayInfo();
		payInfo.setMoney(money);
		payInfo.setPayer(payaccount);
		payInfo.setSn(paysnStr);
		//
		try {
			paySrv.payOrder(user, orderId, payInfo);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error",	e.getMessage());
		}
		
		return rtnMap;
	}
	
	@RequestMapping("/auditOrder.do")
	@ResponseBody
	public Map<String, Object> auditOrder(@RequestParam int status, 
			                            @RequestParam long orderid,
			                            @RequestParam String auditmark) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		if (auditmark==null || auditmark=="" || auditmark.trim()=="") {
			rtnMap.put("error", "请填入审核意见");
			return rtnMap;
		}
		
		if (status != 2 && status != 3 ) {
			rtnMap.put("error", "订单审核状态错误！");
			return rtnMap;
		}
		
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
		
		//审核订单
		try {
			auditSrv.auditOrder(user, status, orderid, auditmark);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("审核订单时出错：%s" , e.getMessage()));
			return rtnMap;
		}
		
		return rtnMap;
	}
}
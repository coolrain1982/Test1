package com.web.order.ctrl;

import java.text.SimpleDateFormat;
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
import org.springframework.web.multipart.MultipartFile;

import com.web.common.PageForQuery;
import com.web.entity.Order;
import com.web.entity.PayInfo;
import com.web.entity.User;
import com.web.order.service.GetOrderService;
import com.web.order.service.PayOrderService;
import com.web.order.service.SaveNewOrderService;
import com.web.user.UserService;

@Controller
@RequestMapping("userorder")
public class UserOrderCtrl {
	
	@Resource
	public SaveNewOrderService saveSrv;
	
	@Resource
	public GetOrderService getSrv;
	
	@Resource 
    public UserService userSrv;
	
	@Resource
	public PayOrderService paySrv;
    
	@RequestMapping("/neworder.do")
	@ResponseBody
	public Map<String, Object> saveNewOrder(@RequestParam MultiValueMap<String, Object> params, 
			@RequestParam("files")MultipartFile[] files) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		String userName = "";
		
		try {
			userName = getLoginName();
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
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
	
	@RequestMapping("/getAllOrder.do")
	@ResponseBody
	public Map<String, Object> getOrder(@RequestParam int command, 
			                            @RequestParam int page, 
			                            @RequestParam int size ) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		String userName = "";
		
		try {
			userName = getLoginName();
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
		
		try {
			long count = getSrv.getOrderCount(user.getId(), 99);
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
			List<Order> orders = getSrv.getOrders(user.getId(), 99, new PageForQuery(page, size));
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
			userName = getLoginName();
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
		
		//查找
		try {
			long count = getSrv.getProcessOrderCount(user.getId());
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
			List<Order> orders = getSrv.getProcessOrders(user.getId(), new PageForQuery(page, size));
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
			userName = getLoginName();
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
		
		//查找
		try {
			long count = getSrv.getDoingOrderCount(user.getId());
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
			List<Order> orders = getSrv.getDoingOrders(user.getId(), new PageForQuery(page, size));
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
	public Map<String, Object> payOrder(@RequestParam long orderId,
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
			userName = getLoginName();
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
	
	public static String getLoginName() throws Exception {
		// 先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (UserDetails.class.isInstance(o)) {
			return ((UserDetails) o).getUsername();
		} else {
			throw new Exception("请先登录系统后再进行相关操作！");
		}
	}
}
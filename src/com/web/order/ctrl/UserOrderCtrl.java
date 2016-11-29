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
import com.web.entity.User;
import com.web.order.service.GetOrderService;
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
	
	@RequestMapping("/getOrder.do")
	@ResponseBody
	public Map<String, Object> getOrder(@RequestParam int status, 
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
	
	public static String getLoginName() throws Exception {
		// 先取用户
		Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (UserDetails.class.isInstance(o)) {
			return ((UserDetails) o).getUsername();
		} else {
			throw new Exception("请先登录系统后再进行订单操作！");
		}
	}
}
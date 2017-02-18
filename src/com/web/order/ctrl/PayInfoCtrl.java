package com.web.order.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.entity.PayInfo;
import com.web.entity.User;
import com.web.order.service.PayOrderService;
import com.web.user.UserService;

@Controller
@RequestMapping("payinfo")
public class PayInfoCtrl {
	@Resource 
    public UserService userSrv;
	
	@Resource 
	public PayOrderService paySrv;
	
	@RequestMapping("/getPayInfo.do")
	@ResponseBody
	public Map<String, Object> getOrderPayInfo(@RequestParam long orderid) {
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
		
		try {
			List<PayInfo> payInfos = paySrv.getPayInfo(user, orderid);
			rtnMap.put("payInfo", payInfos);
			rtnMap.put("status", 1);
			
			if (payInfos != null && payInfos.size() > 0) {
				for(int i = payInfos.size() - 1; i > 0; i --) {
					payInfos.remove(i);
				}
			}
			
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询订单支付信息失败：%s!" ,
					e.getMessage()));
		}
		return rtnMap;
	}
	
	@RequestMapping("/getAllPayInfo.do")
	@ResponseBody
	public Map<String, Object> getAllOrderPayInfo(@RequestParam long orderid) {
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
		
		try {
			List<PayInfo> payInfos = paySrv.getPayInfo(user, orderid);
			rtnMap.put("payInfo", payInfos);
			rtnMap.put("status", 1);
			
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询订单支付信息失败：%s!" ,
					e.getMessage()));
		}
		return rtnMap;
	}
}

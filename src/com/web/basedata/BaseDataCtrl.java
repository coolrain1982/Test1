package com.web.basedata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.PageForQuery;
import com.web.entity.Commision;
import com.web.entity.ExchangeRate;
import com.web.entity.PaypalFee;
import com.web.entity.User;
import com.web.order.ctrl.UserOrderCtrl;
import com.web.user.UserService;

@Controller
@RequestMapping("basedata")
public class BaseDataCtrl {
	
	@Resource
	public BaseDataService baseDataSrv;
	
	@Resource 
    public UserService userSrv;
	
	@RequestMapping("/neworder.do")
	@ResponseBody
	public Map<String, Object> getBaseDataForNewOrder(@RequestParam MultiValueMap<String, Object> params) {
		
		return baseDataSrv.getBaseData(1);
	}
	
	@RequestMapping("/getExchange.do")
	@ResponseBody
	public Map<String, Object> getExchange( @RequestParam int page, 
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
			rtnMap.put("error", String.format("你没有权限进行汇率操作!"));
			return rtnMap;
		}
		
		try {
			long count = baseDataSrv.getExchangeCount();
			rtnMap.put("count", count);
			
			if (count == 0) {
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询汇率总记录数失败：%s!" ,
					e.getMessage()));
			return rtnMap;
		}
				
		try {
			List<ExchangeRate> exchanges = baseDataSrv.getExchanges(new PageForQuery(page, size));
			rtnMap.put("list", exchanges);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询汇率数据失败：%s!" ,
					e.getMessage()));
		}
		
		return rtnMap;
	}
	
	@RequestMapping("/getCommision.do")
	@ResponseBody
	public Map<String, Object> getCommision( @RequestParam int page, 
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
			rtnMap.put("error", String.format("你没有权限进行佣金操作!"));
			return rtnMap;
		}
		
		try {
			long count = baseDataSrv.getCommisionCount();
			rtnMap.put("count", count);
			
			if (count == 0) {
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询佣金总记录数失败：%s!" ,
					e.getMessage()));
			return rtnMap;
		}
				
		try {
			List<Commision> commisions = baseDataSrv.getCommisions(new PageForQuery(page, size));
			rtnMap.put("list", commisions);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询佣金数据失败：%s!" ,
					e.getMessage()));
		}
		
		return rtnMap;
	}
	
	@RequestMapping("/getPaypal.do")
	@ResponseBody
	public Map<String, Object> getPaypal( @RequestParam int page, 
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
			rtnMap.put("error", String.format("你没有权限进行Paypal操作!"));
			return rtnMap;
		}
		
		try {
			long count = baseDataSrv.getPaypalCount();
			rtnMap.put("count", count);
			
			if (count == 0) {
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询Paypal总记录数失败：%s!" ,
					e.getMessage()));
			return rtnMap;
		}
				
		try {
			List<PaypalFee> paypals = baseDataSrv.getPaypals(new PageForQuery(page, size));
			rtnMap.put("list", paypals);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("查询Paypal数据失败：%s!" ,
					e.getMessage()));
		}
		
		return rtnMap;
	}
}

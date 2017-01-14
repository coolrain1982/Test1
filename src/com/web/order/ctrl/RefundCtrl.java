package com.web.order.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.entity.Order;
import com.web.entity.RefundInfo;
import com.web.entity.User;
import com.web.refund.service.OrderRefundService;
import com.web.user.UserController;
import com.web.user.UserService;

@Controller
public class RefundCtrl {
	
	@Resource 
    public UserService userSrv;
	
	@Resource
	public OrderRefundService orderRefundSrv;

	@RequestMapping("admin/getRefundOrderById.do")
	@ResponseBody
	public Map<String, Object> getOrder(@RequestParam String searchStr) {
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		try {
			boolean isAdmin = UserController.checkIsAdmin(userSrv);
			if (!isAdmin) {
				rtnMap.put("error", "您没有退款操作权限");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
			return rtnMap;
		}
		
		//看看searchStr能否转为long
		long searchOrderId = 0;
		try {
			searchOrderId = Long.parseLong(searchStr.trim());
		} catch (Exception e) {
			rtnMap.put("error", "搜索的订单ID必须为数字");
			return rtnMap;
		}
		
		long count = 0;
		
		try {
			count = orderRefundSrv.getOrderCountBySearchID(searchOrderId);
		} catch (Exception e) {
			rtnMap.put("error", "查询退款订单总数失败:" + e.getMessage());
			return rtnMap;
		}
		
		if (count ==0) {
			rtnMap.put("error", "没有查询到符合条件的订单！");
			return rtnMap;
		}
		
		if (count > 10) {
			rtnMap.put("error", "查询出的订单数量过多，请输入更精确的查询条件！");
			return rtnMap;
		}
		
	    try {
	    	List<Order> orders = orderRefundSrv.getOrderBySearchID(searchOrderId);
	    	rtnMap.put("status", 1);
	    	rtnMap.put("list", orders);
	    } catch (Exception e) {
	    	rtnMap.put("error", "查询退款订单记录时出错:" + e.getMessage());
			return rtnMap;
	    }
		
		return rtnMap;
	}
	
	@RequestMapping("admin/getRefundInfoByOrderid.do")
	@ResponseBody
	public Map<String, Object> addNoreviewRefund(@RequestParam long orderid) {
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		try {
			boolean isAdmin = UserController.checkIsAdmin(userSrv);
			if (!isAdmin) {
				rtnMap.put("error", "您没有退款的操作权限");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", "获取登录用户信息时出错：" + e.getMessage());
			return rtnMap;
		}
		
	    try {
	    	List<RefundInfo> refundInfos = orderRefundSrv.getRefundInfoByOrder(orderid);
	    	rtnMap.put("status", 1);
	    	rtnMap.put("refundInfos", refundInfos);
	    	
	    } catch (Exception e) {
	    	rtnMap.put("error", "增加退款记录出错:" + e.getMessage());
			return rtnMap;
	    }
		
		return rtnMap;
	}
	
	@RequestMapping("admin/addNoreviewRefund.do")
	@ResponseBody
	public Map<String, Object> addNoreviewRefund(@RequestParam MultiValueMap<String, Object> params) {
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		String userName;
		try {
			userName = UserOrderCtrl.getLoginName();
		} catch (Exception e1) {
			rtnMap.put("error", "请先登录系统！");
			return rtnMap;
		}
		
		// 取用户信息
		User user = userSrv.getUser(userName);
		if (user == null) {
			rtnMap.put("error", "请先登录系统！");
			return rtnMap;
		}
		
		if (!user.getRole().equalsIgnoreCase("role_admin")) {
			rtnMap.put("error", "您没有退款操作权限");
			return rtnMap;
		}
		
	    try {
	    	RefundInfo refundInfo = orderRefundSrv.addRefundInfo(user, 1, params);
	    	rtnMap.put("status", 1);
	    	RefundInfo renRefund = new RefundInfo(
	    			refundInfo.getId(), refundInfo.getStatus(), 
	    			refundInfo.getPay_type(), refundInfo.getRefund_type(),
	    			refundInfo.getSn(), refundInfo.getPayee(), 
	    			refundInfo.getRemark(), refundInfo.getRefund_date(), 
	    			refundInfo.getMoney(), user.getName());
	    	rtnMap.put("refundinfo", renRefund);
	    } catch (Exception e) {
	    	rtnMap.put("error", "增加退款记录出错:" + e.getMessage());
			return rtnMap;
	    }
		
		return rtnMap;
	}
	
	@RequestMapping("admin/addUncompleteRefund.do")
	@ResponseBody
	public Map<String, Object> addUncompleteRefund(@RequestParam MultiValueMap<String, Object> reqParams) {
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		String userName;
		try {
			userName = UserOrderCtrl.getLoginName();
		} catch (Exception e1) {
			rtnMap.put("error", "请先登录系统！");
			return rtnMap;
		}
		
		// 取用户信息
		User user = userSrv.getUser(userName);
		if (user == null) {
			rtnMap.put("error", "请先登录系统！");
			return rtnMap;
		}
		
		if (!user.getRole().equalsIgnoreCase("role_admin")) {
			rtnMap.put("error", "您没有退款操作权限");
			return rtnMap;
		}
		
	    try {
	    	RefundInfo refundInfo = orderRefundSrv.addRefundInfo(user, 2, reqParams);
	    	rtnMap.put("status", 1);
	    	RefundInfo renRefund = new RefundInfo(
	    			refundInfo.getId(), refundInfo.getStatus(), 
	    			refundInfo.getPay_type(), refundInfo.getRefund_type(),
	    			refundInfo.getSn(), refundInfo.getPayee(), 
	    			refundInfo.getRemark(), refundInfo.getRefund_date(), 
	    			refundInfo.getMoney(), user.getName());
	    	rtnMap.put("refundinfo", renRefund);
	    } catch (Exception e) {
	    	rtnMap.put("error", "增加退款记录出错:" + e.getMessage());
			return rtnMap;
	    }
		
		return rtnMap;
	}
	
	public static String getRefundColStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("r.id,r.status,r.pay_type,r.refund_type,r.sn,r.payee,r.remark,r.refund_date,r.money,u.name");
		return sb.toString();
	}
}

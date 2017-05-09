package com.web.review.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.entity.OrderForReview;
import com.web.entity.User;
import com.web.order.ctrl.UserOrderCtrl;
import com.web.order.service.CheckUserForOrder;
import com.web.review.service.OrderReviewService;
import com.web.user.UserController;
import com.web.user.UserService;

@Controller
public class ReviewInputCtrl {
	
	@Resource 
    public UserService userSrv;
	
	@Resource
	public OrderReviewService reviewSrv;
	
	@Resource
	public CheckUserForOrder checkSrv;
	
	@RequestMapping("reviewinfo/getReviewInfo.do")
	@ResponseBody
	public Map<String, Object> adminGetReviewInfo(@RequestParam long orderid) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		try {
			if (!UserController.checkIsAdmin(userSrv)) {
				rtnMap.put("error", "您没有该操作权限");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", "获取登录用户信息时出错：" + e.getMessage());
			return rtnMap;
		}

		try {
			List<OrderForReview> reviewInfos = reviewSrv.getReviews(orderid);
			rtnMap.put("status", 1);
			rtnMap.put("reviewInfos", reviewInfos);

		} catch (Exception e) {
			rtnMap.put("error", "获取review记录出错:" + e.getMessage());
			return rtnMap;
		}

		return rtnMap;
	}
	
	@RequestMapping("user/getReviewByOrderid.do")
	@ResponseBody
	public Map<String, Object> getReviewInfo(@RequestParam long orderid) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		String userName = "";
		User user = null;
		try {
			userName = UserOrderCtrl.getLoginName();
			user = userSrv.getUser(userName);
			if (user == null) {
				throw new Exception("未知用户：" + userName);
			}
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
			return rtnMap;
		}
		
		try {
			if (!checkSrv.canOperateOrder(user, orderid)) {
				rtnMap.put("error", "您没有此操作权限");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
			return rtnMap;
		}

		try {
			List<OrderForReview> reviewInfos = reviewSrv.getReviews(orderid);
			rtnMap.put("status", 1);
			rtnMap.put("reviewInfos", reviewInfos);

		} catch (Exception e) {
			rtnMap.put("error", "获取review记录出错:" + e.getMessage());
			return rtnMap;
		}

		return rtnMap;
	}
	
	@RequestMapping("admin/addReviewInfo.do")
	@ResponseBody
	public Map<String, Object> addReview(@RequestParam MultiValueMap<String, Object> params) {
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		try {
			if (!UserController.checkIsAdmin(userSrv)) {
				rtnMap.put("error", "您没有该操作权限");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", "获取登录用户信息时出错：" + e.getMessage());
			return rtnMap;
		}
		
	    try {
	    	OrderForReview review = reviewSrv.newOrderReview(params);
	    	rtnMap.put("status", 1);
	    	OrderForReview rtnreview = new OrderForReview(
	    			review.getId(), review.getStatus(), review.getA_No(), review.getAudit_name(),
	    			review.getReview_remark(), review.getAudit_remark(), review.getReview_name(),
	    			review.getSubmit_date(), review.getAudit_date(), review.getFinish_date(),
	    			review.getCreate_date());
	    	rtnMap.put("reviewinfo", rtnreview);
	    } catch (Exception e) {
	    	rtnMap.put("error", "增加review记录出错:" + e.getMessage());
			return rtnMap;
	    }
		
		return rtnMap;
	}
	
	@RequestMapping("admin/saveReviewInfo.do")
	@ResponseBody
	public Map<String, Object> saveReview(@RequestParam MultiValueMap<String, Object> params) {
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		try {
			if (!UserController.checkIsAdmin(userSrv)) {
				rtnMap.put("error", "您没有该操作权限");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", "获取登录用户信息时出错：" + e.getMessage());
			return rtnMap;
		}
		
	    try {
	    	OrderForReview review = reviewSrv.saveOrderReview(params);
	    	rtnMap.put("status", 1);
	    	OrderForReview rtnreview = new OrderForReview(
	    			review.getId(), review.getStatus(), review.getA_No(), review.getAudit_name(),
	    			review.getReview_remark(), review.getAudit_remark(), review.getReview_name(),
	    			review.getSubmit_date(), review.getAudit_date(), review.getFinish_date(),
	    			review.getCreate_date());
	    	rtnMap.put("reviewinfo", rtnreview);
	    } catch (Exception e) {
	    	rtnMap.put("error", "保存review记录出错:" + e.getMessage());
			return rtnMap;
	    }
		
		return rtnMap;
	}
	
	@RequestMapping("admin/submitReviewInfo.do")
	@ResponseBody
	public Map<String, Object> submitReview(@RequestParam MultiValueMap<String, Object> params) {
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		try {
			if (!UserController.checkIsAdmin(userSrv)) {
				rtnMap.put("error", "您没有该操作权限");
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", "获取登录用户信息时出错：" + e.getMessage());
			return rtnMap;
		}
		
	    try {
	    	OrderForReview review = reviewSrv.submitOrderReview(params);
	    	rtnMap.put("status", 1);
	    	OrderForReview rtnreview = new OrderForReview(
	    			review.getId(), review.getStatus(), review.getA_No(), review.getAudit_name(),
	    			review.getReview_remark(), review.getAudit_remark(), review.getReview_name(),
	    			review.getSubmit_date(), review.getAudit_date(), review.getFinish_date(),
	    			review.getCreate_date());
	    	rtnMap.put("reviewinfo", rtnreview);
	    } catch (Exception e) {
	    	rtnMap.put("error", "提交review记录出错:" + e.getMessage());
			return rtnMap;
	    }
		
		return rtnMap;
	}

	public static Object getReviewColStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("r.id,r.status,r.a_No,r.audit_name,r.review_remark,r.audit_remark,r.review_name,");
		sb.append("r.submit_date,r.audit_date,r.finish_date,r.create_date");
		return sb.toString();
	}
}

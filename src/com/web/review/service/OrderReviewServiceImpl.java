package com.web.review.service;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.web.entity.Order;
import com.web.entity.OrderForReview;
import com.web.order.dao.OrderDao;
import com.web.review.dao.OrderReviewDao;

@Service
public class OrderReviewServiceImpl implements OrderReviewService {
	
	@Resource
	public OrderReviewDao orderReviewDao; 
	
	@Resource
	public OrderDao orderDao; 


	@Override
	@Transactional
	public List<OrderForReview> getReviews(long orderid) throws Exception {
		return orderReviewDao.getReviews(orderid);
	}


	@Override
	@Transactional(rollbackFor=Exception.class)
	public OrderForReview newOrderReview(MultiValueMap<String, Object> params) throws Exception {
		String reviewer, aNo, remark;
		long orderid;
		
		if (params.containsKey("orderid") && params.get("orderid").size() > 0) {
			try {
				orderid = Long.parseLong(params.get("orderid").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("review订单ID错误！");
			}
		} else {
			throw new Exception("无review订单ID");
		}
		
		if (params.containsKey("sn") && params.get("sn").size() > 0 && 
				!params.get("sn").get(0).toString().trim().equals("")) {
			aNo = params.get("sn").get(0).toString().trim();
			if (aNo.length() > 50) {
				throw new Exception("亚马逊订单号不能超过50位！");
			}
		} else {
			throw new Exception("亚马逊订单号输入不正确！");
		}
		
		if (params.containsKey("reviewer") && params.get("reviewer").size() > 0 && 
				!params.get("reviewer").get(0).toString().trim().equals("")) {
			reviewer = params.get("reviewer").get(0).toString().trim();
			if (reviewer.length() > 50) {
				throw new Exception("reviewer不能超过50个字符！");
			}
		} else {
			throw new Exception("reviewer输入不正确！");
		}
		
		if (params.containsKey("remark") && params.get("remark").size() > 0 && 
				!params.get("remark").get(0).toString().trim().equals("")) {
			remark = params.get("remark").get(0).toString().trim();
			if (remark.length() > 3000) {
				throw new Exception("review内容不能超过3000个字符！");
			}
		} else {
			throw new Exception("请输入备注！");
		}
		
		//根据ID取订单信息
		Order order = null;
		try {
			order = orderDao.getOrderById(orderid);
		} catch (Exception e) {
			throw new Exception("取review订单信息失败:" + e.getMessage());
		}
		
		if (order == null) {
			throw new Exception("review订单不存在！");
		}
		
		if (order.getStatus() < Order.IN_REVIEW) {
			throw new Exception("该订单不可进行review操作！");
		}
		
		OrderForReview review = new OrderForReview();
		review.setA_No(aNo);
		review.setCreate_date(Calendar.getInstance());
		review.setOrder(order);
		review.setReview_name(reviewer);
		review.setReview_remark(remark);
		review.setStatus(4);
		
		orderReviewDao.newOrderReview(review);
		
		return review;
	}


	@Override
	@Transactional(rollbackFor=Exception.class)
	public OrderForReview updateOrderReview(MultiValueMap<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

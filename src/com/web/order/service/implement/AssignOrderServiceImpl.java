package com.web.order.service.implement;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scheduletask.OrderSync;
import com.web.entity.Order;
import com.web.entity.OrderForReview;
import com.web.order.dao.OrderDao;
import com.web.order.service.AssignOrderService;
import com.web.review.dao.OrderReviewDao;

@Service
public class AssignOrderServiceImpl implements AssignOrderService {
	
	@Resource
	public OrderDao orderDao;
	
	@Resource
	public OrderReviewDao orderReviewDao;

	@Override
	@Transactional
	public List<Order> getWaitAssignOrders() throws Exception {
		List<Order> orders = orderDao.getWaitAssignmentOrders();
		if (orders == null || orders.size() == 0) {
			throw new Exception("No orders wait for assignment!");
		}
		
		return orders;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void assignOrder(Order order) throws Exception {
		logDebug("Start to process order " + order.getOrder_id());
		//
		if (order.getStatus() != Order.WAIT_ASSIGNMENT) {
			throw new Exception(String.format("Order[%s] status is %s. Cannot to be assignmented!",
					order.getOrder_id(), order.getStatus()));
		}
		
		//取出订单数量
		int quanity = order.getProduct_quantity();
		logDebug("Product quanity is" + quanity);
		
//		for(int i = 0; i < quanity; i ++) {
//			//新建一个OrderForReview
//			OrderForReview orderFR = new OrderForReview();
//			orderFR.setOrder(order);
//			orderFR.setCreate_date(Calendar.getInstance());
//			orderFR.setStatus(1);
//			
//			orderReviewDao.newOrderReview(orderFR);
//		}
		
		//更新订单状态为“in_review”
		order.setStatus(Order.IN_REVIEW);
		orderDao.updateOrder(order);
		
		logInfo(String.format("Assignment order[%s] success!", order.getOrder_id()));
	}
	
	public void logDebug(String info) {
		OrderSync.log.debug("AssignOrder---" + info);
	}
	
	public void logInfo(String info) {
		OrderSync.log.info("AssignOrder---" + info);
	}

}

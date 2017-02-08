package com.web.order.service;

import java.util.List;

import com.web.entity.Order;

public interface AssignOrderService {
	List<Order> getWaitAssignOrders() throws Exception;
	void assignOrder(Order order) throws Exception;
}

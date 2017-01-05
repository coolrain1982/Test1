package com.web.order.service;

import java.util.List;

import com.web.entity.Order;

public interface OrderTaskService {
	
	List<Order> getAssignmentOrders() throws Exception;
	
	void assignmentOrder(Order order) throws Exception;
}

package com.web.order.service;

import java.util.List;

import com.web.entity.Order;

public interface FinishOrderService {

	public List<Order> getFinishOrder() throws Exception;
	public void setOrderFinish(Order order) throws Exception;
}

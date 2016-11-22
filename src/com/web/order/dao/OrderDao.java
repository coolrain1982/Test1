package com.web.order.dao;

import com.web.entity.Order;

public interface OrderDao {
	public void newOrder(Order order);
	public void updateOrder(Order order);
}

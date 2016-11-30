package com.web.order.dao;

import java.util.List;

import com.web.entity.Order;

public interface OrderDao {
	public void newOrder(Order order);
	public void updateOrder(Order order);
	public long getOrderCount(int userId, int status);
	public long getOrderCountForCS(int csId, int status);
	public long getOrderCount(int userId);
	public long getOrderCountForCS(int csId);
	public List<Order> getOrders(int userId, int status, int startIdx, int size);
	public List<Order> getOrdersForCS(int csId, int status, int startIdx, int size);
	public List<Order> getOrders(int userId, int startIdx, int size);
	public List<Order> getOrdersForCS(int csId, int startIdx, int size);
}

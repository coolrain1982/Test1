package com.web.order.dao;

import java.util.List;

import com.web.entity.Order;

public interface OrderDao {
	public void newOrder(Order order);
	public void updateOrder(Order order);
	
	public long getOrderCount(int userId, int status);
	public long getOrderCountForCS(int csId, int status);
	public long getOrderCount();
	public long getOrderCount(int userId);
	public long getOrderCountForCS(int csId);
	public long getProcessOrderCount(Integer userId);
	public long getProcessOrderCount();
	public long getDoingOrderCount(Integer userId);
	public long getDoingOrderCount();
	
	public Order getOrderById(long orderId);
	public Order getOrder(Integer userId, long orderId);
	
	public List<Order> getOrders(int startIdx, int size);
	public List<Order> getOrders(int userId, int status, int startIdx, int size);
	public List<Order> getOrdersForCS(int csId, int status, int startIdx, int size);
	public List<Order> getOrders(int userId, int startIdx, int size);
	public List<Order> getOrdersForCS(int csId, int startIdx, int size);
	public List<Order> getProcessOrders(int startIdx, int size);
	public List<Order> getProcessOrders(Integer userId, int startIdx, int size);
	public List<Order> getDoingOrders(int startIdx, int size);
	public List<Order> getDoingOrders(Integer userId, int startIdx, int size);

}

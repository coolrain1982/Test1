package com.web.order.service;

import java.util.List;

import com.web.common.PageForQuery;
import com.web.entity.Order;

public interface GetOrderService {
	public long getOrderCount(Integer userId, int status) throws Exception;
	public long getOrderCount(int status) throws Exception;
	public List<Order> getOrders(Integer userId, int status, PageForQuery pfq) throws Exception;
	public List<Order> getOrders(int status, PageForQuery pfq);
	public long getProcessOrderCount(Integer userId) throws Exception;
	public List<Order> getProcessOrders(Integer userId, PageForQuery pageForQuery) throws Exception;
	public long getDoingOrderCount(Integer userId) throws Exception;
	public List<Order> getDoingOrders(Integer userId, PageForQuery pageForQuery) throws Exception;
	public long getOrderCount() throws Exception;
	public List<Order> getOrders(PageForQuery pfq) throws Exception;
	public long getProcessOrderCount() throws Exception;
	public List<Order> getProcessOrders(PageForQuery pfq) throws Exception;
	public long getDoingOrderCount() throws Exception;
	public List<Order> getDoingOrders(PageForQuery pfq) throws Exception;
}

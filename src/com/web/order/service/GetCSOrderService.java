package com.web.order.service;

import java.util.List;

import com.web.common.PageForQuery;
import com.web.entity.Order;

public interface GetCSOrderService {
	public long getOrderCount(Integer userId, int status) throws Exception;
	public long getOrderCount(int status) throws Exception;
	public List<Order> getOrders(Integer userId, int status, PageForQuery pfq) throws Exception;
	public List<Order> getOrders(int status, PageForQuery pfq);
}

package com.web.order.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.common.PageForQuery;
import com.web.entity.Order;
import com.web.order.dao.OrderDao;

@Service
public class GetOrderServiceImpl implements GetOrderService {

	@Resource
	public OrderDao orderDao;

	@Transactional
	@Override
	public long getOrderCount(Integer userId, int status) throws Exception {
		if (status == 99) {
			return orderDao.getOrderCount(userId);
		}
		return orderDao.getOrderCount(userId, status);
	}

	@Override
	@Transactional
	public long getOrderCount(int status) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public List<Order> getOrders(Integer userId, int status, PageForQuery pfq) throws Exception {
		int startIdx = (pfq.getPage() - 1) * pfq.getSize();
		if (status == 99) {
			return orderDao.getOrders(userId, startIdx, pfq.getSize());
		}
		return orderDao.getOrders(userId, status, startIdx, pfq.getSize());
	}

	@Override
	@Transactional
	public List<Order> getOrders(int status, PageForQuery pfq) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Order getPayment(Integer userId, long orderId) throws Exception {
		Order order = null;
		// 获取order
		order = orderDao.getOrder(userId, orderId);
		if (order == null) {
			throw new Exception(String.format("找不到订单[编号%s]信息!", Order.getFormatOrderID(orderId, 8)));
		}
		
		switch (order.getStatus()) {
		case Order.PAYED :
			throw new Exception(String.format("订单[编号%s]已支付，请等待我们的确认反馈!", Order.getFormatOrderID(orderId, 8)));
		case Order.WAIT_PAY:
		case Order.PAYED_FAIL:
			return order;
		case Order.PAYED_SUCCESS:
		case Order.FINISH:
			throw new Exception(String.format("订单[编号%s]已支付成功，不能再次支付!", Order.getFormatOrderID(orderId, 8)));
		default:
			throw new Exception(String.format("订单[编号%s]无法支付, 请刷新页面后重试!", Order.getFormatOrderID(orderId, 8)));
		}
	}

}

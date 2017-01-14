package com.web.order.service.implement;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.common.PageForQuery;
import com.web.entity.Order;
import com.web.order.dao.OrderDao;
import com.web.order.service.GetCSOrderService;

@Service
public class GetCSOrderServiceImpl implements GetCSOrderService {
	
	@Resource
	public OrderDao orderDao;

	@Transactional
	@Override
	public long getOrderCount(Integer userId, int status) throws Exception {
		if (status == 99) {
			return orderDao.getOrderCountForCS(userId);
		}
		return orderDao.getOrderCountForCS(userId, status);
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
			return orderDao.getOrdersForCS(userId, startIdx, pfq.getSize());
		}
		return orderDao.getOrdersForCS(userId, status, startIdx, pfq.getSize());
	}

	@Override
	@Transactional
	public List<Order> getOrders(int status, PageForQuery pfq) {
		// TODO Auto-generated method stub
		return null;
	}
}

package com.web.order.service.implement;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.common.PageForQuery;
import com.web.entity.Order;
import com.web.order.dao.OrderDao;
import com.web.order.service.GetOrderService;

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
	public List<Order> getOrders(int status, PageForQuery pfq) {
		// TODO Auto-generated method stub
		return null;
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
	public long getProcessOrderCount(Integer userId) throws Exception {
		return orderDao.getProcessOrderCount(userId);
	}

	@Override
	@Transactional
	public List<Order> getProcessOrders(Integer userId, PageForQuery pfq) throws Exception {
		int startIdx = (pfq.getPage() - 1) * pfq.getSize();
		return orderDao.getProcessOrders(userId, startIdx, pfq.getSize());
	}

	@Override
	@Transactional
	public long getDoingOrderCount(Integer userId) throws Exception {
		return orderDao.getDoingOrderCount(userId);
	}

	@Override
	@Transactional
	public List<Order> getDoingOrders(Integer userId, PageForQuery pfq) throws Exception {
		int startIdx = (pfq.getPage() - 1) * pfq.getSize();
		return orderDao.getDoingOrders(userId, startIdx, pfq.getSize());
	}
	
	@Transactional
	@Override
	public long getOrderCount() throws Exception {
		return orderDao.getOrderCount();
	}
	
	@Override
	@Transactional
	public List<Order> getOrders(PageForQuery pfq) throws Exception {
		int startIdx = (pfq.getPage() - 1) * pfq.getSize();
		return orderDao.getOrders(startIdx, pfq.getSize());
	}


	@Override
	@Transactional
	public long getProcessOrderCount() throws Exception {
		return orderDao.getProcessOrderCount();
	}

	@Override
	@Transactional
	public List<Order> getProcessOrders(PageForQuery pfq) throws Exception {
		int startIdx = (pfq.getPage() - 1) * pfq.getSize();
		return orderDao.getProcessOrders(startIdx, pfq.getSize());
	}

	@Override
	@Transactional
	public long getDoingOrderCount() throws Exception {
		return orderDao.getDoingOrderCount();
	}

	@Override
	@Transactional
	public List<Order> getDoingOrders(PageForQuery pfq) throws Exception {
		int startIdx = (pfq.getPage() - 1) * pfq.getSize();
		return orderDao.getDoingOrders(startIdx, pfq.getSize());
	}

}

package com.web.order.service.implement;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.entity.Order;
import com.web.entity.User;
import com.web.order.dao.OrderDao;
import com.web.order.service.CheckUserForOrder;

@Service
public class CheckUserForOrderImpl implements CheckUserForOrder {

	@Resource
	public OrderDao orderDao;
	
	@Override
	@Transactional
	public boolean canOperateOrder(User user, long orderid) throws Exception {
		
		if (user.getRole().equalsIgnoreCase("role_admin")) {
			return true;
		}
		
		Order order = null;
		try {
			order = orderDao.getOrderById(orderid);
		} catch (Exception e) {
			throw new Exception("找不到订单ID为[" + orderid + "]的订单");
		}
		
		if (order == null) {
			throw new Exception("找不到订单ID为[" + orderid + "]的订单");
		}
		
		return order.getUser().getId() == user.getId()?true:false;
	}

}

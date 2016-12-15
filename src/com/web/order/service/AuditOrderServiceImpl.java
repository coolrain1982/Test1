package com.web.order.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.entity.Order;
import com.web.entity.User;
import com.web.order.dao.OrderDao;

@Service
public class AuditOrderServiceImpl implements AuditOrderService {

	@Resource
	public OrderDao orderDao;

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void auditOrder(User user, int status, long orderId, String auditRemark) throws Exception {
		
		//先用orderid取出order
		Order order = orderDao.getOrderById(orderId);
		if (order == null) {
			throw new Exception(String.format("未找到单号为[%s]的订单！", orderId));
		}
		
		//检查操作员是否有审核改订单的权限
		if (user.getRole().equalsIgnoreCase("role_cs") && 
			order.getCsid() != user.getId()) {
			throw new Exception(String.format("你没有订单[%s]的审核权限！", orderId));
		}
		
		//检查订单状态
		if (order.getStatus() != Order.INIT) {
			throw new Exception(String.format("订单[%s]未处于[待审核]状态！", orderId));
		}
		
		Calendar c = Calendar.getInstance();
		auditRemark = String.format("[%s by %s]\n%s",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
				   format(c.getTime()), user.getName(), auditRemark);
		
		//开始审核订单
		order.setStatus(status);
		order.setAudit_date(c);
		order.setAudit_remark(auditRemark);
		
		orderDao.updateOrder(order);
	}

}

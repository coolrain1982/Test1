package com.web.order.service.implement;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scheduletask.OrderSync;
import com.web.entity.Order;
import com.web.order.dao.OrderDao;
import com.web.order.service.FinishOrderService;

@Service
public class FinishOrderServiceImpl implements FinishOrderService {

	@Resource
	public OrderDao orderDao;

	@Override
	@Transactional
	public List<Order> getFinishOrder() throws Exception {
		return orderDao.getOrderFinish();
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void setOrderFinish(Order order) throws Exception {
		logDebug("Start to finish order " + order.getOrder_id());
		
		order.setStatus(Order.FINISH);
		orderDao.updateOrder(order);
		
		logInfo(String.format("Finish order[%s] success!", order.getOrder_id()));
		
	}
	
	private void logInfo(String info) {
		OrderSync.log.info("FinishOrder---" + info);
	}
	
	private void logDebug(String info) {
		OrderSync.log.debug("FinishOrder---" + info);
	}

}

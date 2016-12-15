package com.web.order.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.entity.Order;
import com.web.entity.PayInfo;
import com.web.entity.User;
import com.web.order.dao.OrderDao;
import com.web.order.dao.PayInfoDao;

@Service
public class PayOrderServiceImpl implements PayOrderService {

	@Resource
	public OrderDao orderDao;

	@Resource
	public PayInfoDao payDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void payOrder(User user, long orderId, PayInfo payInfo) throws Exception {
		// 先用orderid取出order
		Order order = orderDao.getOrderById(orderId);
		if (order == null) {
			throw new Exception(String.format("未找到单号为[%s]的订单！", orderId));
		}

		// 检查是否为该用户的订单
		if (!user.getRole().equalsIgnoreCase("role_admin")) {
			if (order.getUser().getId() != user.getId()) {
				throw new Exception(String.format("未找到您的订单[%s]！", orderId));
			}
		}

		// 检查订单状态
		if (order.getStatus() != Order.WAIT_PAY && order.getStatus() != Order.PAYED_FAIL) {
			throw new Exception(String.format("订单[%s]不可支付！", orderId));
		}

		// 保存payInfo信息
		payInfo.setOrder(order);
		payInfo.setPay_date(Calendar.getInstance());
		payInfo.setPay_type(1);
		payInfo.setStatus(PayInfo.INIT);

		payDao.newPayInfo(payInfo);

		// 更新order的状态
		order.setStatus(Order.PAYED);
		orderDao.updateOrder(order);
	}

	@Override
	@Transactional
	public List<PayInfo> getPayInfo(User user, long orderId) throws Exception {
		// 先用orderid取出order
		Order order = orderDao.getOrderById(orderId);
		if (order == null) {
			throw new Exception(String.format("未找到单号为[%s]的订单！", orderId));
		}

		// 检查是否为该用户的订单
		if (!user.getRole().equalsIgnoreCase("role_admin") && 
			!user.getRole().equalsIgnoreCase("role_cs")	) {
			if (order.getUser().getId() != user.getId()) {
				throw new Exception(String.format("未找到您的订单[%s]！", orderId));
			}
		}
		
		return payDao.getPayInfo(orderId);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void auditOrderPay(User user, long orderId, int result, int payResult, String auditRemark) throws Exception {
		
		// 先用orderid取出order
		Order order = orderDao.getOrderById(orderId);
		if (order == null) {
			throw new Exception(String.format("未找到单号为[%s]的订单！", orderId));
		}

		// 检查是否为该用户的订单
		if (!user.getRole().equalsIgnoreCase("role_admin")) {
			if (order.getCsid() != user.getId()) {
				throw new Exception(String.format("你无权操作订单[%s]！", orderId));
			}
		}

		// 检查订单状态
		if (order.getStatus() != Order.PAYED) {
			throw new Exception(String.format("订单[%s]不可审核支付！", orderId));
		}

		//取order的payinfo
		Set<PayInfo> payInfos = order.getOrderPay();
		for(Iterator<PayInfo> it = payInfos.iterator(); it.hasNext();) {
			PayInfo payinfo = it.next();
			payinfo.setAuditor(user.getName());
			payinfo.setAudit_date(Calendar.getInstance());
			payinfo.setAuditRemark(String.format("%s[%s by %s]\n%s",
					payinfo.getAuditRemark()==null?"":(payinfo.getAuditRemark()+"\n"), 
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreate_date().getTime()),
							user.getName(),
							auditRemark));
			payinfo.setStatus(payResult);
			
			payDao.updatePayInfo(payinfo);
		}

		// 更新order的状态
		order.setStatus(result);
		orderDao.updateOrder(order);
	}

}

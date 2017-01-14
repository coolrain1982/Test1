package com.web.refund.service;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.web.entity.Order;
import com.web.entity.RefundInfo;
import com.web.entity.User;
import com.web.order.dao.OrderDao;
import com.web.refund.dao.RefundInfoDao;

@Service
public class OrderRefundServiceImpl implements OrderRefundService {
	
	@Resource
	public OrderDao orderDao; 
	
	@Resource
	public RefundInfoDao refundDao;

	@Override
	@Transactional(rollbackFor=Exception.class)
	public RefundInfo addRefundInfo(User user, int refundType, MultiValueMap<String, Object> params) throws Exception {
		double money;
		long orderid;
		String sn, payee, remark;
		
		if (params.containsKey("orderid") && params.get("orderid").size() > 0) {
			try {
				orderid = Long.parseLong(params.get("orderid").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("退款订单ID错误！");
			}
		} else {
			throw new Exception("无退款订单ID");
		}
		
		if (params.containsKey("money") && params.get("money").size() > 0) {
			try {
				money = Double.parseDouble(params.get("money").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("退款金额错误:" + params.get("money").get(0).toString().trim());
			}
		} else {
			throw new Exception("无退款金额");
		}
		
		if (params.containsKey("sn") && params.get("sn").size() > 0 && 
				!params.get("sn").get(0).toString().trim().equals("")) {
			sn = params.get("sn").get(0).toString().trim();
			if (sn.length() > 8) {
				throw new Exception("流水号不能超过8位！");
			}
		} else {
			throw new Exception("流水号输入不正确！");
		}
		
		if (params.containsKey("payee") && params.get("payee").size() > 0 && 
				!params.get("payee").get(0).toString().trim().equals("")) {
			payee = params.get("payee").get(0).toString().trim();
			if (payee.length() > 100) {
				throw new Exception("退款账户不能超过100个字符！");
			}
		} else {
			throw new Exception("退款账户输入不正确！");
		}
		
		if (params.containsKey("remark") && params.get("remark").size() > 0 && 
				!params.get("remark").get(0).toString().trim().equals("")) {
			remark = params.get("remark").get(0).toString().trim();
			if (remark.length() > 500) {
				throw new Exception("备注不能超过500个字符！");
			}
		} else {
			throw new Exception("请输入备注！");
		}
		
		//根据ID取订单信息
		Order order = null;
		try {
			order = orderDao.getOrderById(orderid);
		} catch (Exception e) {
			throw new Exception("取退款订单信息失败:" + e.getMessage());
		}
		
		if (order == null) {
			throw new Exception("退款订单不存在！");
		}
		
		if (order.getStatus() < 5) {
				throw new Exception("该订单不可进行退款操作！");
			}
		
		//开始新增退款
		RefundInfo refundInfo = new RefundInfo();
		refundInfo.setMoney(money);
		refundInfo.setOrder(order);
		refundInfo.setPay_type(1);
		refundInfo.setPayee(payee);
		refundInfo.setRefund_date(Calendar.getInstance());
		refundInfo.setRefund_type(refundType);
		refundInfo.setRemark(remark);
		refundInfo.setSn(sn);
		refundInfo.setStatus(1);
		refundInfo.setUser(user);
		
		refundDao.addRefund(refundInfo);
		
		return refundInfo;
	}

	@Override
	@Transactional
	public List<RefundInfo> getRefundInfoByOrder(long orderid) throws Exception {
		return refundDao.getRefundInfoByOrderid(orderid);
	}
	
	@Override
	@Transactional
	public List<Order> getOrderBySearchID(long orderid) throws Exception {
		return orderDao.getOrdersForSearchByID(orderid);
	}

	@Override
	@Transactional
	public long getOrderCountBySearchID(long orderid) throws Exception {
		return orderDao.getOrderCountForSearchByID(orderid);
	}

}

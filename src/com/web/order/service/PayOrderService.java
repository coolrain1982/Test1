package com.web.order.service;

import java.util.List;

import com.web.entity.PayInfo;
import com.web.entity.User;

public interface PayOrderService {
	
	public void payOrder(User user, long orderId, PayInfo payInfo) throws Exception;

	public List<PayInfo> getPayInfo(User user, long orderId) throws Exception;
}

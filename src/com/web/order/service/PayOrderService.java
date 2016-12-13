package com.web.order.service;

import com.web.entity.PayInfo;
import com.web.entity.User;

public interface PayOrderService {
	public void payOrder(User user, long orderId, PayInfo payInfo) throws Exception;
}

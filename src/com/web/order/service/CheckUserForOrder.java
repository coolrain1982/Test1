package com.web.order.service;

import com.web.entity.User;

public interface CheckUserForOrder {
	public boolean canOperateOrder(User user, long orderid) throws Exception;
}

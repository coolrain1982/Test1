package com.web.order.service;

import com.web.entity.User;

public interface AuditOrderService {
	public void auditOrder(User user, int status, long orderId, String auditRemark) throws Exception;
}

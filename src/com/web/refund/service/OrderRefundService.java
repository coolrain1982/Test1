package com.web.refund.service;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.web.entity.Order;
import com.web.entity.RefundInfo;
import com.web.entity.User;

public interface OrderRefundService {
	public RefundInfo addRefundInfo(User user, int refundType, MultiValueMap<String, Object> reqParams) throws Exception;
	public List<RefundInfo> getRefundInfoByOrder(long orderid) throws Exception;
	public List<Order> getOrderBySearchID(long orderid) throws Exception;
	public long getOrderCountBySearchID(long orderid) throws Exception;
}

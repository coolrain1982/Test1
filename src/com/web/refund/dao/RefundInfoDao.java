package com.web.refund.dao;

import java.util.List;

import com.web.entity.RefundInfo;

public interface RefundInfoDao {
	List<RefundInfo> getRefundInfoByOrderid(long orderid);
	long getRefundInfoCountByOrderid(long orderid);
	void addRefund(RefundInfo refundinfo);
}

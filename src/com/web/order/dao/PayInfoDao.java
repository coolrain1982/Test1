package com.web.order.dao;

import java.util.List;

import com.web.entity.PayInfo;

public interface PayInfoDao {
	public void newPayInfo(PayInfo payInfo);
	public void updatePayInfo(PayInfo payInfo);
	public List<PayInfo> getPayInfo(long orderId);
}

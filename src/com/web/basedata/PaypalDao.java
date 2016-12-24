package com.web.basedata;

import java.util.Calendar;
import java.util.List;

import com.web.entity.PaypalFee;

public interface PaypalDao {
	public PaypalFee getPaypal(int type);
	public PaypalFee getPaypal(Calendar date);
	public void addPaypal(PaypalFee paypalFee);
	public long getCount();
	public List<PaypalFee> getPaypal(int startIdx, int size);
}

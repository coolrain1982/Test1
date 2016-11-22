package com.web.basedata;

import java.util.Calendar;

import com.web.entity.PaypalFee;

public interface PaypalDao {
	public PaypalFee getPaypal(int type);
	public PaypalFee getPaypal(Calendar date);
	public void addPaypal(PaypalFee paypalFee);
}

package com.web.basedata;

import java.util.Calendar;

public interface ExchangeDao {
	public Double getExchange(int type);
	public Double getExchange(Calendar date);
	public void addExchange(int type, double rate);
}

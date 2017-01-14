package com.web.basedata.dao;

import java.util.Calendar;
import java.util.List;

import com.web.entity.ExchangeRate;

public interface ExchangeDao {
	public Double getExchange(int type);
	public Double getExchange(Calendar date);
	public void addExchange(ExchangeRate exchange);
	public long getCount();
	public List<ExchangeRate> getExchange(int startIdx, int size);
}

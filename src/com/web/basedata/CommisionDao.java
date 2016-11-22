package com.web.basedata;

import java.util.Calendar;

public interface CommisionDao {
	public Double getCommision(int type);
	public Double getCommision(Calendar date);
	public void addCommision(int type, double rate);
}

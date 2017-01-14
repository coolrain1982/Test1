package com.web.basedata.dao;

import java.util.Calendar;
import java.util.List;
import com.web.entity.Commision;

public interface CommisionDao {
	public List<Commision> getCommision(int type);
	public List<Commision> getCommision(Calendar date);
	public void addCommision(Commision commision);
	public long getCount();
	public List<Commision> getCommision(int startIdx, int size);
}

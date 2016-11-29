package com.web.basedata;

import java.util.Calendar;
import java.util.List;
import com.web.entity.Commision;

public interface CommisionDao {
	public List<Commision> getCommision(int type);
	public List<Commision> getCommision(Calendar date);
	public void addCommision(int type, List<Commision> commision);
}

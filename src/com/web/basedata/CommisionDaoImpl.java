package com.web.basedata;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.Commision;

@Repository
public class CommisionDaoImpl implements CommisionDao {

	@Resource
	public SessionFactory sesssionFactory;

	@Override
	public Double getCommision(int type) {
		String hql = "select new com.web.entity.Commision(e.type, e.fee, e.date) from Commision e where e.type = :type order by e.date desc";
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("type", type);
		
		List<Commision> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList.get(0).getFee();
	}

	@Override
	public Double getCommision(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCommision(int type, double rate) {
		// TODO Auto-generated method stub

	}

}

package com.web.basedata;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.web.entity.ExchangeRate;

@Repository
public class ExchangeDaoImpl implements ExchangeDao {

	@Resource
	public SessionFactory sesssionFactory;

	@Override
	public Double getExchange(int type) {
		String hql = "select new com.web.entity.ExchangeRate(e.type, e.rate) from ExchangeRate e where e.type = :type order by e.date desc";
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("type", type);
		
		List<ExchangeRate> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList.get(0).getRate();
	}

	@Override
	public Double getExchange(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addExchange(int type, double rate) {
		// TODO Auto-generated method stub

	}

}

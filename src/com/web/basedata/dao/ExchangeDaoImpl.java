package com.web.basedata.dao;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.ExchangeRate;

@Repository
public class ExchangeDaoImpl implements ExchangeDao {

	@Resource
	public SessionFactory sesssionFactory;

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	public void addExchange(ExchangeRate ex) {
		sesssionFactory.getCurrentSession().save(ex);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public long getCount() {
		String hql = String.format(
				"select count(e.id) from ExchangeRate e");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<ExchangeRate> getExchange(int startIdx, int size) {
		String hql = String.format("select new com.web.entity.ExchangeRate(e.type, e.rate,e.date) from ExchangeRate e order by e.date desc");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}

}

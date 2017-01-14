package com.web.basedata.dao;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.PaypalFee;

@Repository
public class PaypalDaoImpl implements PaypalDao {

	@Resource
	public SessionFactory sesssionFactory;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PaypalFee getPaypal(int type) {
		String hql = "select new com.web.entity.PaypalFee(p.type, p.fee, p.fee_rate) from PaypalFee p where p.type = :type order by p.date desc";
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("type", type);
		
		List<PaypalFee> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList.get(0);
	}

	@Override
	public PaypalFee getPaypal(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPaypal(PaypalFee paypalFee) {
		sesssionFactory.getCurrentSession().save(paypalFee);
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public long getCount() {
		String hql = String.format(
				"select count(p.id) from PaypalFee p");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<PaypalFee> getPaypal(int startIdx, int size) {
		String hql = String.format("select new com.web.entity.PaypalFee(p.type, p.fee, p.fee_rate, p.date) from PaypalFee p order by p.date desc");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}
}

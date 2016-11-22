package com.web.basedata;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.Commision;
import com.web.entity.PaypalFee;

@Repository
public class PaypalDaoImpl implements PaypalDao {

	@Resource
	public SessionFactory sesssionFactory;

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
		// TODO Auto-generated method stub
		
	}
}

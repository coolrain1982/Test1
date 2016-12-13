package com.web.order.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import com.web.entity.PayInfo;

@Repository
public class PayInfoDaoImpl implements PayInfoDao {

	@Resource
	public SessionFactory sesssionFactory;
	
	@Override
	public void newPayInfo(PayInfo payInfo) {
		sesssionFactory.getCurrentSession().save(payInfo);
	}

	@Override
	public void updatePayInfo(PayInfo payInfo) {
		sesssionFactory.getCurrentSession().update(payInfo);		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<PayInfo> getPayInfo(long orderId) {
		String hql = String.format("from PayInfo p join p.order o where o.order_id = :orderId");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("orderId", orderId);

		return q.getResultList();
	}

}

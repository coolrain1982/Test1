package com.web.refund.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.RefundInfo;
import com.web.order.ctrl.RefundCtrl;

@Repository
public class RefundInfoDaoImpl implements RefundInfoDao {

	@Resource
	public SessionFactory sesssionFactory;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<RefundInfo> getRefundInfoByOrderid(long orderid) {
		String hql = String.format(
"select new com.web.entity.RefundInfo(%s) from RefundInfo r join r.order o join r.user u where o.order_id =:orderid order by r.refund_date desc",
				RefundCtrl.getRefundColStr());

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("orderid", orderid);

		return q.getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public long getRefundInfoCountByOrderid(long orderid) {
		String hql = String.format("select count(o.id) from RefundInfo r where r.order_id = :orderid");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("orderid", orderid);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@Override
	public void addRefund(RefundInfo refundinfo) {
		sesssionFactory.getCurrentSession().save(refundinfo);
	}

}

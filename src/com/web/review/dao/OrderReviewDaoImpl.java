package com.web.review.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.OrderForReview;
import com.web.review.ctrl.ReviewInputCtrl;

@Repository
public class OrderReviewDaoImpl implements OrderReviewDao {
	
	@Resource
	public SessionFactory sesssionFactory;

	@Override
	public void newOrderReview(OrderForReview orderReview) {
		sesssionFactory.getCurrentSession().save(orderReview);
	}

	@Override
	public void updateOrderReview(OrderForReview orderReview) {
		sesssionFactory.getCurrentSession().update(orderReview);	
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<OrderForReview> getReviews(long orderid) {
			String hql = String.format(
	"select new com.web.entity.OrderForReview(%s) from OrderForReview r join r.order o where o.order_id =:orderid order by r.create_date desc",
					ReviewInputCtrl.getReviewColStr());

			Query q = sesssionFactory.getCurrentSession().createQuery(hql);
			q.setParameter("orderid", orderid);

			return q.getResultList();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public OrderForReview getReviewById(long reviewid) {

		String hql = "from OrderForReview r where r.id=:reviewid";

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("reviewid", reviewid);
		List<OrderForReview> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		return resultList.get(0);
	}

}

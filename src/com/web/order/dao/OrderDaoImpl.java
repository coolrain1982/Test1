package com.web.order.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.Order;

@Repository
public class OrderDaoImpl implements OrderDao {
	@Resource
	public SessionFactory sesssionFactory;

	@Override
	public void newOrder(Order order) {
		sesssionFactory.getCurrentSession().save(order);
	}

	@Override
	public void updateOrder(Order order) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("rawtypes")
	@Override
	public long getOrderCount(int userId, int status) {
		String hql = String.format(
				"select count(o.order_id) from Order o join o.user u where o.status=:status and u.id = :userId ");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("status", status);
		q.setParameter("userId", userId);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public long getOrderCountForCS(int csId, int status) {
		String hql = String.format(
				"select count(o.order_id) from Order o where o.status=:status and o.csid = :csId ");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("status", status);
		q.setParameter("csId", csId);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public long getOrderCount(int userId) {
		String hql = String.format(
				"select count(o.order_id) from Order o join o.user u where u.id = :userId ");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("userId", userId);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public long getOrderCountForCS(int csId) {
		String hql = String.format(
				"select count(o.order_id) from Order o where o.csid = :csId ");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("csId", csId);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Order> getOrders(int userId, int status, int startIdx, int size) {
		String hql = String.format(
				"select new com.web.entity.Order(%s,%s,%s) from Order o join o.user u where o.status=:status and u.id = :userId ", 
				"o.order_id, o.discount,o.product_descript,o.link,o.product_photo_url,o.audit_remark,o.product_unit_price",
				"o.product_unit_freight,o.product_unit_commission,o.exchange_rate,o.paypal_fee,o.paypal_rate",
				"o.product_quantity,o.create_date,o.status, o.type");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("status", status);
		q.setParameter("userId", userId);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Order> getOrdersForCS(int csId, int status, int startIdx, int size) {
		String hql = String.format(
				"select new com.web.entity.Order(%s,%s,%s) from Order o where o.status=:status and o.csid = :csId ", 
				"o.order_id, o.discount,o.product_descript,o.link,o.product_photo_url,o.audit_remark,o.product_unit_price",
				"o.product_unit_freight,o.product_unit_commission,o.exchange_rate,o.paypal_fee,o.paypal_rate",
				"o.product_quantity,o.create_date,o.status, o.type");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("status", status);
		q.setParameter("csId", csId);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Order> getOrders(int userId,  int startIdx, int size) {
		String hql = String.format(
				"select new com.web.entity.Order(%s,%s,%s) from Order o join o.user u where u.id = :userId ", 
				"o.order_id, o.discount,o.product_descript,o.link,o.product_photo_url,o.audit_remark,o.product_unit_price",
				"o.product_unit_freight,o.product_unit_commission,o.exchange_rate,o.paypal_fee,o.paypal_rate",
				"o.product_quantity,o.create_date,o.status, o.type");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("userId", userId);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Order> getOrdersForCS(int csId,  int startIdx, int size) {
		String hql = String.format(
				"select new com.web.entity.Order(%s,%s,%s) from Order o where o.csid = :csId ", 
				"o.order_id, o.discount,o.product_descript,o.link,o.product_photo_url,o.audit_remark,o.product_unit_price",
				"o.product_unit_freight,o.product_unit_commission,o.exchange_rate,o.paypal_fee,o.paypal_rate",
				"o.product_quantity,o.create_date,o.status, o.type");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("csId", csId);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}

}

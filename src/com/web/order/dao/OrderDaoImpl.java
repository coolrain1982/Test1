package com.web.order.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.Order;
import com.web.entity.PayInfo;

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
		sesssionFactory.getCurrentSession().update(order);
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
	
	@SuppressWarnings("rawtypes")
	@Override
	public long getProcessOrderCount(Integer userId) {
		String hql = String.format(
				"select count(o.order_id) from Order o join o.user u where o.status in (:status) and u.id = :userId");
		
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("userId", userId);
		q.setParameterList("status", Order.getProcessOrderStatus());

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public long getDoingOrderCount(Integer userId) {
		String hql = String.format(
				"select count(o.order_id) from Order o join o.user u where o.status in (:status) and u.id = :userId");
		
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("userId", userId);
		q.setParameterList("status", Order.getDoingOrderStatus());

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public long getOrderCount() {
		String hql = String.format(
				"select count(o.order_id) from Order o");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public long getProcessOrderCount() {
		String hql = String.format(
				"select count(o.order_id) from Order o where o.status in (:status)");
		
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameterList("status", Order.getAdminProcessOrderStatus());

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public long getDoingOrderCount() {
		String hql = String.format(
				"select count(o.order_id) from Order o where o.status in (:status)");
		
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameterList("status", Order.getAdminDoingOrderStatus());

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
            "select new com.web.entity.Order(%s) from Order o join o.user u where o.status=:status and u.id = :userId order by o.create_date desc", 
            getOrderColumnStr());

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
				"select new com.web.entity.Order(%s) from Order o where o.status=:status and o.csid = :csId ",
				getOrderColumnStr());

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
				"select new com.web.entity.Order(%s) from Order o join o.user u where u.id = :userId order by o.create_date desc", 
				getOrderColumnStr());

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
				"select new com.web.entity.Order(%s) from Order o where o.csid = :csId order by o.create_date desc", 
				getOrderColumnStr());

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("csId", csId);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Order getOrderById(long orderId) {
		String hql = "from Order o where o.order_id = :orderId";

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("orderId", orderId);

		List<Order> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		return resultList.get(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Order getOrder(Integer userId, long orderId) {
		String hql = String.format(
				"select new com.web.entity.Order(%s) from Order o join o.user u where u.id = :userId and o.order_id = :orderId", 
				getOrderColumnStr());

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("userId", userId);
		q.setParameter("orderId", orderId);
		
		List<Order> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		return resultList.get(0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Order> getProcessOrders(Integer userId, int startIdx, int size) {
		String hql = String.format(
				"select new com.web.entity.Order(%s) from Order o join o.user u where o.status in (:status) and u.id = :userId order by o.create_date desc", 
				getOrderColumnStr());

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameterList("status", Order.getProcessOrderStatus());
		q.setParameter("userId", userId);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Order> getDoingOrders(Integer userId, int startIdx, int size) {
		String hql = String.format(
				"select new com.web.entity.Order(%s) from Order o join o.user u where o.status in (:status) and u.id = :userId order by o.create_date desc", 
				getOrderColumnStr());

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameterList("status", Order.getDoingOrderStatus());
		q.setParameter("userId", userId);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Order> getOrders(int startIdx, int size) {
		String hql = String.format(
				"select new com.web.entity.Order(%s) from Order o join o.user u  order by o.create_date desc", 
				getOrderColumnStr());

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Order> getProcessOrders(int startIdx, int size) {
		String hql = String.format(
				"select new com.web.entity.Order(%s) from Order o join o.user u where o.status in (:status) order by o.create_date desc", 
				getOrderColumnStr());

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameterList("status", Order.getAdminProcessOrderStatus());
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Order> getDoingOrders(int startIdx, int size) {
		String hql = String.format(
				"select new com.web.entity.Order(%s) from Order o join o.user u where o.status in (:status) order by o.create_date desc", 
				getOrderColumnStr());

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameterList("status", Order.getAdminDoingOrderStatus());
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}
	
	private String getOrderColumnStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("o.order_id, o.discount,o.product_descript,o.link, o.product_asin,o.product_photo_url,o.audit_remark,o.product_unit_price,");
		sb.append("o.product_unit_freight,o.product_unit_commission,o.exchange_rate,o.paypal_fee,o.paypal_rate,");
		sb.append("o.product_quantity,o.create_date,o.status, o.type, o.audit_date, u.name, o.find_product_mode, ");
		sb.append("o.search_page_idx,o.shop_name,o.key_word ");
		return sb.toString();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public long getOrderCountForSearchByID(long orderid) {
		String hql = String.format(
				"select count(o.order_id) from Order o where o.order_id=:orderid");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("orderid", orderid);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Order> getOrdersForSearchByID(long orderid) {
		String hql = String.format(
				"select new com.web.entity.Order(%s) from Order o join o.user u where o.order_id =:orderid", 
				getOrderColumnStr());

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("orderid", orderid);

		return q.getResultList();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Order> getWaitAssignmentOrders() {
		String hql = String.format(
				"from Order o where o.status = :status");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("status", Order.PAYED_SUCCESS);

		return q.getResultList();
	}

}

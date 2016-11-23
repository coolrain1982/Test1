package com.web.order.dao;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
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

}

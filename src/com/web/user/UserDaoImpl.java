package com.web.user;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.web.entity.User;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Resource
	public SessionFactory sesssionFactory;

	@Override
	@Transactional
	public void addUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User getUser(String userName) {
		String hql = "from User u where u.name = :name";
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("name", userName);
		
		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		
		return (User) resultList.get(0);
	}

	@Override
	public User getUser(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

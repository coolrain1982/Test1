package com.web.user;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.User;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Resource
	public SessionFactory sesssionFactory;

	@Override
	public void addUser(User user) {
		sesssionFactory.getCurrentSession().save(user);	
	}

	@Override
	public void updateUser(User user) {
		sesssionFactory.getCurrentSession().update(user);	
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<User> getUser(int flag, String role) {
		String hql = "from User u where u.flag = :flag and role=:role";
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("flag", flag);
		q.setParameter("role", role);
		
		List<User> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean checkUserExists(String colName, String colVal) {
		
		String hql = String.format("select count(u.id) from User u where LOWER(u.%s) = :%s", colName, colName);
		
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter(colName, colVal);
		
		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return false;
		}
		
		long count = (long)resultList.get(0);
		
		return count==0?false:true;
	}
	
}

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<User> getUserByName(String name, int startIdx, int size) {
		String hql = String.format("select new com.web.entity.User(%s) from User u where u.name like :name order by u.name"
				 , "u.name,u.email,u.role,u.mobile,u.qq,u.discount");
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("name", "%" + name + "%");
		q.setFirstResult(startIdx);
		q.setMaxResults(size);
		
		List<User> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<User> getUserByTel(String tel, int startIdx, int size) {
		String hql = String.format("select new com.web.entity.User(%s) from User u where u.mobile like :mobile order by u.mobile"
				 , "u.name,u.email,u.role,u.mobile,u.qq,u.discount");
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("mobile", "%" + tel + "%");
		q.setFirstResult(startIdx);
		q.setMaxResults(size);
		
		List<User> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<User> getAllUser(int startIdx, int size) {
		String hql = String.format("select new com.web.entity.User(%s) from User u"
				 , "u.name,u.email,u.role,u.mobile,u.qq,u.discount");
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);
		
		List<User> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public long countUserByName(String name) {
		String hql = String.format(
				"select count(u.id) from User u where u.name like :name");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("name", "%" + name + "%");

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public long countAllUser() {
		String hql = String.format(
				"select count(u.id) from User u");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public long countUserByTel(String tel) {
		String hql = String.format(
				"select count(u.id) from User u where u.mobile like :mobile");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("mobile", "%" + tel + "%");

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public long countUserBySearch(String queryParam) {
		String hql = String.format(
				"select count(u.id) from User u where u.name like :name or u.email like :email or u.mobile like :mobile or u.qq like :qq");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("mobile", "%" + queryParam + "%");
		q.setParameter("name", "%" + queryParam + "%");
		q.setParameter("email", "%" + queryParam + "%");
		q.setParameter("qq", "%" + queryParam + "%");

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<User> getUserBySearch(String queryParam, int startIdx, int size) {
		String hql = String.format("select new com.web.entity.User(%s) from User u where u.name like :name or u.email like :email or u.mobile like :mobile or u.qq like :qq"
				 , "u.name,u.email,u.role,u.mobile,u.qq,u.discount");
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("mobile", "%" + queryParam + "%");
		q.setParameter("name", "%" + queryParam + "%");
		q.setParameter("email", "%" + queryParam + "%");
		q.setParameter("qq", "%" + queryParam + "%");
		q.setFirstResult(startIdx);
		q.setMaxResults(size);
		
		List<User> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList;
	}
	
}

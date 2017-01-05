package com.web.basedata;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.Commision;

@Repository
public class CommisionDaoImpl implements CommisionDao {

	@Resource
	public SessionFactory sesssionFactory;

	@Override
	public List<Commision> getCommision(int type) {
		String hql = "select new com.web.entity.Commision(e.type, e.srv_type, e.srv_mode, e.fee, e.date) from Commision "
				+ "e where e.type = :type and e.date = (select max(date) from Commision f where e.type = f.type"
				+ " and e.srv_type=f.srv_type and e.srv_mode=f.srv_mode group by e.srv_type,e.srv_mode,e.type)";
		@SuppressWarnings("rawtypes")
		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("type", type);
		
		@SuppressWarnings("unchecked")
		List<Commision> resultList = q.getResultList();
		if (resultList.size() == 0) {
			return null;
		}
		
		return resultList;
	}

	@Override
	public List<Commision> getCommision(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCommision(Commision comm) {
		sesssionFactory.getCurrentSession().save(comm);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public long getCount() {
		String hql = String.format(
				"select count(c.id) from Commision c");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Commision> getCommision(int startIdx, int size) {
		String hql = String.format("select new com.web.entity.Commision(c.type, c.srv_type, c.srv_mode, c.fee, c.date) from Commision c order by c.date desc");

			Query q = sesssionFactory.getCurrentSession().createQuery(hql);
			q.setFirstResult(startIdx);
			q.setMaxResults(size);

			return q.getResultList();
	}

}

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
		String hql = "select new com.web.entity.Commision(e.type, e.srv_type, e.fee, e.date) from Commision "
				+ "e where e.type = :type and e.date = (select max(date) from Commision f where e.type = f.type)";
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
	public void addCommision(int type,  List<Commision> commList) {
		// TODO Auto-generated method stub

	}

}

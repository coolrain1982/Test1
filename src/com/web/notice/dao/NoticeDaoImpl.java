package com.web.notice.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.web.entity.Notice;

@Repository
public class NoticeDaoImpl implements NoticeDao {
	
	@Resource
	public SessionFactory sesssionFactory;

	@Override
	public void newNotice(Notice notice) {
		sesssionFactory.getCurrentSession().save(notice);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public long getNoticeCount(int status) {
		String hql = String.format(
				"select count(n.id) from Notice n where n.status=:status");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("status", status);

		List resultList = q.getResultList();
		if (resultList.size() == 0) {
			return 0;
		}
		return (long) resultList.get(0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Notice> getNotices(int status, int startIdx, int size) {
		String hql = String.format(
				"select new com.web.entity.Notice(%s) from Notice n join n.user u where n.status=:status order by n.release_date desc", 
				"n.status, n.release_date, n.url, n.title, u.name");

		Query q = sesssionFactory.getCurrentSession().createQuery(hql);
		q.setParameter("status", status);
		q.setFirstResult(startIdx);
		q.setMaxResults(size);

		return q.getResultList();
	}

}
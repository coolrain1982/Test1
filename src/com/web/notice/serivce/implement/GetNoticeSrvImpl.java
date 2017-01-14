package com.web.notice.serivce.implement;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.common.PageForQuery;
import com.web.entity.Notice;
import com.web.notice.dao.NoticeDao;
import com.web.notice.serivce.GetNoticeService;

@Service
public class GetNoticeSrvImpl implements GetNoticeService {

	@Resource
	public NoticeDao noticeDao;

	@Override
	@Transactional
	public List<Notice> getNotice(int status, PageForQuery pfq) throws Exception {
		int startIdx = (pfq.getPage() - 1) * pfq.getSize();
		return noticeDao.getNotices(status, startIdx, pfq.getSize());
	}

	@Override
	@Transactional
	public long getNoticeCount(int status) throws Exception {
		return noticeDao.getNoticeCount(status);
	}
	
}

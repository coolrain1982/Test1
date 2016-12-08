package com.web.notice.dao;

import java.util.List;

import com.web.entity.Notice;

public interface NoticeDao {
	public void newNotice(Notice notice);
	public long getNoticeCount(int status);
	public List<Notice> getNotices(int status, int startIdx, int size);
}

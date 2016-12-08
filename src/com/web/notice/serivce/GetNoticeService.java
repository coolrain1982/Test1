package com.web.notice.serivce;

import java.util.List;

import com.web.common.PageForQuery;
import com.web.entity.Notice;

public interface GetNoticeService {
	
	public List<Notice> getNotice(int status, PageForQuery pfq) throws Exception;
	public long getNoticeCount(int status) throws Exception;
}

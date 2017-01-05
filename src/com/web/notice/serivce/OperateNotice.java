package com.web.notice.serivce;

public interface OperateNotice {
	void top(long id, int action) throws Exception;
	void delete(long id) throws Exception;
	void modify(long id, String newContent) throws Exception;
}

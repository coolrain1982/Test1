package com.web.notice.serivce;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.entity.Notice;
import com.web.notice.dao.NoticeDao;

@Service
public class OperateNoticeImpl implements OperateNotice {
	
	@Resource
	public NoticeDao noticeDao;
	
	@Value("${notice.base}")
	private String uploadBase;

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void top(long id, int action) throws Exception {
		
		//根据id取notice
		Notice notice = noticeDao.getNoticeById(id);
		if (notice == null) {
			throw new Exception("没有找到对应的公告记录！");
		}
		
		if (notice.getFlag() == 0) {
			throw new Exception("此公告状态为无效，不能进行对应操作！");
		}
		
		switch (action) {
		case 1:
			//取最大top
			int top = noticeDao.getMaxTop();
			notice.setTop(top + 1);
			break;
			//
		case 0:
			notice.setTop(0);
			break;
		default:
			throw new Exception("Operate notice action invalid!");
		}
		
		noticeDao.update(notice);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(long id) throws Exception {
		// 根据id取notice
		Notice notice = noticeDao.getNoticeById(id);
		if (notice == null) {
			throw new Exception("没有找到对应的公告记录！");
		}

		if (notice.getFlag() == 0) {
			throw new Exception("此公告状态为无效，不能进行对应操作！");
		}
		
		notice.setFlag(0);
		noticeDao.update(notice);

	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void modify(long id, String newContent) throws Exception {
		// 根据id取notice
		Notice notice = noticeDao.getNoticeById(id);
		if (notice == null) {
			throw new Exception("没有找到对应的公告记录！");
		}

		if (notice.getFlag() == 0) {
			throw new Exception("此公告状态为无效，不能进行对应操作！");
		}
		
		//把内容存为文件
		String url = ReleaseNoticeSrvImpl.saveNotice(newContent, uploadBase, true);
		notice.setUrl(url);
		noticeDao.update(notice);
	}

}

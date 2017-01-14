package com.web.notice.serivce.implement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.web.entity.Notice;
import com.web.entity.User;
import com.web.notice.dao.NoticeDao;
import com.web.notice.serivce.ReleaseNoticeService;
import com.web.user.UserService;

@Service
public class ReleaseNoticeSrvImpl implements ReleaseNoticeService {

	@Resource
	public UserService userService;
	
	@Resource
	public NoticeDao noticeDao;
	
	@Value("${notice.base}")
	private String uploadBase;
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public Notice releaseNotice(String userName, MultiValueMap<String, Object> reqParams) throws Exception {
		Map<String, List<Object>> params = reqParams;
		
		// 取用户信息
		User user = null;
		
		user = userService.getUser(userName);
		if (user == null) {
			throw new Exception("未知用户：" + userName);
		}
		
		if (!user.getRole().equalsIgnoreCase("role_admin")) {
			throw new Exception("您没有此操作权限");
		}
		
		//取标题，类型，内容
		String title, content;
		int status;
		
		if (params.containsKey("status") && params.get("status").size() > 0) {
			try {
				status = Integer.parseInt(params.get("status").get(0).toString().trim());
			} catch (Exception e) {
				throw new Exception("请选择正确的公告类型！");
			}
		} else {
			throw new Exception("请选择正确的公告类型！");
		}
		
		if (params.containsKey("title") && params.get("title").size() > 0 &&
				!params.get("title").get(0).toString().trim().equals("")) {
			title = params.get("title").get(0).toString().trim();
		} else {
			throw new Exception("公告概述输入不正确！");
		}
		
		if (params.containsKey("content") && params.get("content").size() > 0 &&
				!params.get("content").get(0).toString().trim().equals("")) {
			content = params.get("content").get(0).toString().trim();
			if (content.length() > 10*1000) {
				throw new Exception("公告内容不能超过10k个字符！");
			}
		} else {
			throw new Exception("公告内容输入不正确！");
		}
		
		//把内容存为文件
		String url = saveNotice(content, uploadBase, true);
		
		Notice notice = new Notice();
		notice.setRelease_date(Calendar.getInstance());
		notice.setStatus(status);
		notice.setTitle(title);
		notice.setUrl(url);
		notice.setUser(user);
		notice.setFlag(1);
		notice.setTop(0);
		
		noticeDao.newNotice(notice);
		
		return notice;
	}
	
	public static String saveNotice(String content, String uploadBase, boolean forceEncode) throws Exception {
		Calendar c = Calendar.getInstance();
		String fileSavePath = String.format("%s%s%s", 
				File.separatorChar, c.get(Calendar.YEAR), File.separatorChar) ;
		
		File baseDir = new File(String.format("%s%s%s", uploadBase, File.separatorChar, fileSavePath));
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
		
		File noticeF = new File(String.format("%s%s%s.html",baseDir,File.separatorChar, c.getTimeInMillis()));
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(noticeF), "UTF-8"));
		} catch (IOException ioe) {
			throw new Exception("创建公告文件失败：" + ioe.getMessage());
		}
		
		try {
			if (forceEncode) {
				bw.append("<html><head><meta charset='utf-8'></head><body>");
			}
			bw.append(content);
			if (forceEncode) {
				bw.append("</body></html>");
			}
			bw.flush();
		} catch (IOException ioe) {
			throw new Exception("写入公告文件失败：" + ioe.getMessage());
		} finally {
			try {
				bw.close();
				bw = null;
			} catch (Exception e) {}
		}
		
		return fileSavePath +  c.getTimeInMillis();
	}

}

package com.web.notice;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.PageForQuery;
import com.web.entity.Notice;
import com.web.notice.serivce.GetNoticeService;
import com.web.notice.serivce.ReleaseNoticeService;
import com.web.order.ctrl.UserOrderCtrl;

@Controller
public class NoticeCtrl {
	
	@Resource
	public ReleaseNoticeService relNoticeSrv;
	
	@Resource
	public GetNoticeService getNoticeSrv;
	
	@RequestMapping("admin/notice/release.do")
	@ResponseBody
	public Map<String, Object> releaseNotice(@RequestParam MultiValueMap<String, Object> params) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		String userName = "";
		
		try {
			userName = UserOrderCtrl.getLoginName();
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
			return rtnMap;
		}
		
		try {
			
			Notice notice=relNoticeSrv.releaseNotice(userName, params);
			
			rtnMap.put("status", 1);
			rtnMap.put("url", notice.getUrl());
			rtnMap.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(notice.getRelease_date().getTime()));	
		} catch (Exception e) {
			rtnMap.put("error", e.getMessage());
		}
		
		return rtnMap;
	}
	
	@RequestMapping("notice/get.do")
	@ResponseBody
	public Map<String, Object> getNotice(@RequestParam int status, 
			                            @RequestParam int page, 
			                            @RequestParam int size ) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
				
		try {
			long count = getNoticeSrv.getNoticeCount(status);
			rtnMap.put("count", count);
			
			if (count == 0) {
				return rtnMap;
			}
		} catch (Exception e) {
			rtnMap.put("error", String.format("获取公告总记录数失败：%s! status[%s],page[%s],size[%s]" ,
					e.getMessage(), status, page, size));
			return rtnMap;
		}
				
		try {
			List<Notice> notices = getNoticeSrv.getNotice(status, new PageForQuery(page, size));
			rtnMap.put("list", notices);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("获取公告失败：%s! status[%s],page[%s],size[%s]" ,
					e.getMessage(), status, page, size));
		}
		
		return rtnMap;
	}
}
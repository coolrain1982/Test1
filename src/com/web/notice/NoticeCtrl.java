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
import com.web.notice.serivce.OperateNotice;
import com.web.notice.serivce.ReleaseNoticeService;
import com.web.order.ctrl.UserOrderCtrl;
import com.web.user.UserController;
import com.web.user.UserService;

@Controller
public class NoticeCtrl {
	
	@Resource
	public ReleaseNoticeService relNoticeSrv;
	
	@Resource
	public GetNoticeService getNoticeSrv;
	
	@Resource
	public UserService userService;
	
	@Resource 
	public OperateNotice opNoticeSrv;
	
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
	
	@RequestMapping("admin/setNoticeTop.do")
	@ResponseBody
	public Map<String, Object> topNotice(@RequestParam long id, 
			                            @RequestParam int settopaction) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		try {
			if (!UserController.checkIsAdmin(userService)) {
				rtnMap.put("error","您没有此操作权限");
				return rtnMap;
			}
		} catch (Exception e1) {
			rtnMap.put("error",e1.getMessage());
			return rtnMap;
		}
		
		try {
			opNoticeSrv.top(id, settopaction);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("公告置顶相关操作失败: %s" ,
					e.getMessage()));
		}
		
		return rtnMap;
	}
	
	@RequestMapping("admin/deleteNotice.do")
	@ResponseBody
	public Map<String, Object> deleteNotice(@RequestParam long id) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		try {
			if (!UserController.checkIsAdmin(userService)) {
				rtnMap.put("error","您没有此操作权限");
				return rtnMap;
			}
		} catch (Exception e1) {
			rtnMap.put("error",e1.getMessage());
			return rtnMap;
		}
		
		try {
			opNoticeSrv.delete(id);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("公告置顶相关操作失败: %s" ,
					e.getMessage()));
		}
		
		return rtnMap;
	}
	
	@RequestMapping("admin/modifyNotice.do")
	@ResponseBody
	public Map<String, Object> modifyNotice(@RequestParam MultiValueMap<String, Object> params) {
		
		Map<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("status", 0);
		
		try {
			if (!UserController.checkIsAdmin(userService)) {
				rtnMap.put("error","您没有此操作权限");
				return rtnMap;
			}
		} catch (Exception e1) {
			rtnMap.put("error",e1.getMessage());
			return rtnMap;
		}
		
		long id;
		String newContent = null;
		if (params.containsKey("id") && params.get("id").size() > 0) {
			try {
				id = Long.parseLong(params.get("id").get(0).toString().trim());
			} catch (Exception e) {
				rtnMap.put("error","请选择正确的公告！");
				return rtnMap;
			}
		} else {
			rtnMap.put("error","请选择正确的公告！");
			return rtnMap;
		}
		
		if (params.containsKey("newcontent") && params.get("newcontent").size() > 0) {
			newContent = params.get("newcontent").get(0).toString().trim();
			if (newContent.length() > 10*1000) {
				rtnMap.put("error","公告内容不能超过10k个字符！");
				return rtnMap;
			}
		} else {
			rtnMap.put("error","请填入正确的公告内容！");
			return rtnMap;
		}
		
		try {
			opNoticeSrv.modify(id, newContent);
			rtnMap.put("status", 1);
		} catch (Exception e) {
			rtnMap.put("error", String.format("公告置顶相关操作失败: %s" ,
					e.getMessage()));
		}
		
		return rtnMap;
	}
}

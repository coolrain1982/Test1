package com.web.notice.serivce;

import org.springframework.util.MultiValueMap;

import com.web.entity.Notice;

public interface ReleaseNoticeService {
	public Notice releaseNotice(String userName, MultiValueMap<String, Object> reqParams) throws Exception;
}

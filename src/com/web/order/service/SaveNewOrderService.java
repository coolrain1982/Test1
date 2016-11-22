package com.web.order.service;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public interface SaveNewOrderService {
	public long saveNewOrder(String userName, MultiValueMap<String, Object> params, MultipartFile[] files) throws Exception;
}

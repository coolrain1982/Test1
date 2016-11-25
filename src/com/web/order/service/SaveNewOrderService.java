package com.web.order.service;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.web.entity.Order;

public interface SaveNewOrderService {
	public Order saveNewOrder(String userName, MultiValueMap<String, Object> params, MultipartFile[] files) throws Exception;
}

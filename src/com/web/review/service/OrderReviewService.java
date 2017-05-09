package com.web.review.service;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.web.entity.OrderForReview;

public interface OrderReviewService {

	public List<OrderForReview> getReviews(long orderid) throws Exception;
	public OrderForReview newOrderReview(MultiValueMap<String, Object> params) throws Exception;
	public OrderForReview updateOrderReview(MultiValueMap<String, Object> params) throws Exception;
	public OrderForReview saveOrderReview(MultiValueMap<String, Object> params) throws Exception;
	public OrderForReview submitOrderReview(MultiValueMap<String, Object> params) throws Exception;
}

package com.web.review.dao;

import java.util.List;

import com.web.entity.OrderForReview;

public interface OrderReviewDao {
	public void newOrderReview(OrderForReview orderReview);
	public void updateOrderReview(OrderForReview orderReview);
	public List<OrderForReview> getReviews(long orderid);
}

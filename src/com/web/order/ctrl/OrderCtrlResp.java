package com.web.order.ctrl;

public class OrderCtrlResp {
	private int status = 0 ; //sucess-1,failed-0
	private String error = "";
	private long orderId = 0;
	
	public OrderCtrlResp(int status, String error, long orderId) {
		this.status = status;
		this.error = error;
		this.orderId = orderId;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
}

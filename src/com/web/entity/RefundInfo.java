package com.web.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="tbl_refundinfo", schema="meiyabuy")
public class RefundInfo {
	private Long id;
	private Integer status, pay_type, refund_type;
	private String sn, payee, remark;
	private Order order;
	private User user;
	private Calendar refund_date;
	private Double money;
	private String userName;
	
	public RefundInfo() {}
	
	public RefundInfo(long id, Integer status, Integer pay_type, Integer refund_type
			, String sn, String payee, String remark, Calendar refund_date, Double money, String username) {
		
		setId(id);
		setStatus(status);
		setPay_type(pay_type);
		setRefund_date(refund_date);
		setRefund_type(refund_type);
		setSn(sn);
		setPayee(payee);
		setRemark(remark);
		setMoney(money);
		setUserName(username);
		setUser(null);
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column
	public Integer getPay_type() {
		return pay_type;
	}

	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}

	@Column
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getRefund_date() {
		return refund_date;
	}

	public void setRefund_date(Calendar refund_date) {
		this.refund_date = refund_date;
	}

	@Column
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@ManyToOne
	@JoinColumn(name="order_id")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Column
	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	@Column
	public Integer getRefund_type() {
		return refund_type;
	}

	public void setRefund_type(Integer refund_type) {
		this.refund_type = refund_type;
	}

	@Column
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}

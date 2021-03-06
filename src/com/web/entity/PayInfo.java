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

@Entity
@Table(name="tbl_payinfo", schema="meiyabuy")
public class PayInfo {
	public static final Integer INIT = 1;
	public static final Integer SUCCESS = 2;
	public static final Integer FAILED = 3;
	public static final Integer INVALID = 0;
	private Integer status, pay_type;
	private Long id;
	private String sn, payer;
	private Order order;
	private Calendar pay_date, audit_date;
	private Double money;
	private String auditRemark, auditor;
	
	public PayInfo() {}
	
	public PayInfo(long id, int status, int pay_type, String sn, String payer, 
			Calendar pay_date, Double money, String auditRemark) {
		setId(id);
		setSn(sn);
		setStatus(status);
		setPay_date(pay_date);
		setPay_type(pay_type);
		setPayer(payer);
		setMoney(money);
		setAuditRemark(auditRemark);
	}
	
	@ManyToOne
	@JoinColumn(name="order_id")
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
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
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getPay_date() {
		return pay_date;
	}
	public void setPay_date(Calendar pay_date) {
		this.pay_date = pay_date;
	}
	
	@Column
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
	@Column
	public Integer getPay_type() {
		return pay_type;
	}
	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}
	
	@Column
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}

	@Column
	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	@Column
	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	@Column
	public Calendar getAudit_date() {
		return audit_date;
	}

	public void setAudit_date(Calendar audit_date) {
		this.audit_date = audit_date;
	}
}

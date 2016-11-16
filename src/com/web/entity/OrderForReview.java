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
@Table(name="tbl_order_review", schema="meiyabuy")
public class OrderForReview {
	private long id;
    private int status;
	private String a_No, audit_name;
	private Calendar submit_date, audit_date, finish_date;
	private Reviewer reviewer;
	private Order order;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column
	public String getA_No() {
		return a_No;
	}
	public void setA_No(String a_No) {
		this.a_No = a_No;
	}
	
	@Temporal(TemporalType.DATE)
	public Calendar getSubmit_date() {
		return submit_date;
	}
	public void setSubmit_date(Calendar submit_date) {
		this.submit_date = submit_date;
	}
	
	@Temporal(TemporalType.DATE)
	public Calendar getAudit_date() {
		return audit_date;
	}
	public void setAudit_date(Calendar audit_date) {
		this.audit_date = audit_date;
	}
	
	@Temporal(TemporalType.DATE)
	public Calendar getFinish_date() {
		return finish_date;
	}
	public void setFinish_date(Calendar finish_date) {
		this.finish_date = finish_date;
	}
	
	@ManyToOne
	@JoinColumn(name="review_id")
	public Reviewer getReviewer() {
		return reviewer;
	}
	public void setReviewer(Reviewer reviewer) {
		this.reviewer = reviewer;
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
	public String getAudit_name() {
		return audit_name;
	}
	public void setAudit_name(String audit_name) {
		this.audit_name = audit_name;
	}

}

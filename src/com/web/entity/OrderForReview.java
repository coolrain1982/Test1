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
	//status:0-无效；1-初始值；2-待审核；3-审核不通过；4-完成review；5-已付款给review
    private int status;
	private String a_No, audit_name, review_remark, audit_remark, review_name;
	private Calendar submit_date, audit_date, finish_date, create_date;
	private Reviewer reviewer;
	private Order order;
	
	public OrderForReview() {}
	
	public OrderForReview(long id, int status, String a_No, String audit_name, String review_remark,
			String audit_remark, String review_name, Calendar submit_date, Calendar audit_date, 
			Calendar finish_date, Calendar create_date) {
		setId(id);
		setStatus(status);
		setA_No(a_No);
		setAudit_date(audit_date);
		setAudit_name(audit_name);
		setReview_remark(review_remark);
		setAudit_remark(audit_remark);
		setReview_name(review_name);
		setSubmit_date(submit_date);
		setFinish_date(finish_date);
		setCreate_date(create_date);
	}
	
	
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
	
	@Temporal(TemporalType.TIMESTAMP)
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
	
	@Temporal(TemporalType.TIMESTAMP)
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
	
	@Column(length=3000)
	public String getReview_remark() {
		return review_remark;
	}
	public void setReview_remark(String review_remark) {
		this.review_remark = review_remark;
	}
	
	@Column
	public String getAudit_remark() {
		return audit_remark;
	}
	public void setAudit_remark(String audit_remark) {
		this.audit_remark = audit_remark;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Calendar create_date) {
		this.create_date = create_date;
	}
	
	@Column
	public String getReview_name() {
		return review_name;
	}
	public void setReview_name(String review_name) {
		this.review_name = review_name;
	}

}

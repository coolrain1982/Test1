package com.web.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="tbl_order", schema="meiyabuy")
public class Order {
	
	private long order_id;
	//status:0-无效；1-初始值；2-待支付；3-审核未通过；4-客户已支付；10-已经付中介；20-已全部完成review；21-部分完成并退款关闭
	private int status, discount; 
	private String link, key_word, product_descript, product_photo_url, audit_name, audit_remark;
	private double product_unit_price, product_unit_freight, product_total_price, product_unit_commission;
	private double exchange_rate, refunds;                                               
	private int product_quantity;
	private Calendar create_date, pay_date, audit_date, finish_date;
	private User user;
	private Set<OrderForReview> ordersForReview;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(long order_id) {
		this.order_id = order_id;
	}
	
	@Column
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	@Column
	public String getKey_word() {
		return key_word;
	}
	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}
	
	@Column
	public double getProduct_unit_price() {
		return product_unit_price;
	}
	public void setProduct_unit_price(double product_unit_price) {
		this.product_unit_price = product_unit_price;
	}
	
	@Column
	public int getProduct_quantity() {
		return product_quantity;
	}
	public void setProduct_quantity(int product_quantity) {
		this.product_quantity = product_quantity;
	}
	
	@Column
	public String getProduct_descript() {
		return product_descript;
	}
	public void setProduct_descript(String product_descript) {
		this.product_descript = product_descript;
	}
	
	@Column
	public String getProduct_photo_url() {
		return product_photo_url;
	}
	public void setProduct_photo_url(String product_photo_url) {
		this.product_photo_url = product_photo_url;
	}
	
	@ManyToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column
	public double getProduct_total_price() {
		return product_total_price;
	}
	public void setProduct_total_price(double product_total_price) {
		this.product_total_price = product_total_price;
	}
	
	@Column
	public double getProduct_unit_freight() {
		return product_unit_freight;
	}
	public void setProduct_unit_freight(double product_unit_freight) {
		this.product_unit_freight = product_unit_freight;
	}
	
	@Column
	public double getProduct_unit_commission() {
		return product_unit_commission;
	}
	public void setProduct_unit_commission(double product_unit_commission) {
		this.product_unit_commission = product_unit_commission;
	}
	
	@Column
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
	@Temporal(TemporalType.DATE)
	public Calendar getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Calendar create_date) {
		this.create_date = create_date;
	}
	
	@Temporal(TemporalType.DATE)
	public Calendar getPay_date() {
		return pay_date;
	}
	public void setPay_date(Calendar pay_date) {
		this.pay_date = pay_date;
	}
	
	@Temporal(TemporalType.DATE)
	public Calendar getFinish_date() {
		return finish_date;
	}
	public void setFinish_date(Calendar finish_date) {
		this.finish_date = finish_date;
	}
	
	@Temporal(TemporalType.DATE)
	public Calendar getAudit_date() {
		return audit_date;
	}
	public void setAudit_date(Calendar audit_date) {
		this.audit_date = audit_date;
	}
	
	@Column
	public String getAudit_name() {
		return audit_name;
	}
	public void setAudit_name(String audit_name) {
		this.audit_name = audit_name;
	}
	
	@OneToMany(mappedBy="order", fetch=FetchType.LAZY)
	public Set<OrderForReview> getOrdersForReview() {
		return ordersForReview;
	}
	
	public void setOrdersForReview(Set<OrderForReview> ordersForReview) {
		this.ordersForReview = ordersForReview;
	}
	
	@Column
	public String getAudit_remark() {
		return audit_remark;
	}
	public void setAudit_remark(String audit_remark) {
		this.audit_remark = audit_remark;
	}
	
	@Column
	public double getExchange_rate() {
		return exchange_rate;
	}
	public void setExchange_rate(double exchange_rate) {
		this.exchange_rate = exchange_rate;
	}
	
	@Column
	public double getRefunds() {
		return refunds;
	}
	public void setRefunds(double refunds) {
		this.refunds = refunds;
	}
}

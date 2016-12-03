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
	//status:0-已取消；1-初始值；2-待支付；3-审核未通过；4-客户已支付；10-已经付中介；20-已完成
	//type:1-仅购买；2-购买+review；3-购买+review+feedback
	private int status, discount, type, csid; 
	private String link, key_word, product_descript, product_photo_url, audit_remark;
	private Double product_unit_price, product_unit_freight, product_total_price, product_unit_commission;
	private Double exchange_rate, refunds, paypal_fee, paypal_rate, total;                                               
	private int product_quantity;
	private Calendar create_date, pay_date, audit_date, finish_date;
	private User user;
	private Set<OrderForReview> ordersForReview;
	private Set<PayInfo> orderPay;
	private Set<RefundInfo> orderRefund;
	
	public Order() {
		
	}
	
	public Order(long id, int discount, String product_descript,
			     String link, String product_photo_url, String audit_remark, 
			     Double product_unit_price, Double product_unit_freight,
			     Double product_unit_commission, Double exchange_rate, Double paypal_fee,
			     Double paypal_rate, int product_quantity, Calendar create_date, int status, 
			     int type, Calendar audit_date) {
		setOrder_id(id);
		setDiscount(discount);
		setProduct_descript(product_descript);
		setLink(link);
		setProduct_photo_url(product_photo_url);
		setAudit_remark(audit_remark);
		setProduct_unit_price(product_unit_price);
		setProduct_unit_freight(product_unit_freight);
		setProduct_unit_commission(product_unit_commission);
		setExchange_rate(exchange_rate);
		setPaypal_fee(paypal_fee);
		setPaypal_rate(paypal_rate);
		setProduct_quantity(product_quantity);
		setCreate_date(create_date);
		setStatus(status);
		setType(type);
		setAudit_date(audit_date);
	}
	
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
	public Double getProduct_unit_price() {
		return product_unit_price;
	}
	public void setProduct_unit_price(Double product_unit_price) {
		this.product_unit_price = product_unit_price;
	}
	
	@Column
	public int getProduct_quantity() {
		return product_quantity;
	}
	public void setProduct_quantity(Integer product_quantity) {
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
	public Double getProduct_total_price() {
		return product_total_price;
	}
	public void setProduct_total_price(Double product_total_price) {
		this.product_total_price = product_total_price;
	}
	
	@Column
	public Double getProduct_unit_freight() {
		return product_unit_freight;
	}
	public void setProduct_unit_freight(Double product_unit_freight) {
		this.product_unit_freight = product_unit_freight;
	}
	
	@Column
	public Double getProduct_unit_commission() {
		return product_unit_commission;
	}
	public void setProduct_unit_commission(Double product_unit_commission) {
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
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Calendar create_date) {
		this.create_date = create_date;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getPay_date() {
		return pay_date;
	}
	public void setPay_date(Calendar pay_date) {
		this.pay_date = pay_date;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFinish_date() {
		return finish_date;
	}
	public void setFinish_date(Calendar finish_date) {
		this.finish_date = finish_date;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getAudit_date() {
		return audit_date;
	}
	public void setAudit_date(Calendar audit_date) {
		this.audit_date = audit_date;
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
	public Double getExchange_rate() {
		return exchange_rate;
	}
	public void setExchange_rate(Double exchange_rate) {
		this.exchange_rate = exchange_rate;
	}
	
	@Column
	public Double getRefunds() {
		return refunds;
	}
	public void setRefunds(Double refunds) {
		this.refunds = refunds;
	}
	
	@Column
	public Double getPaypal_fee() {
		return paypal_fee;
	}
	public void setPaypal_fee(Double paypal_fee) {
		this.paypal_fee = paypal_fee;
	}
	
	@Column
	public Double getPaypal_rate() {
		return paypal_rate;
	}
	public void setPaypal_rate(Double paypal_rate) {
		this.paypal_rate = paypal_rate;
	}
	
	@Column
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}

	@Column
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column
	public int getCsid() {
		return csid;
	}

	public void setCsid(int csid) {
		this.csid = csid;
	}

	@OneToMany(mappedBy="order", fetch=FetchType.LAZY)
	public Set<PayInfo> getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(Set<PayInfo> orderPay) {
		this.orderPay = orderPay;
	}

	@OneToMany(mappedBy="order", fetch=FetchType.LAZY)
	public Set<RefundInfo> getOrderRefund() {
		return orderRefund;
	}

	public void setOrderRefund(Set<RefundInfo> orderRefund) {
		this.orderRefund = orderRefund;
	}
}

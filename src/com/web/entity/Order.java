package com.web.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "tbl_order", schema = "meiyabuy")
public class Order {

	public static final int CANCEL = 0;
	public static final int INIT = 1;
	public static final int WAIT_PAY = 2;
	public static final int REJECT = 3;
	public static final int PAYED = 4;
	public static final int PAYED_SUCCESS = 5;
	public static final int WAIT_ASSIGNMENT = 5;
	public static final int PAYED_FAIL = 6;
	public static final int IN_REVIEW = 7;
	public static final int PAY_TO_AGENT = 10;
	public static final int WAIT_FINISH = 20;
	public static final int FINISH = 21;

	private long order_id;
	// status:0-已取消；1-初始值；2-待支付；3-审核未通过；4-客户已支付；5-支付审核通过；6-支付审核失败；10-已经付中介；20-已完成；21-用户确认完成
	// type:1-仅购买；2-购买+review；
	private int status, discount, type, csid;
	private Integer find_product_mode;  //1-链接;2-关键词搜索
	private String link, key_word, product_descript, product_photo_url, audit_remark, product_asin, shop_name;
	private Double product_unit_price, product_unit_freight, product_total_price, product_unit_commission;
	private Double exchange_rate, refunds, paypal_fee, paypal_rate, total;
	private int product_quantity;
	private Integer search_page_idx;
	private Calendar create_date, pay_date, audit_date, finish_date;
	private User user;
	private Commision comm;
	private Set<OrderForReview> ordersForReview;
	private Set<PayInfo> orderPay;
	private Set<RefundInfo> orderRefund;
	private String userName;
	private Integer hasRefund;
	private Double fee1, fee2;

	public Order() {

	}
	
	public Order(long id, int discount, String product_descript, String link, String product_asin, String product_photo_url,
			String audit_remark, Double product_unit_price, Double product_unit_freight, Double product_unit_commission,
			Double exchange_rate, Double paypal_fee, Double paypal_rate, int product_quantity, Calendar create_date,
			int status, int type, Calendar audit_date, String userName, Integer find_product_mode,
			Integer search_page_idx, String shop_name, String key_word, Double fee1, Double fee2, Integer hasRefund) {
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
		setUserName(userName);
		setProduct_asin(product_asin);
		setFind_product_mode(find_product_mode);
		setSearch_page_idx(search_page_idx);
		setShop_name(shop_name);
		setKey_word(key_word);
		setHasRefund(hasRefund);
		setFee1(fee1);
		setFee2(fee2);
		
		if (type == 1) {
			setProduct_unit_commission(fee1);
		} else {
			setProduct_unit_commission(fee1 + fee2);
		}
	}
	
	public Order(long id, int discount, String product_descript, String link, String product_asin, String product_photo_url,
			String audit_remark, Double product_unit_price, Double product_unit_freight, Double product_unit_commission,
			Double exchange_rate, Double paypal_fee, Double paypal_rate, int product_quantity, Calendar create_date,
			int status, int type, Calendar audit_date, String userName, Integer find_product_mode,
			Integer search_page_idx, String shop_name, String key_word, Double fee1, Double fee2, 
			Integer hasRefund, Set<PayInfo> payinfos) {
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
		setUserName(userName);
		setProduct_asin(product_asin);
		setFind_product_mode(find_product_mode);
		setSearch_page_idx(search_page_idx);
		setShop_name(shop_name);
		setKey_word(key_word);
		setHasRefund(hasRefund);
		setFee1(fee1);
		setFee2(fee2);
		setOrderPay(payinfos);
		
		if (type == 1) {
			setProduct_unit_commission(fee1);
		} else {
			setProduct_unit_commission(fee1 + fee2);
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@JoinColumn(name = "user_id")
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

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	@Where(clause="status=4")
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

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	public Set<PayInfo> getOrderPay() {
		return orderPay;
	}

	public void setOrderPay(Set<PayInfo> orderPay) {
		this.orderPay = orderPay;
	}

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	public Set<RefundInfo> getOrderRefund() {
		return orderRefund;
	}

	public void setOrderRefund(Set<RefundInfo> orderRefund) {
		this.orderRefund = orderRefund;
	}

	public static String getFormatOrderID(long orderID, int len) {
		String strOrderID = String.valueOf(orderID);
		for (int i = 0; i < len; i++) {
			strOrderID = '0' + strOrderID;
		}
		return strOrderID.substring(strOrderID.length() - len, strOrderID.length());
	}
	
	public static List<Integer> getProcessOrderStatus() {
		List<Integer> processStatus = new ArrayList<>();
		processStatus.add(Order.WAIT_PAY);
		processStatus.add(Order.PAYED_FAIL);
		processStatus.add(Order.WAIT_FINISH);
		
		return processStatus;
	}
	
	public static List<Integer> getDoingOrderStatus() {
		List<Integer> doingStatus = new ArrayList<>();
		doingStatus.add(Order.PAYED_SUCCESS);
		doingStatus.add(Order.PAY_TO_AGENT);
		doingStatus.add(Order.IN_REVIEW);
		
		return doingStatus;
	}
	
	public static List<Integer> getAdminProcessOrderStatus() {
		List<Integer> processStatus = new ArrayList<>();
		processStatus.add(Order.INIT);
		processStatus.add(Order.PAYED);
		
		return processStatus;
	}
	
	public static List<Integer> getAdminDoingOrderStatus() {
		List<Integer> doingStatus = new ArrayList<>();
		doingStatus.add(Order.PAYED_SUCCESS);
		doingStatus.add(Order.PAY_TO_AGENT);
		doingStatus.add(Order.IN_REVIEW);
		
		return doingStatus;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column
	public String getProduct_asin() {
		return product_asin;
	}

	public void setProduct_asin(String product_asin) {
		this.product_asin = product_asin;
	}

	@Column
	public Integer getFind_product_mode() {
		return find_product_mode;
	}

	public void setFind_product_mode(Integer find_product_mode) {
		this.find_product_mode = find_product_mode;
	}

	@Column
	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	@Column
	public Integer getSearch_page_idx() {
		return search_page_idx;
	}

	public void setSearch_page_idx(Integer search_page_idx) {
		this.search_page_idx = search_page_idx;
	}

	@Column
	public Integer getHasRefund() {
		return hasRefund;
	}

	public void setHasRefund(Integer hasRefund) {
		this.hasRefund = hasRefund;
	}

	@ManyToOne
	@JoinColumn(name="comm_id")
	public Commision getComm() {
		return comm;
	}

	public void setComm(Commision comm) {
		this.comm = comm;
	}

	@Transient
	public Double getFee1() {
		return fee1;
	}

	
	public void setFee1(Double fee1) {
		this.fee1 = fee1;
	}

	@Transient
	public Double getFee2() {
		return fee2;
	}

	
	public void setFee2(Double fee2) {
		this.fee2 = fee2;
	}

 }

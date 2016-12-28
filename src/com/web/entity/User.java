package com.web.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tbl_user", schema="meiyabuy")
public class User {
	
	private Integer id;
	private String name;
	private String password;
	private String email;
	private String role;
	private String mobile, qq;
	private Integer flag;
	private Integer discount;
	private Set<Order> orders;
	private Set<Notice> notices;
	private Set<ExchangeRate> exchangeRates;
	private Set<Commision> commisions;
	private Set<PaypalFee> paypalFees;
	
	public User() {
		
	}
	
	public User(String name, String email, String role, 
			    String mobile, String qq, Integer discount) {
		
		setName(name);
		setEmail(email);
		setRole(role);
		setMobile(mobile);
		setQq(qq);
		setDiscount(discount);
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	@Column(unique=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	@Column
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	@Column
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	@Column
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Column
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	
	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	public Set<Notice> getNotices() {
		return notices;
	}
	public void setNotices(Set<Notice> notices) {
		this.notices = notices;
	}
	
	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	public Set<ExchangeRate> getExchangeRates() {
		return exchangeRates;
	}
	public void setExchangeRates(Set<ExchangeRate> exchangeRates) {
		this.exchangeRates = exchangeRates;
	}
	
	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	public Set<Commision> getCommisions() {
		return commisions;
	}
	public void setCommisions(Set<Commision> commisions) {
		this.commisions = commisions;
	}
	
	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	public Set<PaypalFee> getPaypalFees() {
		return paypalFees;
	}
	public void setPaypalFees(Set<PaypalFee> paypalFees) {
		this.paypalFees = paypalFees;
	}
}

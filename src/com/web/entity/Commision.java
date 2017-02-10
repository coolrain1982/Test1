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
@Table(name="tbl_commision", schema="meiyabuy")
public class Commision {
	
	private Integer type; //1-美元
	private Integer srv_type; //保留
	private Integer srv_mode; //1-链接;2-搜索
	private Integer id;
	private Double fee; //保留
	private Double fee1; //购买
	private Double fee2; //review
	private Calendar date;
	private User user;
	private Set<Order> orders;
	
	public Commision() {
	}
	
	public Commision(Integer type, Integer srv_type, Integer srv_mode, Double fee,
			Double fee1, Double fee2, Calendar date) {
		this.type = type;
		this.fee = fee;
		this.fee1 = fee1;
		this.fee2 = fee2;
		this.date = date;
		this.srv_type = srv_type;
		this.srv_mode = srv_mode;
		this.setUser(new User());
		this.orders = null;
	}
	
	@Column
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar data) {
		this.date = data;
	}

	@Column
	public int getSrv_type() {
		return srv_type;
	}

	public void setSrv_type(int srv_type) {
		this.srv_type = srv_type;
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
	public int getSrv_mode() {
		return srv_mode;
	}

	public void setSrv_mode(int srv_mode) {
		this.srv_mode = srv_mode;
	}

	@OneToMany(mappedBy="comm", fetch=FetchType.LAZY)
	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@Column
	public Double getFee1() {
		return fee1;
	}

	public void setFee1(Double fee1) {
		this.fee1 = fee1;
	}

	@Column
	public Double getFee2() {
		return fee2;
	}

	public void setFee2(Double fee2) {
		this.fee2 = fee2;
	}

}

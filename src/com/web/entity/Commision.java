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
@Table(name="tbl_commision", schema="meiyabuy")
public class Commision {
	
	private int type; //1-美元
	private int srv_type; //1-仅购买；2-购买+review；
	private int srv_mode; //1-链接;2-搜索
	private int id;
	private double fee;
	private Calendar date;
	private User user;
	
	public Commision(int type, int srv_type, int srv_mode, double fee, Calendar date) {
		this.type = type;
		this.fee = fee;
		this.date = date;
		this.srv_type = srv_type;
		this.srv_mode = srv_mode;
		this.setUser(new User());
	}
	
	public Commision() {}
	
	@Column
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
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

}

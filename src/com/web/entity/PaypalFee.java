package com.web.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="tbl_paypal", schema="meiyabuy")
public class PaypalFee {
	
	private int type; //1-美元
	private int id;
	private double fee;
	private double fee_rate;
	private Calendar date;
	
	public PaypalFee(int type, double fee, double fee_rate) {
		this.type = type;
		this.fee = fee;
		this.fee_rate = fee_rate;
	}
	
	public PaypalFee() {}
	
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
	public double getFee_rate() {
		return fee_rate;
	}
	public void setFee_rate(double fee_rate) {
		this.fee_rate = fee_rate;
	}
}

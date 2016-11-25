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
@Table(name="tbl_exchange", schema="meiyabuy")
public class ExchangeRate {
	
	private int type; //1-美元
	private int id;
	private double rate;
	private Calendar date;
	
	public ExchangeRate(int type , double rate) {
		this.type = type;
		this.rate = rate;
	}
	
	public ExchangeRate() {}
	
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
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
}

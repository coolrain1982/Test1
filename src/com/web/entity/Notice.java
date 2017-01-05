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
@Table(name="tbl_notice", schema="meiyabuy")
public class Notice {
	private Integer status, top;
	private Long id;
	private Calendar release_date;
	private User user;
	private String url;
	private String title;
	private Integer flag; //0-无效,其他为有效
	
	public Notice() {
		
	}
	
	public Notice(Long id, Integer status, Calendar releasedata, String url, String title, String name, Integer top) {
		this.status = status;
		this.release_date = releasedata;
		this.url = url;
		this.title = title;
		this.user = new User();
		this.user.setName(name);
		this.top = top;
		this.id = id;
	}
	
	@Column
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getRelease_date() {
		return release_date;
	}
	public void setRelease_date(Calendar release_date) {
		this.release_date = release_date;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Column
	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	@Column
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
}

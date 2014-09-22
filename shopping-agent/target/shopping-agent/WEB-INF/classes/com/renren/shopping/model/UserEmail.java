package com.renren.shopping.model;

import java.util.Date;

/**
 * 用户绑定邮箱,邮箱登录
 * 
 * @author kk
 * 
 */
public class UserEmail {
	private int userId;
	private String email;
	private Date update_time;

	public UserEmail() {
	}

	public UserEmail(int userId, String email) {
		this.userId = userId;
		this.email = email;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "UserEmail [userId=" + userId + ", email=" + email
				+ ", update_time=" + update_time + "]";
	}

}

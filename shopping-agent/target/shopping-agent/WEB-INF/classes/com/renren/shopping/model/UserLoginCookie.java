package com.renren.shopping.model;

import java.util.Date;

/**
 * 用户浏览器登录设置cookie信息
 * 
 * @author kk
 * 
 */
public class UserLoginCookie {
	private int userId;
	private String loginCookie;
	private long deadline;
	private Date update_time;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLoginCookie() {
		return loginCookie;
	}

	public void setLoginCookie(String loginCookie) {
		this.loginCookie = loginCookie;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "UserLoginCookie [userId=" + userId + ", loginCookie="
				+ loginCookie + ", deadline=" + deadline + ", update_time="
				+ update_time + "]";
	}

}

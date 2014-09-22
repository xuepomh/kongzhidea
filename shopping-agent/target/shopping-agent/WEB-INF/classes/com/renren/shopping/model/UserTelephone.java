package com.renren.shopping.model;

import java.util.Date;

/**
 * 用户绑定手机号，手机号登录
 * 
 * @author kk
 * 
 */
public class UserTelephone {
	private int userId;
	private String telephone;
	private Date update_time;

	public UserTelephone() {
	}

	public UserTelephone(int userId, String telephone) {
		this.userId = userId;
		this.telephone = telephone;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "UserTelephone [userId=" + userId + ", telephone=" + telephone
				+ ", update_time=" + update_time + "]";
	}

}

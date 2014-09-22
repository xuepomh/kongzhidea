package com.renren.shopping.model;

import java.util.Date;

/**
 * 用户信息，其中telephone和email仅供查看(不保证通过验证)，用于登录的使用user_telephone和user_email
 * 
 * @author kk
 * 
 */
public class User {
	private int id;
	private String account;
	private String password_md5;
	private String name;
	private String description;
	private String original_url;// 头像大图 original
	private String main_url;
	private String head_url;
	private String tiny_url;
	private UserDetail userDetail;
	private int isV;// 用户V认证,0:不是; 1是
	private int credit;// 信用
	private int popularity;// 人气
	private int delete_flag;// 默认0， 1表示被删除
	private Date update_time;

	public User() {
	}

	public User(String account, String password_md5, String name) {
		this.account = account;
		this.password_md5 = password_md5;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword_md5() {
		return password_md5;
	}

	public void setPassword_md5(String password_md5) {
		this.password_md5 = password_md5;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOriginal_url() {
		return original_url;
	}

	public void setOriginal_url(String original_url) {
		this.original_url = original_url;
	}

	public String getMain_url() {
		return main_url;
	}

	public void setMain_url(String main_url) {
		this.main_url = main_url;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public String getTiny_url() {
		return tiny_url;
	}

	public void setTiny_url(String tiny_url) {
		this.tiny_url = tiny_url;
	}

	public int getIsV() {
		return isV;
	}

	public void setIsV(int isV) {
		this.isV = isV;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public int getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(int delete_flag) {
		this.delete_flag = delete_flag;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", password_md5="
				+ password_md5 + ", name=" + name + ", description="
				+ description + ", original_url=" + original_url
				+ ", main_url=" + main_url + ", head_url=" + head_url
				+ ", tiny_url=" + tiny_url + ", userDetail=" + userDetail
				+ ", isV=" + isV + ", credit=" + credit + ", popularity="
				+ popularity + ", delete_flag=" + delete_flag
				+ ", update_time=" + update_time + "]";
	}

}

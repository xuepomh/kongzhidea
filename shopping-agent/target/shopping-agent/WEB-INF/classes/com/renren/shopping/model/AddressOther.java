package com.renren.shopping.model;

import java.util.Date;

/**
 * 地址簿 其他地方用
 * @author kk
 *
 */
public class AddressOther {
	private long id;
	private int userId;// 地址所属用户ID
	private String receiverName;// 收件人
	private int countryId;// 收件人国家ID  @delete
	private String addressDetail;// 收件人详细地址 json格式 {"province":"北京市","provinceCode":"110000000000","city":"市辖区","cityCode":"110100000000","district":"宣武区","districtCode":"110104000000","street":"北三环东路"}
	private String zipcode;// 收件人邮编
	private String tel;// 收件人电话
	private int isDefault;// 是否默认地址
	private Date update_time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "AddreddUser [id=" + id + ", userId=" + userId
				+ ", receiverName=" + receiverName + ", countryId=" + countryId
				+ ", addressDetail=" + addressDetail + ", zipcode=" + zipcode
				+ ", tel=" + tel + ", isDefault=" + isDefault
				+ ", update_time=" + update_time + "]";
	}

}

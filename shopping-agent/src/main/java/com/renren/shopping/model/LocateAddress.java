package com.renren.shopping.model;

import java.util.Date;

/**
 * 定位地址
 * @author yu.zhuang
 *
 */

public class LocateAddress {
	private int id;
	private long longitude;//经度
	private long latitude;//纬度
	private String continent;
	private String country;
	private String province;
	private String city;
	private String street;
	private int order_id;//订单id
	private Date create_time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getLongitude() {
		return longitude;
	}
	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}
	public long getLatitude() {
		return latitude;
	}
	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	@Override
	public String toString() {
		return "LocateAddress [id=" + id + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", continent=" + continent
				+ ", country=" + country + ", province=" + province + ", city="
				+ city + ", street=" + street + ", order_id=" + order_id
				+ ", create_time=" + create_time + "]";
	}
	
	
}

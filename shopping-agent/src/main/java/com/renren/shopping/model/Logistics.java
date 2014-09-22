package com.renren.shopping.model;
/**
 * 订单
 * @author yu.zhuang
 */
import java.util.Date;

public class Logistics {
	private int id;
	private long orders_number;
	private String company;
	private String logistics_number;
	public long getOrders_number() {
		return orders_number;
	}
	public void setOrders_number(long orders_number) {
		this.orders_number = orders_number;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getLogistics_number() {
		return logistics_number;
	}
	public void setLogistics_number(String logistics_number) {
		this.logistics_number = logistics_number;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Logistics [id=" + id + ", orders_number=" + orders_number
				+ ", company=" + company + ", logistics_number="
				+ logistics_number + "]";
	}

}

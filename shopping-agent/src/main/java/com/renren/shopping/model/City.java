package com.renren.shopping.model;

import java.util.Date;

/**
 * 城市
 * 
 * @author kk
 * 
 */
public class City {
	private int id;
	private String name;
	private String description;
	private int provinceId;// 省id
	private int hot;// 是否为热门城市 0表示表示，非0表示热门，按照hot来排序
	private int weight;// 权重 根据洲来去城市时候根据weight来排序
	private Date update_time;

	public City() {
	}

	public City(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", description="
				+ description + ", provinceId=" + provinceId + ", hot=" + hot
				+ ", weight=" + weight + ", update_time=" + update_time + "]";
	}

}

package com.renren.shopping.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 省
 * 
 * @author kk
 * 
 */
public class Province {
	private int id;
	private String name;
	private String description;
	private int weight;// 权重 按照weight排序
	private Date update_time;
	// 一对多查询,在hbm中设置按照weight排序
	private Set<City> cities = new HashSet<City>();

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

	public Set<City> getCities() {
		return cities;
	}

	public void setCities(Set<City> cities) {
		this.cities = cities;
	}

	@Override
	public String toString() {
		return "Continent [id=" + id + ", name=" + name + ", description="
				+ description + ", weight=" + weight + ", update_time="
				+ update_time + ", citiesSize=" + cities.size() + "]";
	}

}

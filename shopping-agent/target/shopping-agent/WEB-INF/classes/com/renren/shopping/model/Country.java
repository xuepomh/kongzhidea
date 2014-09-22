package com.renren.shopping.model;

import java.util.Date;

/**
 * 国家
 * 
 * @author kk
 * 
 */
public class Country {
	private int id;
	private String name;
	private String description;
	private String flag;// 国旗 大图
	private String flag_thumb;// 国旗 小图
	private Continent continent;// many-to-one 取消延迟加载
	private int hot;// 是否为热门国家 0表示表示，非0表示热门，按照hot来排序
	private int weight;// 权重 根据洲来去国家时候根据weight来排序
	private Date update_time;

	public Country() {
	}

	public Country(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Country(int id, String name, String flag, String flag_thumb) {
		this.id = id;
		this.name = name;
		this.flag = flag;
		this.flag_thumb = flag_thumb;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlag_thumb() {
		return flag_thumb;
	}

	public void setFlag_thumb(String flag_thumb) {
		this.flag_thumb = flag_thumb;
	}

	public Continent getContinent() {
		return continent;
	}

	public void setContinent(Continent continent) {
		this.continent = continent;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", description="
				+ description + ", flag=" + flag + ", flag_thumb=" + flag_thumb
				+ ", continent=" + continent + ", hot=" + hot + ", weight="
				+ weight + ", update_time=" + update_time + "]";
	}

}

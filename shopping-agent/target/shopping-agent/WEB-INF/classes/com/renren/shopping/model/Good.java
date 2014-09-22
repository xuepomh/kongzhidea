package com.renren.shopping.model;

import java.util.Date;

/**
 * 商品
 * 
 * @author kk
 * 
 */
public class Good {
	private long id;
	private String name;
	private String description;
	private String defaultPhoto;// 默认大图
	private String defaultMainUrl;
	private String defaultHeadUrl;
	private String defaultTinyUrl;
	private Date update_time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getDefaultPhoto() {
		return defaultPhoto;
	}

	public void setDefaultPhoto(String defaultPhoto) {
		this.defaultPhoto = defaultPhoto;
	}

	public String getDefaultMainUrl() {
		return defaultMainUrl;
	}

	public void setDefaultMainUrl(String defaultMainUrl) {
		this.defaultMainUrl = defaultMainUrl;
	}

	public String getDefaultHeadUrl() {
		return defaultHeadUrl;
	}

	public void setDefaultHeadUrl(String defaultHeadUrl) {
		this.defaultHeadUrl = defaultHeadUrl;
	}

	public String getDefaultTinyUrl() {
		return defaultTinyUrl;
	}

	public void setDefaultTinyUrl(String defaultTinyUrl) {
		this.defaultTinyUrl = defaultTinyUrl;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "Good [id=" + id + ", name=" + name + ", description="
				+ description + ", defaultPhoto=" + defaultPhoto
				+ ", defaultMainUrl=" + defaultMainUrl + ", defaultHeadUrl="
				+ defaultHeadUrl + ", defaultTinyUrl=" + defaultTinyUrl
				+ ", update_time=" + update_time + "]";
	}

}

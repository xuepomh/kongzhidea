package com.renren.shopping.model;

import java.util.Date;

/**
 * 商品照片属性
 * 
 * @author kk
 * 
 */
public class GoodPhoto {
	private long id;
	private long goodId;// 商品照片所属商品ID
	private String description;
	private String photo_url;// 商品照片
	private String main_url;// 商品照片缩略图
	private String head_url;// 商品照片缩略图
	private String tiny_url;// 商品照片缩略图
	private Date update_time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGoodId() {
		return goodId;
	}

	public void setGoodId(long goodId) {
		this.goodId = goodId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoto_url() {
		return photo_url;
	}

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
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

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	@Override
	public String toString() {
		return "GoodPhoto [id=" + id + ", goodId=" + goodId + ", description="
				+ description + ", photo_url=" + photo_url + ", main_url="
				+ main_url + ", head_url=" + head_url + ", tiny_url="
				+ tiny_url + ", update_time=" + update_time + "]";
	}

}

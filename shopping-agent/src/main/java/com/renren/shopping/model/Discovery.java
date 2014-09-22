package com.renren.shopping.model;

import java.util.Date;

public class Discovery {
	private int id;
	private String main_pic_url;
	private String background_pic_url;
	private String title;
	private String buyer_ids;
	private String article;
	private Date create_time;
	private Date update_time;
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "Discovery [id=" + id + ", main_pic_url=" + main_pic_url
				+ ", background_pic_url=" + background_pic_url + ", title="
				+ title + ", buyer_ids=" + buyer_ids + ", article=" + article
				+ "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMain_pic_url() {
		return main_pic_url;
	}
	public void setMain_pic_url(String main_pic_url) {
		this.main_pic_url = main_pic_url;
	}
	public String getBackground_pic_url() {
		return background_pic_url;
	}
	public void setBackground_pic_url(String background_pic_url) {
		this.background_pic_url = background_pic_url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBuyer_ids() {
		return buyer_ids;
	}
	public void setBuyer_ids(String buyer_ids) {
		this.buyer_ids = buyer_ids;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
}

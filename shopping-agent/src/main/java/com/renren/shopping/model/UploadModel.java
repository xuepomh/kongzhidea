package com.renren.shopping.model;

public class UploadModel {

	private String original_url; // 大图
	private String main_url;//
	private String head_url;//
	private String tiny_url;//
	private String type;
	private String name;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UploadModel [original_url=" + original_url + ", main_url="
				+ main_url + ", head_url=" + head_url + ", tiny_url="
				+ tiny_url + ", type=" + type + ", name=" + name + "]";
	}

}

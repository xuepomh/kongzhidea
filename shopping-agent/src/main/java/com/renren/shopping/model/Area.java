package com.renren.shopping.model;

/**
 * 地区表
 * 
 * @author kk
 * 
 */
public class Area {
	private String code;// 编码
	private String name;// 地区名

	public Area() {
		super();
	}

	public Area(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Area [code=" + code + ", name=" + name + "]";
	}

}

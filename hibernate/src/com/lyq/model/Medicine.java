package com.lyq.model;

/**
 * 药品持久化类
 * 
 * @author Li Yong Qiang
 */
public class Medicine {
	private Integer id; // id号
	private String name; // 药品名称
	private double price; // 价格
	private String factoryAdd; // 出厂地址
	private String Description; // 描述

	public Medicine() {
		super();
	}

	public Medicine(Integer id, String name, double price, String factoryAdd,
			String description) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.factoryAdd = factoryAdd;
		Description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getFactoryAdd() {
		return factoryAdd;
	}

	public void setFactoryAdd(String factoryAdd) {
		this.factoryAdd = factoryAdd;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	@Override
	public String toString() {
		return "Medicine [id=" + id + ", name=" + name + ", price=" + price
				+ ", factoryAdd=" + factoryAdd + ", Description=" + Description
				+ "]";
	}

}

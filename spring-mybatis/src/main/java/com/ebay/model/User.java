package com.ebay.model;

public class User {
	private int id;
	private String name;
	private int birthYear;

	public User() {
		super();
	}

	public User(String name) {
		super();
		this.name = name;
	}

	public User(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public User(int id, String name, int birthYear) {
		super();
		this.id = id;
		this.name = name;
		this.birthYear = birthYear;
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

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", birthYear=" + birthYear
				+ "]";
	}

}

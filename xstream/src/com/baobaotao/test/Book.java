package com.baobaotao.test;

import java.util.List;

public class Book {
	int id;
	String name;
	List<Student> stus;

	public Book(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Book() {
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

	@Override
	public boolean equals(Object obj) {
		Book b = (Book) obj;
		if (name.equals(b.getName())) {
			return true;
		}
		return false;
	}

	public List<Student> getStus() {
		return stus;
	}

	public void setStus(List<Student> stus) {
		this.stus = stus;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", stus=" + stus + "]";
	}

}

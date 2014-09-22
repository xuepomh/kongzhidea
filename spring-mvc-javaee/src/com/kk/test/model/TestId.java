package com.kk.test.model;

public class TestId {
	private int id;

	public TestId(int id) {
		super();
		this.id = id;
	}

	public TestId() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TestId [id=" + id + "]";
	}
}

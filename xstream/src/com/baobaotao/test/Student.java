package com.baobaotao.test;

/**
 * Created by IntelliJ IDEA. User: leizhimin Date: 2007-11-23 Time: 10:45:02 学生
 */
public class Student {
	private int age = 0; // 年龄

	public Student() {
		this.age = 1;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Student [age=" + age + "]";
	}

}
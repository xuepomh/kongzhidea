package com.renren.shopping.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class UserDetail {
	private String email;
	private int age;
	private String tel;
	private String qq;
	private String sex;// M表示男，F表示女

	public static final String MALE = "M";
	public static final String FEMALE = "F";

	/**
	 * 设置性别时候 判断是否合法
	 * 
	 * @param sex
	 * @return
	 */
	public static boolean isSexValidate(String sex) {
		if (StringUtils.isBlank(sex)) {
			return false;
		}
		if (MALE.equals(sex) || FEMALE.equals(sex)) {
			return true;
		}
		return false;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "UserDetail [email=" + email + ", age=" + age + ", tel=" + tel
				+ ", qq=" + qq + ", sex=" + sex + "]";
	}

}

package com.baobaotao.oxm.xstream.alias;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import com.baobaotao.domain.LoginLog;
import com.baobaotao.domain.User;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamAliasSample {
	private static XStream xstream;
	static {
		xstream = new XStream(new DomDriver());
		// 重新设置包名
		// xstream.aliasPackage("c", "com.baobaotao");
		// 设置类名 默认为当前类名加上包名
		xstream.alias("loginLog", LoginLog.class);
		xstream.alias("user", User.class);
		// 设置类成员别名
		xstream.aliasField("id1", User.class, "userId");

		// 将userId设置为xml属性，默认为xml元素
		xstream.useAttributeFor(LoginLog.class, "userId");
		// 设置属性别名
		xstream.aliasAttribute(LoginLog.class, "userId", "id2");

		// 去掉集合类型生成xml的父节点
		xstream.addImplicitCollection(User.class, "logs");
	}

	public static User getUser() {
		LoginLog log1 = new LoginLog();
		LoginLog log2 = new LoginLog();
		log1.setIp("192.168.1.91");
		log1.setLoginDate(new Date());
		log2.setIp("192.168.1.92");
		log2.setLoginDate(new Date());
		User user = new User();
		user.setUserId(1);
		user.setUserName("xstream");
		user.addLoginLog(log1);
		user.addLoginLog(log2);
		return user;
	}

	/**
     */
	public static void objectToXml() throws Exception {
		User user = getUser();
		FileOutputStream fs = new FileOutputStream(
				"out\\XStreamAliasSample.xml");
		xstream.toXML(user, fs);
	}

	/**
     */
	public static User xmlToObject() throws Exception {
		FileInputStream fis = new FileInputStream("out\\XStreamAliasSample.xml");
		User u = (User) xstream.fromXML(fis);
		for (LoginLog log : u.getLogs()) {
			if (log != null) {
				System.out.println("IP: " + log.getIp());
				System.out.println("Date: " + log.getLoginDate());
			}
		}
		return u;
	}

	public static void main(String[] args) throws Exception {
		objectToXml();
		xmlToObject();
	}
}

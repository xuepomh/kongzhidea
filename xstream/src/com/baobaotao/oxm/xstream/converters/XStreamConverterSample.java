package com.baobaotao.oxm.xstream.converters;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;
import com.baobaotao.domain.LoginLog;
import com.baobaotao.domain.User;
import com.baobaotao.utils.ResourceUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamConverterSample {
	public static String FILE_NAME = "";
	private static XStream xstream;
	private static User u;
	static {
		xstream = new XStream(new DomDriver());
		// 设置类名 默认为当前类名加上包名
		xstream.alias("loginLog", LoginLog.class);
		xstream.alias("user", User.class);
		// 将userId设置为xml属性，默认为xml元素
		xstream.aliasField("id", User.class, "userId");
		xstream.useAttributeFor(User.class, "userId");

		// 去掉集合类型生成xml的父节点
		xstream.addImplicitCollection(User.class, "logs");
		// 设置自定义date转换器
		xstream.registerConverter(new DateConverter(Locale.SIMPLIFIED_CHINESE));
	}

	public static void initUser() {
		LoginLog log1 = new LoginLog();
		LoginLog log2 = new LoginLog();
		log1.setIp("192.168.1.91");
		log1.setLoginDate(new Date());
		log2.setIp("192.168.1.92");
		log2.setLoginDate(new Date());
		u = new User();
		u.setUserId(1);
		u.setUserName("xstream");
		u.addLoginLog(log1);
		u.addLoginLog(log2);
	}

	/**
	 */
	public static void objectToXml() throws Exception {
		initUser();
		FileOutputStream fs = new FileOutputStream(FILE_NAME);
		OutputStreamWriter writer = new OutputStreamWriter(fs,
				Charset.forName("UTF-8"));
		xstream.toXML(u, writer);
	}

	/**
	 */
	public static User xmlToObject() throws Exception {
		FileInputStream fis = new FileInputStream(FILE_NAME);
		InputStreamReader reader = new InputStreamReader(fis,
				Charset.forName("UTF-8"));
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
		FILE_NAME = "out/XStreamConverterSample.xml";
		objectToXml();
		xmlToObject();
	}
}

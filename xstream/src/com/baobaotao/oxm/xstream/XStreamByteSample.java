package com.baobaotao.oxm.xstream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.tools.ant.taskdefs.condition.Os;

import com.baobaotao.domain.LoginLog;
import com.baobaotao.domain.User;
import com.baobaotao.utils.ResourceUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamByteSample {
	private static XStream xstream;
	static {
		xstream = new XStream(new DomDriver());
	}
	private static byte[] b;

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
		Map<String, String> mat = new HashMap<String, String>();
		mat.put("id", "1");
		mat.put("name", "kk");
		user.setDatas(mat);
		return user;
	}

	public static User objectToXml() throws Exception {
		User user = getUser();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		xstream.toXML(user, outStream);
		b = outStream.toByteArray();
		return user;
	}

	public static User xmlToObject() throws Exception {
		InputStream fis = new ByteArrayInputStream(b, 0, b.length);
		User u = (User) xstream.fromXML(fis);
		for (LoginLog log : u.getLogs()) {
			if (log != null) {
				System.out.println("IP: " + log.getIp());
				System.out.println("logDate: " + log.getLoginDate());
			}
		}
		return u;
	}

	public static void main(String[] args) throws Exception {
		objectToXml();
		xmlToObject();
		System.out.println(new String(b, "utf-8"));
	}
}

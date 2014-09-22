package com.baobaotao.oxm.xstream;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.baobaotao.domain.LoginLog;
import com.baobaotao.domain.User;
import com.baobaotao.utils.ResourceUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamSample {
	private static XStream xstream;
	static {
		xstream = new XStream(new DomDriver());
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
		Map<String,String> mat = new HashMap<String,String>();
		mat.put("id","1");
		mat.put("name","kk");
		user.setDatas(mat);
		return user;
	}

	public static User objectToXml() throws Exception {
		User user = getUser();
		FileOutputStream outputStream = new FileOutputStream("out\\XStreamSample1.xml");
		xstream.toXML(user, outputStream);
		return user;
	}

	public static User xmlToObject() throws Exception {
		FileInputStream fis = new FileInputStream("out\\XStreamSample1.xml");
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
	}
}

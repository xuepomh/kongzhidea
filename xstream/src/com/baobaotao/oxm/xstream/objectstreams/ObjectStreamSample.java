package com.baobaotao.oxm.xstream.objectstreams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Locale;

import com.baobaotao.domain.LoginLog;
import com.baobaotao.domain.User;
import com.baobaotao.oxm.xstream.XStreamSample;
import com.baobaotao.oxm.xstream.annotations.XStreamAnnotationSample;
import com.baobaotao.oxm.xstream.converters.DateConverter;
import com.baobaotao.utils.ResourceUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.*;

/**
 * 流化对象，用户序列化和反序列化操作
 * 
 * @author kk
 * 
 */
public class ObjectStreamSample {
	private static XStream xstream;
	static {
		xstream = new XStream();
	}

	public User xmlToObject() throws Exception {
		FileReader fr = new FileReader("out\\ObjectStreamSample.xml");
		BufferedReader br = new BufferedReader(fr);
		// 创建对象输入流
		ObjectInputStream input = xstream.createObjectInputStream(br);
		User user = (User) input.readObject();
		return user;

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

	public void objectToXml() throws Exception {
		User user = getUser();
		PrintWriter pw = new PrintWriter("out\\ObjectStreamSample.xml");

		PrettyPrintWriter ppw = new PrettyPrintWriter(pw);
		// 创建对象输出流
		ObjectOutputStream out = xstream.createObjectOutputStream(ppw);
		out.writeObject(user);
		out.close();
	}

	public static void main(String[] args) throws Exception {
		ObjectStreamSample converter = new ObjectStreamSample();
		converter.objectToXml();
		User u = converter.xmlToObject();
		for (LoginLog log : u.getLogs()) {
			if (log != null) {
				System.out.println("IP: " + log.getIp());
				System.out.println("Date: " + log.getLoginDate());
			}
		}

	}
}

package com.baobaotao.oxm.xstream.annotations;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Date;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
public class XStreamAnnotationSample {
	private static XStream xstream;
	static{
	    xstream = new XStream(new DomDriver());
	    //判断需要转换类型
	    xstream.processAnnotations(User.class);
		xstream.processAnnotations(LoginLog.class);
		//自动加载注解bean
//		xstream.autodetectAnnotations(true); 
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
		user.setLastIp("1.1.1.1");
		user.setUserName("xstream");
		user.addLoginLog(log1);
		user.addLoginLog(log2);
		return user;
	}
	 /**
     */
    public static void objectToXml()throws Exception{
    	User user = getUser();
        FileOutputStream fs = new FileOutputStream("out\\XStreamAnnotationSample.xml");
        OutputStreamWriter writer = new OutputStreamWriter(fs, Charset.forName("UTF-8"));    
        xstream.toXML(user, writer);
    }
    
    /**
     */
    public static User xmlToObject()throws Exception{
    	FileInputStream fis = new FileInputStream("out\\XStreamAnnotationSample.xml");
    	InputStreamReader reader = new InputStreamReader(fis, Charset.forName("UTF-8")); 
        User user = (User) xstream.fromXML(fis);
        for(LoginLog log : user.getLogs()){
        	if(log!=null){
        		System.out.println("IP: " + log.getIp());
        		System.out.println("Date: " + log.getLoginDate());
            }
        }
        return user;
    }

    public static void main(String[] args)throws Exception {
        objectToXml();
        xmlToObject();
    }
}

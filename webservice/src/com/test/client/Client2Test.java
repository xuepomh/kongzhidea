package com.test.client;

import java.net.MalformedURLException;

import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.spring.example.Echo;


public class Client2Test {
	public static void main(String args[]) throws MalformedURLException {
		Service service = new ObjectServiceFactory()
				.create(Echo.class);
		XFireProxyFactory factory = new XFireProxyFactory(XFireFactory
				.newInstance().getXFire());
		String url = "http://localhost:8080/webservice/EchoService";
		Echo echo = (Echo) factory.create(service,
				url);
		System.out.println(echo.echo("ss"));
	}
}
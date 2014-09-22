package com.test.service;

import com.test.service.bean.User;

public class HelloService implements IHelloService {
	public String sayHello(String name) {
		return "Hello, " + name;
	}

	public User getUser(User user) {
		User userNew = new User();
		userNew.setId("new:" + user.getId());
		userNew.setName("new:" + user.getName());
		return userNew;
	}
}
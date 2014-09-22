package com.renren.kk.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class T3Publisher {
	// 发布者和订阅者使用同一个 exchange
	public static final String EXCHANGE = Account.EXCHANGE;

	public static void main(String[] args) throws IOException {
		// 创建链接工厂
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Account.HOST);
		factory.setPort(Account.PORT);
		factory.setVirtualHost(Account.VIRTUALHOST);
		factory.setUsername(Account.USERNAME);
		factory.setPassword(Account.PASSWORD);

		// 创建链接
		Connection connection = factory.newConnection();
		// 创建消息管道
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE, "fanout");
		for (int i = 0; i < 10; i++) {
			String msg = "hello worrd! " + i;
			// 向所有队列广播
			channel.basicPublish(EXCHANGE, "", null, msg.getBytes("utf-8"));
			System.out.println(i);
		}

		channel.close();

		System.exit(0);
	}
}

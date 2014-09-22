package com.renren.kk.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 路由模式，不是想全部广播，只向监听的队列发送广播
 * 
 * @author kk
 * 
 */
public class T4EmitLogDirect {

	public static final String QUEUE = Account.ROUTE;
	public static final String EXCHANGE = Account.EXCHANGE_R;

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
		channel.exchangeDeclare(EXCHANGE, "direct");

		for (int i = 0; i < 10; i++) {
			String msg = "hello worrd! " + i;
			// 向所有队列广播
			channel.basicPublish(EXCHANGE, QUEUE, null, msg.getBytes("utf-8"));
		}

		channel.close();

		System.exit(0);
	}
}

package com.renren.kk.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 广播模式，如果消费者没有收到则抛弃
 * 
 * 所有同一个EXCHANGE都能收到 广播的消息
 * 
 * @author kk
 * 
 */
public class T3Subscribe {

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
		String queueName = channel.queueDeclare().getQueue();
		// 如果指定 queueName，并且queueBind
		// 绑定改队列到exchange上后，publish的消息会保存到该队列中，否则消息则抛弃，但是消息存在一个队列中，如果存在多个客户端，则会轮流取数据
		// String queueName = "MEMEDA";
		channel.queueBind(queueName, EXCHANGE, "");

		System.out.println(" [*] Waiting for messages: " + queueName
				+ " . To exit press CTRL+C");

		// 声明一个消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		// 是否自动回复一个ACK basicQos时候会用到
		boolean autoACK = true;
		channel.basicConsume(queueName, autoACK, consumer);

		while (true) {
			// 循环获取信息
			QueueingConsumer.Delivery delivery;
			try {
				// delivery = consumer.nextDelivery();
				delivery = consumer.nextDelivery(5000);// 可以设置超时时间
				if (delivery != null) {
					String message = new String(delivery.getBody());
					System.out.println("Revieve activate msg:" + message);
				} else {
					System.err.println("no message in mq!");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}

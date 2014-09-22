package com.renren.kk.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 路由模式
 * 
 * @author kk
 * 
 */
public class T5ReceiveLogsDirect {

	public static final String QUEUEKEY = Account.EXCHANGE_RKEY;
	public static final String EXCHANGE = Account.EXCHANGE_RLOG;

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

		channel.exchangeDeclare(EXCHANGE, "topic");
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE, QUEUEKEY);

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
					String routingKey = delivery.getEnvelope().getRoutingKey();
					System.out.println("Revieve activate msg:" + message
							+ "; routingKey:" + routingKey);
				} else {
					System.err.println("no message in mq!");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}

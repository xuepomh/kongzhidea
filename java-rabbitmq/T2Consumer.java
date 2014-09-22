package com.renren.kk.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 如果在队列声明多个消费者，那么队列将盲目的把消息依次 放到队列中。
 * 
 * It just blindly dispatches every n-th message to the n-th consumer.
 * 
 * @author kk
 * 
 */
public class T2Consumer {

	public static final String QUEUE = Account.QUEUE;

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
		// ............
		// 为了解决各个接收端工作量相差太大的问题（有的一直busy，有的空闲比较多），突破Round-robin。 int
		// prefetchCount = 1; channel.basicQos(prefetchCount);
		// 意思为，最多为当前接收方发送一条消息。如果接收方还未处理完毕消息，还没有回发确认，就不要再给他分配消息了，应该把当前消息分配给其它空闲接收方
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);
		// ...............
		// 声明一个持久化的消息队列
		channel.queueDeclare(QUEUE, true, false, false, null);

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		// 声明一个消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		// 是否自动回复一个ACK basicQos时候会用到
		boolean autoACK = true;
		channel.basicConsume(QUEUE, autoACK, consumer);

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

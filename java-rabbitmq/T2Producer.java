package com.renren.kk.rabbitmq;

import java.io.IOException;

import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 5：Message durability(消息持久化)
 * 我们已经学习了如何确保消费者死亡，任务也不会丢失。但是一旦我们的RabbitMQ服务停止，任务还是同样会丢失。
 * 如果你不告诉RabbitMQ，当它退出或发生灾难关闭时，它会丢失队列里的消息。我们需要做两件事情来确保消息不会丢失：我们需要同时保证队列和消息时持久的。
 * 首先：我们需要保证RabbitMQ不会丢失我们的队列。为了实现这个目的，我们需要将队列申明为永久的： boolean durable = true;
 * channel.queueDeclare("hello", durable, false, false, null);
 * 即便命令执行正常，在我们现有的程序中
 * ，它也不会起作用。那是因为我们已经存在一个非永久的队列“hello”。RabbitMQ不允许我们使用不同的参数来定义一个已经存在的队列
 * ，它会向我们返回一些错误信息。但是，通过使用不同的名字(eg：task_queue)，来定义一个新的队列，可以快速的解决这个问题。 boolean
 * durable = true; channel.queueDeclare("task_queue", durable, false, false,
 * null); queueDeclare方法申明队列的变动，需要保证同时被应用在生产者和消费者两方
 * 通过这种方式，我们可以确保task_queue队列在RabbitMQ服务重启后也不会丢失
 * 。现在我们需要把我们的消息也标记为永久的。通过设置MessageProperties
 * (实现了BasicProperties)的值为PERSISTENT_TEXT_PLAIN可以实现这一目的。
 * channel.basicPublish("","task_queue",
 * MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes()); 消息持久化注意事项
 * 
 * 即便我们将消息标记为永久的，但这也不能完全保证消息就一定不会丢失。虽然RabbitMQ会将消息保存到硬盘上，
 * 但是在RabbitMQ接收到消息并且将其保存到硬盘上之间，仍然有一个短暂的时间片段。
 * 
 * @author kk
 * 
 */
public class T2Producer {

	// 生产者和消费者 使用同一个QUEUE
	public static final String QUEUE = Account.QUEUE;

	// public static final String QUEUE = "consumer";

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
		// 声明一个持久化的消息队列
		channel.queueDeclare(QUEUE, true, false, false, null);

		for (int i = 0; i < 10; i++) {
			String msg = "hello worrd! " + i;
			channel.basicPublish("", QUEUE, null, msg.getBytes("utf-8"));
		}

		channel.close();

		System.exit(0);
	}
}

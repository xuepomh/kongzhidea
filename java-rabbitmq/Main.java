package com.renren.kk.rabbitmq;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws Exception {
		// 生产者和消费者使用同一个队列 才能收到消息
		// 可以开多个消费者和生产者 并行处理
		QueueConsumer consumer = new QueueConsumer("queue");
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		Producer producer = new Producer("queue");

		for (int i = 0; i < 10; i++) {
			HashMap<String, Integer> message = new HashMap<String, Integer>();
			message.put("message number", i);
			producer.sendMessage(message);
			System.out.println("Message Number " + i + " sent.");
		}
		producer.close();
	}
}
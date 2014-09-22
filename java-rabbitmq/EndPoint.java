package com.renren.kk.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Represents a connection with a queue
 * 
 * @author syntx
 * 
 */
public abstract class EndPoint {

	protected Channel channel;
	protected Connection connection;
	protected String endPointName;

	public static final boolean durable = true; // 消息队列持久化

	public EndPoint(String endpointName) throws IOException {
		this.endPointName = endpointName;

		// Create a connection factory
		ConnectionFactory factory = new ConnectionFactory();

		// hostname of your rabbitmq server
		// localhost guest guest 只能用于本机，如果想使用Ip访问，需要再新建用户，并添加VirtualHost(默认为/)
		factory.setHost(Account.HOST);
		factory.setPort(Account.PORT);
		factory.setVirtualHost(Account.VIRTUALHOST);
		factory.setUsername(Account.USERNAME);
		factory.setPassword(Account.PASSWORD);

		// getting a connection
		connection = factory.newConnection();

		// creating a channel
		channel = connection.createChannel();

		// declaring a queue for this channel. If queue does not exist,
		// it will be created on the server.
		// durable 持久化队列
		channel.queueDeclare(endpointName, durable, false, false, null);
	}

	/**
	 * 关闭channel和connection。并非必须，因为隐含是自动调用的。
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.channel.close();
		this.connection.close();
	}
}
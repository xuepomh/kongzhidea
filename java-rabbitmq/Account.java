package com.renren.kk.rabbitmq;

public class Account {
	public static final String HOST = "10.2.45.39";
	public static final String VIRTUALHOST = "memeda";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "admin";
	public static final int PORT = 5672;
	public static final String QUEUE = "MEMEDA";
	public static final String EXCHANGE = "MEMEDA_EXCHANGE";
	public static final String ROUTE = "MEMEDA_ROUTE";
	public static final String EXCHANGE_R = "MEMEDA_EXCHANGE_R";
	public static final String EXCHANGE_MEMEDA_LOG = "EXCHANGE_MEMEDA_LOG";
	public static final String EXCHANGE_RKEY = "*.EXCHANGE_RKEY.*";
	public static final String EXCHANGE_RLOG = "EXCHANGE_RLOG";
}

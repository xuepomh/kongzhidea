package com.rr.publik.client.zk.pool;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryNTimes;

public class ZKClient {
	public static Log logger = LogFactory.getLog(ZKClient.class);
	private static CuratorFramework client;

	private static String zkHosts = "10.4.28.172:2181,10.4.28.179:2181";

	// 命名空间 zkClient下所有的data都在该地址下 zk中使用 '/' 可以指定目录结构
	private static String namespace = "thrift";

	static {
		try {
			logger.info("zkHosts: " + zkHosts);
			client = CuratorFrameworkFactory
					.builder()
					.connectString(zkHosts)
					.namespace(namespace)
					.retryPolicy(
							new RetryNTimes(Integer.MAX_VALUE, 5 * 60 * 1000))
					.connectionTimeoutMs(5000).build();
		} catch (IOException e) {
			logger.error("get client error", e);
		}
		client.start();
	}

	public static CuratorFramework getClient() {
		return client;
	}

	public static void destroy() throws Exception {
		client.close();
	}

}

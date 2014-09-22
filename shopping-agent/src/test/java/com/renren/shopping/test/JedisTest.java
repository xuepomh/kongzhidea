package com.renren.shopping.test;

import java.util.ArrayList;

import java.util.List;

import redis.clients.jedis.JedisPoolConfig;

import redis.clients.jedis.JedisShardInfo;

import redis.clients.jedis.ShardedJedis;

import redis.clients.jedis.ShardedJedisPool;

import redis.clients.util.Hashing;

import redis.clients.util.Sharded;

public class JedisTest {

	static ShardedJedisPool pool;

	static {

		JedisPoolConfig config = new JedisPoolConfig();// Jedis池配置

		config.setMaxActive(500);// 最大活动的对象个数

		config.setMaxIdle(1000 * 60);// 对象最大空闲时间

		config.setMaxWait(1000 * 10);// 获取对象时最大等待时间

		config.setTestOnBorrow(true);

		String hostA = "10.3.20.160";

		int portA = 6379;

		String hostB = "10.4.25.89";

		int portB = 6379;

		List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>(2);

		JedisShardInfo infoA = new JedisShardInfo(hostA, portA);

		infoA.setPassword("whoareyou");

		JedisShardInfo infoB = new JedisShardInfo(hostB, portB);

		infoB.setPassword("whoareyou");

		jdsInfoList.add(infoA);

		jdsInfoList.add(infoB);

		pool = new ShardedJedisPool(config, jdsInfoList, Hashing.MURMUR_HASH,

		Sharded.DEFAULT_KEY_TAG_PATTERN);

	}

	/**
	 * 
	 * @param args
	 */

	public static void main(String[] args) {

		for (int i = 0; i < 100; i++) {

			String key = generateKey();

			// key += "{aaa}";

			ShardedJedis jds = null;

			try {

				jds = pool.getResource();

				System.out.println(key + ":"
						+ jds.getShard(key).getClient().getHost());

//				System.out.println(jds.set(key, "11"));
				System.out.println(jds.get(key));

			} catch (Exception e) {

				e.printStackTrace();

			}

			finally {

				pool.returnResource(jds);

			}

		}

	}

	private static int index = 1;

	public static String generateKey() {

		return String.valueOf("s_" + Thread.currentThread().getId()) + "_"
				+ (index++);

	}
}

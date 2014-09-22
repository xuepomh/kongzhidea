package com.renren.shopping.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.renren.shopping.conf.KVConf;
import com.renren.shopping.util.RedisAPI;
import com.renren.shopping.util.RedisUtil;

public class TestCase {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		System.out.println(RedisAPI.lrange(
				KVConf.HOT_SEARCH_WORD_KEY, 0, 1));
		System.out.println(RedisAPI.zrevrange(
				KVConf.TASK_GRATUITY_SORT, 0, 1));
		System.out.println(RedisAPI.zrevrank(
				KVConf.TASK_GRATUITY_SORT, "4"));
	}

	@Test
	public void test2() {
		// String phone = "18612566582";
		// System.out.println(RedisAPI.get(
		// KVConf.getPhoneRegisterKey(phone)));
		// System.out.println(RedisAPI.ttl(
		// KVConf.getPhoneRegisterKey(phone)));
		//
		// System.out.println(RedisAPI.get(
		// KVConf.getPhoneRegisterTimeKey(phone)));
		// System.out.println(RedisAPI.ttl(
		// KVConf.getPhoneRegisterTimeKey(phone)));
	}

}

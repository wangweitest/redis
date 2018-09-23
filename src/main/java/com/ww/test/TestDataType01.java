package com.ww.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * 测试基本的数据类型
 * 
 * @author Administrator
 *
 */
public class TestDataType01 {

	// 在redis的配置文件中的bind属性中增加192.168.25.131
	Jedis jedis = new Jedis("192.168.25.131", 6379);

	@Test
	public void test01() {
		// 查看服务是否运行，打出pong表示OK
		System.out.println("connection is OK==========>: " + jedis.ping());

	}

	@Test
	public void test02() {
		// key
		Set<String> keys = jedis.keys("*");
		for (String string : keys) {
			System.out.println(string);
		}
	}

	@Test
	public void test03() {
		// String
		// append实在value后追加，set是覆盖value
		jedis.append("k2", "v2");
		System.out.println(jedis.get("k2"));

		jedis.set("k3", "v4");
		System.out.println(jedis.get("k3"));

		// Redis Mset 命令用于同时设置一个或多个 key-value 对。
		jedis.mset("str1", "v1", "str2", "v2", "str3", "v3");
		System.out.println(jedis.mget("str1", "str2", "str3"));
	}

	@Test
	public void test04() {
		// list
		jedis.lpush("mylist", "v1", "v2", "v3", "v4", "v5");
		List<String> list = jedis.lrange("mylist", 0, -1);
		for (String element : list) {
			System.out.println(element);
		}
	}

	@Test
	public void test05() {
		// set
		jedis.sadd("orders", "jd001");
		jedis.sadd("orders", "jd002");
		jedis.sadd("orders", "jd003");
		jedis.sadd("orders", "jd003");
		Set<String> set1 = jedis.smembers("orders");
		for (Iterator iterator = set1.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);
		}
		// 删除
		jedis.srem("orders", "jd002");
		System.out.println(jedis.smembers("orders").size());

	}

	@Test
	public void test06() {
		// hash
		jedis.hset("hash1", "userName", "lisi");
		System.out.println(jedis.hget("hash1", "userName"));
		Map<String, String> map = new HashMap<String, String>();
		map.put("telphone", "13811814763");
		map.put("address", "atguigu");
		map.put("email", "abc@163.com");
		jedis.hmset("hash2", map);
		List<String> result = jedis.hmget("hash2", "telphone", "email");
		for (String element : result) {
			System.out.println(element);
		}
	}

	@Test
	public void test07() {
		// zset
		//值一样时后添加的覆盖之前添加的，分数一样时，在score相同的情况下，redis使用字典排序
		//到底什么是字典排序？
		//怎么使用自定义排序
		jedis.zadd("zset02", 60d, "v1");
		jedis.zadd("zset02", 60d, "2");
		jedis.zadd("zset02", 60d, "3");
		jedis.zadd("zset02", 60d, "d");
		jedis.zadd("zset02", 60d, "！");
		jedis.zadd("zset02", 60d, "￥");

		Set<String> s1 = jedis.zrange("zset02", 0, -1);
		for (Iterator iterator = s1.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			System.out.println(string);
		}
	}
	
	/**
	 * 两个字符串是怎么比较大小的：按照字符Unicode编码大小从第一个往后比较
	 * Unicode编码包含所有字符，所以都可以比较
	 */
	@Test
	public void test08() {
		String aaa = "1";
		String bbb = "￥";
		System.out.println(aaa.compareTo(bbb));
		
		System.out.println((int)'1');
		System.out.println((int)'￥');

		
	}
	
}

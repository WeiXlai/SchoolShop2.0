package com.weilai.o2o.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * spring-redis.xml的配置
 * @author ASUS
 * redis.hostname=39.107.100.210
redis.port=6379
redis.database=0
redis.pool.maxActive=600
redis.pool.maxIdle=300
redis.pool.maxWait=3000
redis.pool.testOnBorrow=true
 *
 */

import com.weilai.o2o.cache.JedisPoolWriper;
import com.weilai.o2o.cache.JedisUtil;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
	@Value("${redis.hostname}")
	private String hostname;
	@Value("${redis.port}")
	private int port;

	@Value("${redis.pool.maxActive}")
	private int maxTotal;
	@Value("${redis.pool.maxIdle}")
	private int maxIdle;
	@Value("${redis.pool.maxWait}")
	private long maxWaitMillis;
	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;
	
	//连接池的设置
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	//创建连接池，并做相关配置
	@Autowired
	private JedisPoolWriper jedisWritePool;
	//Redis的工具类
	@Autowired
	private JedisUtil jedisUtil;
	
	/**
	 * 创建Redis连接池的配置
	 * @return
	 */
	@Bean(name ="jedisPoolConfig")
	public JedisPoolConfig createJedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		//控制一个pool可分配多少个jedis实例
		jedisPoolConfig.setMaxTotal(maxTotal);
		//连接池中最多可空闲maxIdle个连接，这里取值为20
		//表示即使没有数据库连接时依然可以保持20空闲的连接
		//而不清除，随时处于待命状态
		jedisPoolConfig.setMaxIdle(maxIdle);
		//最大等待时间，当没有可用连接时
		//连接池等待连接被归还的最大时间（以毫秒技术），超过时间则跳出异常
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		//在获取连接的时候检查有效性
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		return jedisPoolConfig;
		
	}
	
	/**
	 * 创建Redis连接池，并做相关配置
	 * @return
	 */
	@Bean(name ="jedisWritePool")
	public JedisPoolWriper createJedisPoolWriper() {
		JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostname, port);
		return jedisPoolWriper;
	}
	
	/**
	 * 创建Redis工具类，封装好Redis的连接已进行相关操作
	 * @return
	 */
	@Bean(name ="jedisUtil")
	public JedisUtil createJedisUtil() {
		JedisUtil jedisUtil = new JedisUtil();
		jedisUtil.setJedisPool(jedisWritePool);
		return jedisUtil;
	}
	
	/**
	 * Redis的key操作
	 * @return
	 */
	@Bean(name ="jedisKeys")
	public JedisUtil.Keys createKeys(){
		JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
		return jedisKeys;
	}
	/**
	 * Redis的key操作
	 * @return
	 */
	@Bean(name ="jedisStrings")
	public JedisUtil.Strings createStrings(){
		JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
		return jedisStrings;
	}
}

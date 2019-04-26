package com.weilai.o2o.config.dao;
/**
 * 配置DataSource到spring IOC容器中
 * @author ASUS
 *
 */

import java.beans.PropertyVetoException;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.weilai.o2o.utils.DESUtil;

@Configuration
//配置 mybatis mapper的扫描路径
@MapperScan("com.weilai.o2o.dao")
public class DataSourceConfiguration {
	/**
	 * 通过这些@Value标签就可以在application.properties中读出数据库的配置信息
	 */
	@Value("${jdbc.driver}")
	private String jdbcDriver;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUsername;
	@Value("${jdbc.password}")
	private String jdbcPassword;
	
	/**
	 * 生成与spring-dao.xml对应的bean dataSource
	 * <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<!-- 配置连接池属性 -->
		<property name="driverClass" value="${jdbc.driver}" />
		<property name="jdbcUrl" value="${jdbc.url}" />
		<property name="user" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />

		<!-- c3p0连接池的私有属性 -->
		<property name="maxPoolSize" value="30" />
		<property name="minPoolSize" value="10" />
		<!-- 关闭连接后不自动commit -->
		<property name="autoCommitOnClose" value="false" />
		<!-- 获取连接超时时间 -->
		<property name="checkoutTimeout" value="10000" />
		<!-- 当获取连接失败重试次数 -->
		<property name="acquireRetryAttempts" value="2" />
	</bean>
	 * @return
	 * @throws PropertyVetoException
	 */
	@Bean(name = "dataSource")
	public ComboPooledDataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		//设置驱动
		dataSource.setDriverClass(jdbcDriver);
		//设置连接数据库的URL
		dataSource.setJdbcUrl(jdbcUrl);
		//设置用户名,要解密
		dataSource.setUser(DESUtil.getDecryptString(jdbcUsername));
		//设置密码,要解密
		dataSource.setPassword(DESUtil.getDecryptString(jdbcPassword));
		//c3p0连接池的私有属性
		dataSource.setMaxPoolSize(30);
		dataSource.setMinPoolSize(10);
		//关闭连接后不自动连接commit
		dataSource.setAutoCommitOnClose(false);
		//获取连接超时时间
		dataSource.setCheckoutTimeout(10000);
		//当连接失败重试次数
		dataSource.setAcquireRetryAttempts(2);
		
		
		return dataSource;
	}
	
	
}

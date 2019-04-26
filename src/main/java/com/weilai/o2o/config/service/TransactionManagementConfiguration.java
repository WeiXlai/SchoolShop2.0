package com.weilai.o2o.config.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
/**
 * 对标spring-service.xml里面的transactionManageer
 * 实现TransactionManagementConfigurer是因为开启annotation-driven
 * @author ASUS
 *
 */
@Configuration
//首先使用注解@@EnableTransactionManagement开启事务支持后
//在service方法上添加注解@Transactional便可，相当于配置基于注解的声明式事务：<tx:annotation-driven transaction-manager="transactionManage jgbr" />
@EnableTransactionManagement
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {
	@Autowired
	//注入DataSourceConfiguretion里边的dataSource，通过createDataSource()获取
	DataSource dataSource;
	
	/**
	 * 关于事务管理，需要返回PlatformTransactionManager的实现
	 */
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		// TODO Auto-generated method stub
		return new DataSourceTransactionManager(dataSource);
	}

}

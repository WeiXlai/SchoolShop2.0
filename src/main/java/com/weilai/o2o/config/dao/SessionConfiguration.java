package com.weilai.o2o.config.dao;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
public class SessionConfiguration {
	@Autowired
	DataSource dataSource;
	
	//mybatis-config.xml配置文件的路径
	private static String mybatisConfigFile;
	
	@Value("${mybatis_config_file}")
	public void setMybatisConfigFile(String mybatisConfigFile) {
		SessionConfiguration.mybatisConfigFile = mybatisConfigFile;
	}
	//mybatis mapper文件所在路径  即扫描sql配置文件:mapper需要的xml文件
	private static String mapperPath;
	
	@Value("${mybatis_path}")
	public void setMapperPath(String mapperPath) {
		SessionConfiguration.mapperPath = mapperPath;
	}
	//扫描entity包 使用别名
	@Value("${type_alias_packer}")
	private String typeAliasPackage;
	
	/**
	 * 创建SQLSessionFactoryBean实例
	 *  设置configuration 设置mapper映射路径 设置dataSource数据源
	 * @return
	 * @throws IOException
	 */
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactoryBean createsqlSessionFactory() throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		//设置mybatis configuration 扫描路径
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
		//设置typeAlias 包扫描路径
		sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);
		//添加mapper扫描路径
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+mapperPath;
		sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
		//设置dataSource
		sqlSessionFactoryBean.setDataSource(dataSource);
		return sqlSessionFactoryBean;
	}
	
	
}

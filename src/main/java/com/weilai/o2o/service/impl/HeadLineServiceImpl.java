package com.weilai.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weilai.o2o.cache.JedisUtil;
import com.weilai.o2o.dao.HeadLineDao;
import com.weilai.o2o.entity.HeadLine;
import com.weilai.o2o.exceptions.AreaOperationException;
import com.weilai.o2o.exceptions.HeadLineOperationException;
import com.weilai.o2o.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService {
	@Autowired
	HeadLineDao headLineDao;
	
	@Autowired
	JedisUtil.Keys jedisKeys;
	
	@Autowired
	JedisUtil.Strings jedisStrings;
	
	private static String HEADLINELISTKEY = "headLineList";
	private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);
	
	/**
	 * 通过头条的一些条件查询头条列表
	 */
	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
		//定义Redis的key前缀
		String key = HEADLINELISTKEY;
		List<HeadLine> headLineList = null;
		ObjectMapper mapper = new ObjectMapper();
		//拼接出Redis的key
		if (headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		//若Redis不存在key
		if (!jedisKeys.exists(key)) {
			headLineList = headLineDao.queryHeadLineList(headLineCondition);
			String jsonString = null;
			//将List<HeadLine>对象转换成string
			try {
				jsonString = mapper.writeValueAsString(headLineList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());	
			}
			//将string对象设置进Redis
			jedisStrings.set(key, jsonString);
		}
		else {
			String jsonString = jedisStrings.get(key);
			//将list对象转换成headline对象
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
			try {
				headLineList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());
			}
		}
		
		return headLineList;
	}

}

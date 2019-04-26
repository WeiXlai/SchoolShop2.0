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
import com.weilai.o2o.dao.AreaDao;
import com.weilai.o2o.entity.Area;
import com.weilai.o2o.exceptions.AreaOperationException;
import com.weilai.o2o.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService {
	@Autowired
	AreaDao areaDao;
	/**
	 * JedisUtil下的一个Keys类
	 */
	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Autowired
	private JedisUtil.Strings jedisStrings;
	
	//private static String AREALISTKEY = "arealist";
	private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);
	
	
	@Override
	public List<Area> getAreaList() {
		//key
		String key = AREALISTKEY;
		//用来接收areaDao.queryArea()返回的集合
		List<Area> areaList = null;
		ObjectMapper objectMapper = new ObjectMapper();
		//如果Redis中不存在key是arealist
		if (!jedisKeys.exists(key)) {
			//数据库获取区域列表
			areaList = areaDao.queryArea();
			//接收list转换成String类型
			String jsonString = null;
			try {
				//将list转成string
				jsonString = objectMapper.writeValueAsString(areaList);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new AreaOperationException(e.getMessage());	
			}
			//将key设置进Redis中
			jedisStrings.set(key, jsonString);
			
		}else { //Redis存在key，取出
			//将String转换成list
			String jsonString = jedisStrings.get(key);
			//将list对象转换成area对象
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
			try {
				areaList = objectMapper.readValue(jsonString, javaType);
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

		return areaList;
	}

}

package com.weilai.o2o.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weilai.o2o.cache.JedisUtil;
import com.weilai.o2o.cache.JedisUtil.Keys;
import com.weilai.o2o.dao.ShopCategoryDao;
import com.weilai.o2o.entity.ShopCategory;
import com.weilai.o2o.exceptions.ShopCategoryOperationException;
import com.weilai.o2o.service.ShopCategoryService;
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
	@Autowired
	ShopCategoryDao shopCategoryDao;
	
	@Autowired
	JedisUtil.Keys jedisKeys;
	
	@Autowired
	JedisUtil.Strings jedisStrings;
	
	private static String SHOPCATEGORYLIST = "shopCategoryList";
	private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);
	
	
	/**
	 * 根据查询条件列出店铺类别
	 */
	@Override
	public List<ShopCategory> getShopCategory(ShopCategory shopCategoryCondition) {
		List<ShopCategory> shopCategoryList = null;
		String key = SHOPCATEGORYLIST;
		ObjectMapper mapper = new ObjectMapper();
		// 拼接出redis的key
		if (shopCategoryCondition == null) {
			// 若查询条件为空，则列出所有首页大类，即parentId为空的店铺类型
			key = key + "_allfirstlevel";
		} else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null
				&& shopCategoryCondition.getParent().getShopCategoryId() != null) {
			// 若parentId不为空，则列出该parentId下的所有子类别
			key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
		} else if (shopCategoryCondition != null) {
			// 列出所有子类别，不管其属于哪个类都列出
			key = key + "_allsecondlevel";
		}
		// 判断key是否存在
		if (!jedisKeys.exists(key)) {
			// 若不存在，则从数据库中取出数据
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
			// 将实体类集合转换为string，存入redis
			String jsonString = null;
			try {
				jsonString = mapper.writeValueAsString(shopCategoryList);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
			jedisStrings.set(key, jsonString);
		} else {
			// 若存在，则直接从redis中取出数据
			String jsonString = jedisStrings.get(key);
			// 将String转换为集合类型
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			try {
				shopCategoryList = mapper.readValue(jsonString, javaType);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}
		
		return shopCategoryDao.queryShopCategory(shopCategoryCondition);
	}

}

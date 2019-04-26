package com.weilai.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weilai.o2o.dao.ShopAuthMapDao;
import com.weilai.o2o.dto.ShopAuthMapExecution;
import com.weilai.o2o.entity.ShopAuthMap;
import com.weilai.o2o.enums.ShopAuthMapStateEnum;
import com.weilai.o2o.exceptions.ShopAuthMapOperationException;
import com.weilai.o2o.service.ShopAuthMapService;
import com.weilai.o2o.utils.PageCalculator;

/**
 * 店铺授权service实现
 * @author ASUS
 *
 */
@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {
	@Autowired
	ShopAuthMapDao shopAuthMapDao;
	
	/**
	 * 添加授权信息
	 */
	@Override
	@Transactional
	public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
		//空值判断，主要是对店铺id和员工id做校验
		if (shopAuthMap != null && shopAuthMap.getShop() != null && shopAuthMap.getShop().getShopId() != null
				&& shopAuthMap.getEmployee() != null && shopAuthMap.getEmployee().getUserId() != null) {
			shopAuthMap.setCreateTime(new Date());
			shopAuthMap.setLastEditTime(new Date());
			shopAuthMap.setEnableStatus(1);
			shopAuthMap.setTitleFlag(0);
			try {
				
				//添加授权信息
				int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
				if (effectedNum <= 0) {
					throw new ShopAuthMapOperationException("添加店铺授权失败");
					
				}else {
					return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
				}
			} catch (Exception e) {
				throw new ShopAuthMapOperationException("添加店铺授权失败"+e.getMessage());
			}
		}
		else {
			return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOP_AUTH_INFO);
		}
	}
	/**
	 * 更新授权信息
	 */
	@Override
	public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
		//空值判断，主要是对授权id进行判断
		if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
			int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
			try {
				if (effectedNum <=0) {
					return new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
				}else {
					return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
				}
			} catch (Exception e) {
				throw new ShopAuthMapOperationException("更新授权信息失败"+e.getMessage());	
			}
		}else {
			return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOP_AUTH_ID);
		}
	}
	/**
	 * 根据店铺id分页显示该店铺的授权信息
	 */
	@Override
	public ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize) {
		//进行空值判断
		if (shopId != null && pageIndex != null && pageSize != null) {
			//页转行
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			List<ShopAuthMap> queryShopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(shopId, beginIndex, pageSize);
			int count = shopAuthMapDao.queryShopAuthCountByShopId(shopId);
			
			ShopAuthMapExecution shopAuthMapExecution = new ShopAuthMapExecution();
			shopAuthMapExecution.setCount(count);
			shopAuthMapExecution.setShopAuthMapList(queryShopAuthMapList);
			return shopAuthMapExecution;
		}
		return null;
	}
	/**
	 * 根据shopAuthId返回对应的授权信息
	 */
	@Override
	public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
		ShopAuthMap queryShopAuthMapById = shopAuthMapDao.queryShopAuthMapById(shopAuthId);
		return queryShopAuthMapById;
	}

}

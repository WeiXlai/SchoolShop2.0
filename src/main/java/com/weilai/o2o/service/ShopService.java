package com.weilai.o2o.service;

import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.weilai.o2o.dto.ImageHolder;
import com.weilai.o2o.dto.ShopExecution;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.exceptions.ShopOperationException;

public interface ShopService {

	/**
	 * 根据id查询店铺详情
	 * 
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(long shopId);
	/**
	 * 添加注册商铺信息，包括图片处理
	 * @param shop
	 * @param shopImg
	 * @return
	 * 
	 * ImageHolder thumbnail = InputStream shopImgInputStream,String fileName
	 */
	//为什么要加fileName参数呢，因为shopImgInputStream无法获取文件的名字
	ShopExecution addShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException;

	/**
	 * 更新店铺信息，包括对图片的处理
	 * 
	 * @param shop
	 * @param shopImg
	 * @return
	 */
	ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

	/**
	 * 获取店铺分页列表
	 * 
	 * @param shopCondition 店铺查询条件
	 * @param pageIndex     第几页
	 * @param pageSize      每页条数
	 * @return
	 * @throws ShopOperationException
	 */
	ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) throws ShopOperationException;
}

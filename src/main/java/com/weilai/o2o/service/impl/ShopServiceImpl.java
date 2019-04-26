package com.weilai.o2o.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.weilai.o2o.dao.ShopDao;
import com.weilai.o2o.dto.ImageHolder;
import com.weilai.o2o.dto.ShopExecution;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.enums.ShopStateEnum;
import com.weilai.o2o.exceptions.ShopOperationException;
import com.weilai.o2o.service.ShopService;
import com.weilai.o2o.utils.ImageUtil;
import com.weilai.o2o.utils.PageCalculator;
import com.weilai.o2o.utils.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	@Autowired
	private ShopDao shopDao;
	/**
	 * 添加注册商铺
	 */
	@Override
	@Transactional  //开启事务
	//为什么要加fileName参数呢，因为在shopImgInputStream无法获取文件的名字
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
		//空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			//给店铺信息赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			//添加店铺信息进数据库
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <=0) {
				throw new ShopOperationException("店铺创建失败");
			}else {
				if (thumbnail.getImage() !=null) {
					//存储商铺图片
					try {
						addShopImg(shop,thumbnail);
					} catch (Exception e) {
						throw new ShopOperationException("addShopImg error"+e.getMessage());
					}
					//更新店铺的图片地址
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("更新图片地址失败");
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error"+e.getMessage());
		}
		
		return new ShopExecution(ShopStateEnum.CHECK,shop);
	}
	
	/**
	 * 通过商铺id获取商铺信息
	 */
	@Override
	public Shop getByShopId(long shopId) {
		// TODO Auto-generated method stub
		return shopDao.queryByShopId(shopId);
	}
	/**
	 * 更新店铺信息，包括对图片的处理
	 */
	@Override
	public ShopExecution modifyShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException {
		//空值判断
		if (shop == null||shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}else {
			//1.判断是否要处理图片
		try {
			if (thumbnail.getImage() !=null && thumbnail.getImageName() !=null && !"".equals(thumbnail.getImageName())) {
				Shop tempShop = shopDao.queryByShopId(shop.getShopId());
				if (tempShop.getShopImg() !=null) {
					//删除已存在的图片及其目录
					ImageUtil.deleteFileOrPath(tempShop.getShopImg());
				}
				//添加新的图片
				addShopImg(shop, thumbnail);	
			}
			//2.更新店铺信息
			shop.setLastEditTime(new Date());
			int effectedNum = shopDao.updateShop(shop);
			if (effectedNum <= 0) {
				return new ShopExecution(ShopStateEnum.INNER_ERROR);
			}else {
				shop = shopDao.queryByShopId(shop.getShopId());
				return new ShopExecution(ShopStateEnum.SUCCESS,shop);
			}
		}catch (Exception e) {
			throw new ShopOperationException("modifyShop error:"+e.getMessage());
		}
			
		}
	}
	/**
	 * 通过传入的条件获取店铺列表
	 */
	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) throws ShopOperationException {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize); //获取行数，从第几行读取
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setCount(count);
			se.setShopList(shopList);
		}else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}			
		return se;
	}

	//设置店铺图片
	private void addShopImg(Shop shop,ImageHolder thumbnail) {
		//获取shop图片目录的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);//获得文件路径+图片名+.jpg
		shop.setShopImg(shopImgAddr);
		
	}

}

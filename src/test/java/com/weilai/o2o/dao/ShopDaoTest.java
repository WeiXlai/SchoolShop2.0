package com.weilai.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weilai.o2o.entity.Area;
import com.weilai.o2o.entity.PersonInfo;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.entity.ShopCategory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest {
	@Autowired
	ShopDao shopDao;
	
	@Test
	public  void testQueryShopListAndCount() {
		Shop shopCondition = new Shop();
		PersonInfo ownerInfo = new PersonInfo();
		ownerInfo.setUserId(1L);
		shopCondition.setOwner(ownerInfo);
		List<Shop> queryShopList = shopDao.queryShopList(shopCondition, 0, 5);
		int count = shopDao.queryShopCount(shopCondition);
		System.out.println("店铺的总数："+count);
		System.out.println("店铺的大小："+queryShopList.size());
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(33L);
		shopCondition.setShopCategory(shopCategory);
		List<Shop> queryShopList2 = shopDao.queryShopList(shopCondition, 0, 3);
		int count1 = shopDao.queryShopCount(shopCondition);
		System.out.println("新店铺的总数："+count1);
		System.out.println("新店铺的大小："+queryShopList2.size());
	}
	
	
	
	@Test
	public void testQueryByShopId() {
		long shopId = 39L;
		Shop byShopId = shopDao.queryByShopId(shopId);
		System.out.println(byShopId.getArea().getAreaId());
		System.out.println(byShopId.getArea().getAreaName());
		System.out.println(byShopId);
	}
	
	@Test
	public void testInsertShop() {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		
		owner.setUserId(1L);
		area.setAreaId(7);
		shopCategory.setShopCategoryId(33L);
		shop.setOwner(owner);
		shop.setShopCategory(shopCategory);
		shop.setArea(area);
		shop.setShopName("测试的店铺");
		shop.setEnableStatus(1);
		int insertShop = shopDao.insertShop(shop);
		assertEquals(1, insertShop);				
	}
	
	@Test
	public void testUpdateShop() {
		Shop shop = new Shop();
		shop.setShopId(30L);
		shop.setShopDesc("测试");
		shop.setShopAddr("北京");
		shop.setShopImg("test");
		shop.setPhone("texst");
		shop.setShopName("测试的店铺");
		shop.setAdvice("审核中");
		//shop.setCreateTime(new Date());
		shop.setLastEditTime(new Date());
	
		int updateShop = shopDao.updateShop(shop);
		assertEquals(1, updateShop);				
	}
	
}

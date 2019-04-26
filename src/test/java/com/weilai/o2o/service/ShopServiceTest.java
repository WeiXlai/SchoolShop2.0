package com.weilai.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weilai.o2o.dto.ImageHolder;
import com.weilai.o2o.dto.ShopExecution;
import com.weilai.o2o.entity.Area;
import com.weilai.o2o.entity.PersonInfo;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.entity.ShopCategory;
import com.weilai.o2o.enums.ShopStateEnum;
import com.weilai.o2o.exceptions.ShopOperationException;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {
	@Autowired
	ShopService shopService;
	
	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(33L);
		shopCondition.setShopCategory(shopCategory);
		ShopExecution shopExecution = shopService.getShopList(shopCondition, 2, 3);	
		System.out.println("新店铺的总数："+shopExecution.getCount());
		System.out.println("新店铺的大小："+shopExecution.getShopList().size());

	}
	
	@Test
	public void testModifyShop() throws ShopOperationException,FileNotFoundException{
		Shop shop = new Shop();
		shop.setShopId(30L);
		shop.setShopName("修改后的店铺名字是");
		File shopImg = new File("D:\\projectdev\\gou.jpg");  //upload\item\shop\32\19040716125534923.jpg
		InputStream iStream = new FileInputStream(shopImg);
		//System.out.println(iStream);
		ImageHolder imageHolder = new ImageHolder("gou.jpg", iStream);
		ShopExecution se = shopService.modifyShop(shop, imageHolder);
		System.out.println("新的图片地址是："+se.getShop().getShopImg());
	}
	
	
	@Test
	public void testInsertShop() throws FileNotFoundException {
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
		shop.setShopName("测试的店铺4");
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setShopDesc("测试4");
		shop.setShopAddr("北京4");
		shop.setShopImg("test4");
		shop.setPhone("texst4");
		shop.setShopName("测试的店铺4");
		shop.setAdvice("审核中");
		shop.setCreateTime(new Date());
		shop.setLastEditTime(new Date());
		File shopImg = new File("G:\\img\\shu.jpg");
		InputStream iStream = new FileInputStream(shopImg);
		
		ImageHolder imageHolder = new ImageHolder(shopImg.getName(), iStream);
		
		ShopExecution se = shopService.addShop(shop, imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());				
	}
}

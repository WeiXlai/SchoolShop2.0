package com.weilai.o2o.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.entity.ShopCategory;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCategoryServiceTest {
	@Autowired
	ShopCategoryService shopCategoryService;
	
	@Test
	public void testGetShopCategoryLsit() {
		ShopCategory shopCondition = new ShopCategory();
		ShopCategory parentCategory = new ShopCategory();
		parentCategory.setShopCategoryId(10L);
		shopCondition.setParent(parentCategory);
		
		shopCategoryService.getShopCategory(shopCondition);
	}
}

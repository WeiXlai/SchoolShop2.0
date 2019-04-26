package com.weilai.o2o.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weilai.o2o.entity.ShopCategory;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCategoryDaoTest  {
	@Autowired
	ShopCategoryDao shopCategoryDao;
	
	@Test
	public void test() {
//		ShopCategory testCategory = new ShopCategory();
//		ShopCategory parentCategory = new ShopCategory();
//		parentCategory.setShopCategoryId(13L);
//		testCategory.setParent(parentCategory);
//		List<ShopCategory> shopCategory = shopCategoryDao.queryShopCategory(testCategory);
//		assertEquals(2, shopCategory.size());
//		System.out.println(shopCategory.get(0).getShopCategoryName());
		List<ShopCategory> shopCategory = shopCategoryDao.queryShopCategory(null);
		//assertEquals(2, shopCategory.size());
		System.out.println(shopCategory.size());
	}

}

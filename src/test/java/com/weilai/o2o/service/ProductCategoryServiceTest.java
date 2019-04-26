package com.weilai.o2o.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import com.weilai.o2o.entity.ProductCategory;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryServiceTest  {
	@Autowired
	ProductCategoryService productCategoryService;
	
	@Test
	public void testGetProductCategory() {
		long shopId = 33L;
		List<ProductCategory> categoryList = productCategoryService.getProductCategoryList(shopId);
		for (ProductCategory productCategory : categoryList) {
			System.out.println(productCategory);
		}
	}
	
	
	
}

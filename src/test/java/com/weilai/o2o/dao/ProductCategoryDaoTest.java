package com.weilai.o2o.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import com.weilai.o2o.entity.ProductCategory;

//控制测试方法的执行顺序
//MethodSorters.JVM：按照JVM得到的方法顺序，也就是代码中定义的方法顺序
//MethodSorters.DEFAULT(默认的顺序)(以确定但不可预期的顺序执行)   MethodSorters.NAME_ASCENDING按方法名字母顺序执行)
//@FixMethodOrder
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryDaoTest {
	@Autowired
	ProductCategoryDao productCategoryDao;
	
	@Test
	public void testQueryProductCategory() {
		long shopId = 33L;
		List<ProductCategory> queryCategoryList = productCategoryDao.queryCategoryList(shopId);
		System.out.println(queryCategoryList.size());
	}
	
	@Test
	public void testBatchInsertProductCategory() {
		ProductCategory pCategory = new ProductCategory();
		pCategory.setProductCategoryName("冷饮");
		pCategory.setPriority(1);
		pCategory.setProductCategoryDesc("这是个清爽的饮料");
		pCategory.setShopId(33L);
		pCategory.setCreateTime(new Date());
		List<ProductCategory> list = new ArrayList<ProductCategory>();
		list.add(pCategory);
		
		ProductCategory pCategory1 = new ProductCategory();
		pCategory1.setProductCategoryName("热饮");
		pCategory1.setPriority(2);
		pCategory1.setProductCategoryDesc("这是个解寒的饮料");
		pCategory1.setShopId(33L);
		pCategory1.setCreateTime(new Date());
		list.add(pCategory1);
		int i = productCategoryDao.batchInsertProdutCategory(list);
		System.out.println(list.size());
		System.out.println(i);
			
	}
	
	@Test
	public void deleteProductCategory() {
		//先查询所有类目，匹配到想要删除的类目才删
		long shopId = 30L;
		List<ProductCategory> categoryList1 = productCategoryDao.queryCategoryList(shopId);
		for (ProductCategory productCategory : categoryList1) {
			if ("aaa".equals(productCategory.getProductCategoryName())||"bbb".equals(productCategory.getProductCategoryName())) {
				int i = productCategoryDao.deleteProductCategory(productCategory.getProductCategoryId(), productCategory.getShopId());
				
				assertEquals(1, i);
			}
		}
	}
	
}

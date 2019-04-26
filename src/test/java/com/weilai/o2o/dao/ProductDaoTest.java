package com.weilai.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import com.weilai.o2o.entity.PersonInfo;
import com.weilai.o2o.entity.Product;
import com.weilai.o2o.entity.ProductCategory;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.entity.ShopCategory;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductDaoTest {
	@Autowired
	ProductDao productDao;
	
	@Test
	public void testInsertProduct() {
		//将商品加入店铺中
		Shop shop = new Shop();
		shop.setShopId(32L);
		//设置商品的类别
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(20L);
		
		Product product = new Product();
		product.setCreateTime(new Date());
		product.setEnableStatus(0);
		product.setImgAddr("测试1");
		product.setLastEditTime(new Date());
		product.setNormalPrice("10");
		product.setPriority(1);
		product.setProductCategory(productCategory);
		product.setProductDesc("测试1");
		product.setProductName("测试1");
		product.setShop(shop);
		
		Product product1 = new Product();
		product1.setCreateTime(new Date());
		product1.setEnableStatus(0);
		product1.setImgAddr("测试2");
		product1.setLastEditTime(new Date());
		product1.setNormalPrice("10");
		product1.setPriority(1);
		product1.setProductCategory(productCategory);
		product1.setProductDesc("测试2");
		product1.setProductName("测试2");
		product1.setShop(shop);
		
		Product product2 = new Product();
		product2.setCreateTime(new Date());
		product2.setEnableStatus(0);
		product2.setImgAddr("测试3");
		product2.setLastEditTime(new Date());
		product2.setNormalPrice("10");
		product2.setPriority(1);
		product2.setProductCategory(productCategory);
		product2.setProductDesc("测试3");
		product2.setProductName("测试3");
		product2.setShop(shop);
		
		int p1 = productDao.insertProduct(product);
		assertEquals(1, p1);
		
		int p2 = productDao.insertProduct(product1);
		assertEquals(1, p2);
		
		int p3 = productDao.insertProduct(product2);
		assertEquals(1, p3);
	}
	
	/**
	 * 查询某id下的商品信息
	 */
	@Test
	public void testQueryProductByProductIdt() {
		long productId = 18L;
		Product product = productDao.queryProductByProductId(productId);
		System.out.println(product);

	}
	
	
	@Test
	public void testUpdateProduct() {
		Shop shop = new Shop();
		shop.setShopId(32L);
		Product product = new Product();
		product.setProductId(18L);
		product.setProductName("zheshi 测试4");
		product.setProductDesc("这是测试4");
		product.setShop(shop);
		int updateProduct = productDao.updateProduct(product);
		assertEquals(1, updateProduct);
	}
	
	@Test
	public  void testQueryShopListAndCount() {
		Shop shop = new Shop();
		shop.setShopId(30L);
		Product productCondition = new Product();
		productCondition.setShop(shop);
		List<Product> queryShopList = productDao.queryProductList(productCondition,1, 3);
		int count = productDao.queryProductCount(productCondition);
		System.out.println("店铺的总数："+count);
		System.out.println("店铺的大小："+queryShopList.size());
		
		ProductCategory shopCategory = new ProductCategory();
		shopCategory.setProductCategoryId(20L);
		productCondition.setProductCategory(shopCategory);
		List<Product> queryShopList2 = productDao.queryProductList(productCondition, 1, 3);
		int count1 = productDao.queryProductCount(productCondition);
		System.out.println("新店铺的总数："+count1);
		System.out.println("新店铺的大小："+queryShopList2.size());
	}
	
	@Test
	public void testUpdateProductCategorytoNull() {
		long productCategoryId = 1L;
		int i = productDao.updateProductCategoryToNull(productCategoryId);
		assertEquals(1, i);
	}
	
	
//	@Test
//	public void testDeteleProduct() {
//		
//	}
//	
}

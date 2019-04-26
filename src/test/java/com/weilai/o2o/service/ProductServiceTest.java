package com.weilai.o2o.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;


import com.weilai.o2o.dto.ImageHolder;
import com.weilai.o2o.dto.ProductExecution;
import com.weilai.o2o.entity.Product;
import com.weilai.o2o.entity.ProductCategory;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.enums.EnableStatusEnum;
import com.weilai.o2o.utils.ImageUtil;


/**
 * 
 * @Description: 商品业务测试
 *
 * @author: tyron
 * @date: 2018年11月11日
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Test
	public void testAddShop() throws IOException {
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(32L);
		product.setShop(shop);
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(1L);
		product.setProductCategory(productCategory);
		product.setProductName("测试商品1");
		product.setProductDesc("测试商品1描述");
		product.setPriority(11);
		product.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
		product.setLastEditTime(new Date());
		product.setCreateTime(new Date());
		//创建缩略图文件流
		File thumbnailFile = new File("G:\\img\\gou.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
		//创建两个商品详情图文件流并将他们添加到商品详情列表中
		File productImg1 = new File("G:\\img\\gou.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("G:\\img\\shu.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		
		List<ImageHolder> productImgList = new ArrayList<>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
		
		ProductExecution se = productService.addProduct(product, thumbnail, productImgList);
		System.out.println("ProductExecution.state" + se.getState());
		System.out.println("ProductExecution.stateInfo" + se.getStateInfo());
	}

	@Test
	public void testModifyShop() throws IOException {
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(32L);
		product.setShop(shop);
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(1L);
		product.setProductId(18L);
		product.setProductCategory(productCategory);
		product.setProductName("测试商品2");
		product.setProductDesc("测试商品2描述");
		product.setPriority(22);
		product.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
		product.setLastEditTime(new Date());
		product.setCreateTime(new Date());
		//创建缩略图文件流
		File thumbnailFile = new File("G:\\img\\gou.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
		//创建两个商品详情图文件流并将他们添加到商品详情列表中
		File productImg1 = new File("G:\\img\\gou.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("G:\\img\\shu.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
		ProductExecution se = productService.modifyProduct(product, thumbnail,
				productImgList);
		System.out.println("ProductExecution.state" + se.getState());
		System.out.println("ProductExecution.stateInfo" + se.getStateInfo());
	}
	
	@Test
	public  void testQueryShopListAndCount() {
		Shop shop = new Shop();
		shop.setShopId(30L);
		Product productCondition = new Product();
		productCondition.setShop(shop);
		ProductExecution productList = productService.getProductList(productCondition,1, 3);

		System.out.println("该店铺下商品的数量："+productList.getCount());
		for (int i = 0; i < productList.getProductList().size(); i++) {
			System.out.println(productList.getProductList().get(i).toString());
		}
	}
	
	
}

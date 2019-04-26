package com.weilai.o2o.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weilai.o2o.entity.ProductImg;
/**
 * 商品详情图dao测试
 * @author ASUS
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductImgDaoTest {
	@Autowired
	ProductImgDao productImgDao;
	/**
	 * 批量增加商品详情图
	 */
	@Test
	public void testbatchInsertProductImg() {
		ProductImg productImg = new ProductImg();
		productImg.setImgAddr("地址");
		productImg.setImgDesc("描述");
		productImg.setProductId(1L);
		productImg.setPriority(1);
		productImg.setCreateTime(new Date());
		
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("地址1");
		productImg1.setImgDesc("描述1");
		productImg1.setProductId(1L);
		productImg1.setPriority(2);
		productImg1.setCreateTime(new Date());
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg);
		productImgList.add(productImg1);
		productImgDao.batchInsertProductImg(productImgList);
	}
	/**
	 * 通过某商品id查询其下的所有商品详情图片
	 */
	@Test
	public void testQueryProductImg() {
		List<ProductImg> list = productImgDao.queryProductImgList(1L);
		for (ProductImg productImg : list) {
			System.out.println(productImg);
		}
		System.out.println(list.size());
	}
	/**
	 * 通过商品id删除其下面所有的商品详情图片
	 */
	@Test
	public void testDeleteProductImgById() {
		int productImgById = productImgDao.deleteProductImgByProductId(1L);
		assertEquals(2, productImgById);
	}
}

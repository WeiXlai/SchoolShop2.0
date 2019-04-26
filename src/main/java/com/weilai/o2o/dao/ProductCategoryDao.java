package com.weilai.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weilai.o2o.entity.ProductCategory;
/**
 * 店铺下商品类别dao
 * @author ASUS
 *
 */
public interface ProductCategoryDao {
	/**
	 * 通过shopId来查询该店铺所有的类别
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> queryCategoryList(long shopId);
	
	/**
	 * 批量增加店铺商品类别
	 * @param productCategories
	 * @return
	 */
	int batchInsertProdutCategory(List<ProductCategory> productCategories);
	/**
	 * 通过productCategoryId和shopId两个条件删除商品类别，防止删错不是该店铺的商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId")long shopId);
}

package com.weilai.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weilai.o2o.entity.Product;
import com.weilai.o2o.entity.Shop;

/**
 * 商品dao
 * @author ASUS
 *
 */
public interface ProductDao {
	/**
	 * 列出商品
	 * @param productId
	 * @return
	 */
	Product queryProductByProductId(long productId);
	/**
	 * 增加商品
	 * @param product
	 * @return
	 */
	
	int insertProduct(Product product);
	/**
	 * 更新商品信息
	 * @param product
	 * @return
	 */
	
	int updateProduct(Product product);
	/**
	 * 删除商品
	 * @param productId
	 * @return
	 */
	int deleteProduct(long productId);
	
	/**
	 * 分页查询店铺，可输入的条件有：店铺名（模糊查询），店铺状态，店铺类别，区域id，owner
	 * @param shopCondition 输入的条件
	 * @param rowIndex 从第几页开始取数据
	 * @param pageSize 返回的条数
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition")Product productCondition,
			@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);
	
	/**
	 * 返回queryList的总数
	 * @param shopCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition")Product productCondition);
	
	/**
	 * 删除商品类别之前，要先将所有该类别的商品的类别id置为空
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);
}

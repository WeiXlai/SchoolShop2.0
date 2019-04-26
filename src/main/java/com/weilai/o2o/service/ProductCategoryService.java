package com.weilai.o2o.service;

import java.util.List;

import com.weilai.o2o.dto.ProductCategoryExecution;
import com.weilai.o2o.entity.ProductCategory;
import com.weilai.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	/**
	 * 查询指定某个店铺下的所有商品类别信息
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(Long shopId);
	
	/**
	 * 批量增加商品类别
	 * @param productCategories
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
	throws ProductCategoryOperationException;
	
	/**
	 * 将此类目下的商品里的类别id置位空，再删掉该商品的类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId)
	throws ProductCategoryOperationException;;
	
}

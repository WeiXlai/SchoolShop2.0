package com.weilai.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weilai.o2o.dao.ProductCategoryDao;
import com.weilai.o2o.dao.ProductDao;
import com.weilai.o2o.dto.ProductCategoryExecution;
import com.weilai.o2o.entity.ProductCategory;
import com.weilai.o2o.enums.OperationStatusEnum;
import com.weilai.o2o.enums.ProductCategoryStateEnum;
import com.weilai.o2o.exceptions.ProductCategoryOperationException;
import com.weilai.o2o.service.ProductCategoryService;
/**
 * 店铺的商品类别业务接口实现
 * @author ASUS
 *
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
	@Autowired
	ProductCategoryDao productCategoryDao;
	
	@Autowired
	ProductDao productDao;

	@Override
	public List<ProductCategory> getProductCategoryList(Long shopId) {
		return productCategoryDao.queryCategoryList(shopId);
	}


	/*
	 * (non-Javadoc)
	 * 批量增加店铺类别
	 * @see
	 * com.tyron.o2o.service.ProductCategoryService#batchAddProductCategory(java.
	 * util.List)
	 */
	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		// 列表不为空
		if (productCategoryList != null && !productCategoryList.isEmpty()) {
			try {
				int effectedNum = productCategoryDao.batchInsertProdutCategory(productCategoryList);
				if (effectedNum <= 0) {
					throw new ProductCategoryOperationException("店铺类别创建失败");
				} else {
					return new ProductCategoryExecution(OperationStatusEnum.SUCCESS, productCategoryList, effectedNum);
				}
			} catch (Exception e) {
				throw new ProductCategoryOperationException(
						ProductCategoryStateEnum.EDIT_ERROR.getStateInfo() + e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPETY_LIST);
		}
	}

	/**
	 * 将此类目下的商品里的类别id置位空，再删掉该商品的类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	@Override
	@Transactional  //事务回滚
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		//  将此商品类别下的商品的类别id置为空
		// 解除tb_product与productCategoryId的关联
		try {
			int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
			if (effectedNum <= 0) {
				throw new ProductCategoryOperationException("商品类目更新至null失败");
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error"+e.getMessage());
		}
		
		//删除商品类别
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectedNum <=0) {
				throw new ProductCategoryOperationException("商品类目删除失败");
			}else {
				return new ProductCategoryExecution(OperationStatusEnum.SUCCESS, null, effectedNum);
			}
			
			
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:"+e.getMessage());
		}
	}



}

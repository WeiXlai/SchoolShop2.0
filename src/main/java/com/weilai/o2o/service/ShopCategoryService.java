package com.weilai.o2o.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weilai.o2o.entity.ShopCategory;

public interface ShopCategoryService {
	/**
	 * 列出店铺类别
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> getShopCategory(ShopCategory shopCategoryCondition);
}

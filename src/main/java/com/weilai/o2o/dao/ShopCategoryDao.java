package com.weilai.o2o.dao;
/**
 * 店铺类别
 * @author ASUS
 *
 */

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weilai.o2o.entity.ShopCategory;

public interface ShopCategoryDao {
	/**
	 * 列出店铺类别
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition")
	ShopCategory shopCategoryCondition);

}

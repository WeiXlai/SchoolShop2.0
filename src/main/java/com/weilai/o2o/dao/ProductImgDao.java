package com.weilai.o2o.dao;
/**
 * 商品详情图片dao
 * @author ASUS
 *
 */

import java.util.List;

import com.weilai.o2o.entity.ProductImg;

public interface ProductImgDao {
	/**
	 * 通过商品id列出商品详情图片列表
	 * @param ProductImgId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long ProductId);
	/**
	 * 批量增加详情图
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	/**
	 * 按某商品id见其下面的详情图删除
	 * @param productImgId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);

}

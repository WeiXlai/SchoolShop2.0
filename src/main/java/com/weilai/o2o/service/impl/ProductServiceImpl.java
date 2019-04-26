package com.weilai.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.weilai.o2o.dao.ProductDao;
import com.weilai.o2o.dao.ProductImgDao;
import com.weilai.o2o.dto.ImageHolder;
import com.weilai.o2o.dto.ProductExecution;
import com.weilai.o2o.entity.Product;
import com.weilai.o2o.entity.ProductImg;
import com.weilai.o2o.enums.EnableStatusEnum;
import com.weilai.o2o.enums.OperationStatusEnum;
import com.weilai.o2o.enums.ProductStateEnum;
import com.weilai.o2o.exceptions.ProductOperationException;
import com.weilai.o2o.service.ProductService;
import com.weilai.o2o.utils.ImageUtil;
import com.weilai.o2o.utils.PageCalculator;
import com.weilai.o2o.utils.PathUtil;



/**
 * @Description: 商品业务接口实现
 *
 * @author: tyron
 * @date: 2018年10月27日
 */
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductImgDao productImgDao;

	
	@Override
	@Transactional
	// 1、处理缩略图，获取缩略图相对路径并赋值给product
	// 2、往tb_product写入商品信息，获取productId
	// 3、结合productId批量处理商品详细图
	// 4、将商品详情图列表批量插入tb_product_img中
	 //thumbnail是商品缩略图，productImgList是商品详情图
	public ProductExecution addProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgList)
			throws ProductOperationException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 给商品设置默认属性
			product.setCreateTime(new Date());
			// 默认上架状态
			product.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
			// 若商品缩略图不为空则添加
			if (thumbnail != null) {
				addProductImg(product, thumbnail);
			}
			// 创建商品信息，添加商品信息进数据库
			try {
				int effectNum = productDao.insertProduct(product);
				if (effectNum <= 0) {
					throw new ProductOperationException(ProductStateEnum.EDIT_ERROR.getStateInfo());
				}
			} catch (Exception e) {
				throw new ProductOperationException(ProductStateEnum.EDIT_ERROR.getStateInfo() + e.toString());
			}
			// 若商品详情图列表不为空则添加
			if (productImgList != null && !productImgList.isEmpty()) {
				addProductImgList(product, productImgList);
			}
			return new ProductExecution(OperationStatusEnum.SUCCESS, product);
		} else {
			// 参数为空则返回空值错误信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tyron.o2o.service.ProductService#modifyProduct(com.tyron.o2o.entity.
	 * Product, org.springframework.web.multipart.MultipartFile, java.util.List)
	 */
	// 1.若缩略图参数有值，则先处理缩略图，先删除原有缩略图再添加
	// 2.若商品详情图参数有值，先删除后添加
	// 3.将tb_product_img下面的改商品原先的商品详情图记录全部清除
	// 4.更新tb_product_img以及tb_product的信息
	@Override
	@Transactional
	public ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgList)
			throws ProductOperationException {
		// 空值判断
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			// 设置默认更新时间
			product.setLastEditTime(new Date());
			// 若商品缩略图不为空且原有缩略图不为空，则先删除原有缩略图并添加
			if (thumbnail != null) {
				// 先获取原有信息，得到原有图片地址
				Product origProduct = productDao.queryProductByProductId(product.getProductId());
				if (origProduct.getImgAddr() != null) {
					//删除原有缩略图
					ImageUtil.deleteFileOrPath(origProduct.getImgAddr());
				}
				addProductImg(product, thumbnail);
			}

			// 若存在新的商品详情图且原详情图不为空，则先删除原有详情图并添加
			if (productImgList != null && !productImgList.isEmpty()) {
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgList);
			}

			// 更新商品信息
			try {
				int effectNum = productDao.updateProduct(product);
				if (effectNum <= 0) {
					throw new ProductOperationException(ProductStateEnum.EDIT_ERROR.getStateInfo());
				}
				return new ProductExecution(OperationStatusEnum.SUCCESS, product);
			} catch (ProductOperationException e) {
				throw new ProductOperationException(ProductStateEnum.EDIT_ERROR.getStateInfo() + e.getMessage());
			}
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	/**
	 * 获得该店铺下的商品列表
	 */
	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// 将页码转换为数据库的行数
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		// 获取商品列表分页信息
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		// 获取商品总数
		int productCount = productDao.queryProductCount(productCondition);
		// 构建返回对象,并设值
		ProductExecution productExecution = new ProductExecution();
		productExecution.setCount(productCount);
		productExecution.setProductList(productList);
		return productExecution;
	}
//
//	@Override
//	public Product getProductById(long productId) {
//		return productDao.selectProductByProductId(productId);
//	}

	/**
	 * 添加商品缩略图
	 * 
	 * @param product    商品
	 * @param productImg 商品缩略图
	 */
	private void addProductImg(Product product, ImageHolder thumbnail) {
		// 获取图片存储路径，将图片放在相应店铺的文件夹下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String productImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(productImgAddr);
	}

	/**
	 * 批量添加商品详情图
	 * 
	 * @param product        商品
	 * @param productImgList 商品详情图列表
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgList) {
		// 获取图片存储路径，将图片放在相应店铺的文件夹下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgs = new ArrayList<>();
		// 遍历商品详情图，并设置一些属性添加到productImg这个商品详情对象中
		for (ImageHolder multipartFile : productImgList) {
			String imgAddr = ImageUtil.generateProductImg(multipartFile, dest);
			ProductImg productImg = new ProductImg();
			productImg.setProductId(product.getProductId());
			productImg.setImgAddr(imgAddr);
			productImg.setCreateTime(new Date());
			productImgs.add(productImg);
		}

		// 存入有图片，就执行批量添加操作，将商品详情对象添加进数据库
		if (productImgs.size() > 0) {
			try {
				int effectNum = productImgDao.batchInsertProductImg(productImgs);
				if (effectNum <= 0) {
					throw new ProductOperationException(OperationStatusEnum.PIC_UPLOAD_ERROR.getStateInfo());
				}
			} catch (Exception e) {
				throw new ProductOperationException(OperationStatusEnum.PIC_UPLOAD_ERROR.getStateInfo() + e.toString());
			}
		}
	}

	/**
	 * 根据商品id唯一查询该商品信息
	 */
	@Override
	public Product getProductById(long productId) {	
		return productDao.queryProductByProductId(productId);
	}
	
	/**
	 * 修改商品信息以及图片处理
	 */
//	@Override
//	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
//			throws ProductOperationException {
//		//空值判断
//		if (product != null && product.getShop() !=null && product.getShop().getShopId() !=null) {
//			//给商品设置属性
//			product.setLastEditTime(new Date());
//			//若商品缩略图不为空且原有缩略图不为空，则删除原有缩略图并添加
//			if (thumbnail !=null) {
//				//先获取一遍该商品原有信息，进行判断有没有缩略图地址
//				Product tempProduct = productDao.queryProductByProductId(product.getProductId());
//				if (tempProduct.getImgAddr() != null) {
//					//有原缩略图地址，将其删除
//					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
//				}
//				//添加新的缩略图地址进商品
//				addProductImg(product, thumbnail);
//			}
//			//判断详情图是否有，有则删除原商品下的详情图
//			if (productImgList !=null && productImgList.size() > 0) {
//				deleteProductImgList(product.getProductId());
//				addProductImgList(product, productImgList);
//			}
//			//更新商品信息
//			try {
//				int effectedNum = productDao.updateProduct(product);
//				if (effectedNum < 0) {
//					throw new ProductOperationException("更新商品信息失败");
//				}
//				return new ProductExecution(OperationStatusEnum.SUCCESS,product);
//				
//			} catch (Exception e) {
//				throw new ProductOperationException("更新商品信息失败"+e.toString());
//			}
//			
//		}else {
//			return new ProductExecution(ProductStateEnum.EMPTY);
//		} 
//	}

	/**
	 * 删除某个商品下的所有详情图
	 * 
	 * @param productId
	 */
	private void deleteProductImgList(long productId) {
		// 根据productId获取原有的图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		if (productImgList != null && !productImgList.isEmpty()) {
			for (ProductImg productImg : productImgList) {
				// 删除存的图片
				ImageUtil.deleteFileOrPath(productImg.getImgAddr());
			}
			// 删除数据库中图片的信息
			productImgDao.deleteProductImgByProductId(productId);
		}
	}
}

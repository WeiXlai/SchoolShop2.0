package com.weilai.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weilai.o2o.dto.ProductExecution;
import com.weilai.o2o.dto.ShopExecution;
import com.weilai.o2o.entity.Area;
import com.weilai.o2o.entity.Product;
import com.weilai.o2o.entity.ProductCategory;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.entity.ShopCategory;
import com.weilai.o2o.service.AreaService;
import com.weilai.o2o.service.ProductCategoryService;
import com.weilai.o2o.service.ProductService;
import com.weilai.o2o.service.ShopCategoryService;
import com.weilai.o2o.service.ShopService;
import com.weilai.o2o.utils.HttpServletRequestUtil;
/**
 * 店铺详情控制
 * @author ASUS
 *
 */
@Controller
@RequestMapping("/front")
public class ShopDetailController {
	@Autowired
	ProductCategoryService productCategoryService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ShopService shopService;
	
	/**
	 * 获取店铺信息以及该店铺下面的商品类别列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshopdetailpageinfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listShopPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		List<ProductCategory> productCategoryList = null;
		//如果shopId!= -1，则取出店铺详情信息以及该店铺下的商品类目信息
		if (shopId != -1) {
			try {
				//根据店铺id查询店铺详情信息
				Shop shop = shopService.getByShopId(shopId);
				//获取该店铺下的商品类目信息	
				productCategoryList = productCategoryService.getProductCategoryList(shopId);
				modelMap.put("shop", shop);
				modelMap.put("productCategoryList", productCategoryList);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "empty shopId");
			}

		}

		return modelMap;		
	}
	

	/**
	 * 获取指定条件下该店铺下的所有商品列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listproductsbyshop",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getProductListByShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取从前台传过来的页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取从前台传过来的每页要求返回的商品数上限
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//获试着取店铺id
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		//空值判断
		if ((pageIndex > -1) && (pageSize > -1)&&shopId > -1) {
			//获取传入的需要检索的条件，包括是否需要从某个商品类别以及模糊查找商品名去
			//刷选某个店铺下的商品列表
			//从request中获取查询条件,条件可以进行排列组合
			
			//试着获取商品类目id
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			//试着获取模糊查询的商品名字
			String productName = HttpServletRequestUtil.getString(request, "productName");
			//获得查询组合条件productCondition
			Product productCondition = compactShopConditon4Search(shopId,productCategoryId,productName);
			//传入查询的条件以及分页信息进行查询，返回相应商品列表以及总数
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
	
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "缺少分页参数以及查询条件");
		}
					
		return modelMap;
	}

	/**
	 * 组合查询条件
	 * @param parentId
	 * @param shopCategoryId
	 * @param areaId
	 * @param productName
	 * @return
	 */
	private Product compactShopConditon4Search(long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		//查询某商品类别下所有商品列表
		if (productCategoryId !=-1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
	
		//模糊查询商品名来获取所有商品列表
		if (productName !=null) {
			productCondition.setProductName(productName);
		}
		//前端展示的商品都是上架的
		productCondition.setEnableStatus(1);
		return productCondition;
	}


}

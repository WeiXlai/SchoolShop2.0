package com.weilai.o2o.web.shopadmin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.weilai.o2o.dto.ProductCategoryExecution;
import com.weilai.o2o.dto.Result;
import com.weilai.o2o.entity.ProductCategory;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.enums.OperationStatusEnum;
import com.weilai.o2o.enums.ProductCategoryStateEnum;
import com.weilai.o2o.exceptions.ProductCategoryOperationException;
import com.weilai.o2o.service.ProductCategoryService;



/**
 * @Description: 店铺商品类别控制层
 *
 * @author: tyron
 * @date: 2018年9月22日
 */
@RequestMapping(value = "/shop")
@Controller
public class ProductCategoryController {

	@Autowired
	private ProductCategoryService productCategoryService;

	/**
	 * 根据ShopId获取productCategory商品类目
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
	@ResponseBody
	public Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
//		Shop shop = new Shop();
//		shop.setShopId(33L);
//		request.getSession().setAttribute("currentShop", shop);
		List<ProductCategory> productCategoryList;
		ProductCategoryStateEnum ps;
		// 在进入到shop管理页面（即调用getShopManageInfo方法时）,如果shopId合法，便将该shop信息放在了session中，key为currentShop
		// 这里我们不依赖前端的传入，因为不安全。 我们在后端通过session来做
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		if (currentShop != null && currentShop.getShopId() != null) {
			try {
				productCategoryList = productCategoryService.getProductCategoryList(currentShop.getShopId());
				return new Result<List<ProductCategory>>(true, productCategoryList);
			} catch (Exception e) {
				e.printStackTrace();
				ps = ProductCategoryStateEnum.EDIT_ERROR;
				return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
			}
		} else {
			ps = ProductCategoryStateEnum.NULL_SHOP;
			return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
		}
	

	}

	/**
	 * 添加商铺目录 ，使用@RequestBody接收前端传递过来的productCategoryList
	 * 
	 * @param productCategoryList
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		// 列表不为空
		if (productCategoryList != null && !productCategoryList.isEmpty()) {
			// 从session中获取店铺信息，尽量减少对前端的依赖
			Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
			if (currentShop != null && currentShop.getShopId() != null) {
				for (ProductCategory productCategory : productCategoryList) {
					productCategory.setShopId(currentShop.getShopId());
					productCategory.setCreateTime(new Date());
				}
			}
			try {
				// 批量插入
				ProductCategoryExecution productCategoryExecution = productCategoryService
						.batchAddProductCategory(productCategoryList);
				if (productCategoryExecution.getState() == OperationStatusEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					// 同时也将新增成功的数量返回给前台
					modelMap.put("effectNum", productCategoryExecution.getCount());
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", productCategoryExecution.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", ProductCategoryStateEnum.EMPETY_LIST.getStateInfo());
		}
		return modelMap;
	}
//
//	/**
//	 * 删除商品目录
//	 * 
//	 * @param productCategoryId
//	 * @param request
//	 */
	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryId != null && productCategoryId > 0) {
			// 从session中获取shop的信息
			Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
			if (currentShop != null && currentShop.getShopId() != null) {
				try {
					// 删除
					Long shopId = currentShop.getShopId();
					System.out.println(productCategoryId);
					ProductCategoryExecution pce = productCategoryService.deleteProductCategory(productCategoryId,
							shopId);
					if (pce.getState() == OperationStatusEnum.SUCCESS.getState()) {
						modelMap.put("success", true);
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", pce.getStateInfo());
					}
				} catch (ProductCategoryOperationException e) {
					e.printStackTrace();
					modelMap.put("success", false);
					modelMap.put("errMsg", e.getMessage());
					return modelMap;
				}
			} else {
				//商品信息为空，至少选择一个商品类别
				modelMap.put("success", false);
				modelMap.put("errMsg", ProductCategoryStateEnum.NULL_SHOP.getStateInfo());
			}
		} else {
			//没有商品目录信息
			modelMap.put("success", false);
			modelMap.put("errMsg", ProductCategoryStateEnum.EMPETY_LIST.getStateInfo());
		}
		return modelMap;
	}
}

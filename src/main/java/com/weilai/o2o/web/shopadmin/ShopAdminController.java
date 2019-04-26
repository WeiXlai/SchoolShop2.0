package com.weilai.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/shopadmin",method = {RequestMethod.GET})
public class ShopAdminController {
	/**
	 * 通过shopId转到增加商铺页面
	 * @return
	 */
	@RequestMapping(value = "/shopoperation")
	public String shopoperation() {
		return "shop/shopoperation";
	}
	

	/**
	 * 店铺管理页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/shopmanagement", method = RequestMethod.GET)
	public String shopManagement() {
		return "shop/shopmanagement";
	}
	
	/**
	 * 转到店铺列表页面
	 * @return
	 */
	@RequestMapping(value = "/shoplist")
	public String shopList() {
		return "shop/shoplist";
	}
	/**
	 * 转到商品分类页面即商品类别管理页面
	 * @return
	 */
	@RequestMapping(value = "/productcategorymanagement", method = RequestMethod.GET)
	public String productCategoryManage() {
		return "shop/productcategorymanagement";
	}
	
	/**
	 * 转发之商品添加/编辑页面
	 * @return
	 */
	@RequestMapping(value = "/productoperation")
	public String productOperation() {
		return "shop/productoperation";
	}
	
	/**
	 * 转发之商品管理页面
	 * @return
	 */
	@RequestMapping(value = "/productmanagement")
	public String productManagement() {
		return "shop/productmanagement";
	}
}

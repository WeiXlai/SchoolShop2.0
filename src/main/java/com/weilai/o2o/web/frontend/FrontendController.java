package com.weilai.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
	
	@RequestMapping(value = "/index",method = RequestMethod.GET)
	public String index() {
		return "front/index";
	}
	
	/**
	 * 商品列表页
	 * @return
	 */
	@RequestMapping(value = "/shoplist",method = RequestMethod.GET)
	public String showShopLIst() {
		return "front/shoplist";
	}
	
	/**
	 * 商店铺详情页
	 * @return
	 */
	@RequestMapping(value = "/shopdetail",method = RequestMethod.GET)
	public String showShopDetailList() {
		return "front/shopdetail";
	}
	
	/**
	 * 商品详情页
	 * @return
	 */
	@RequestMapping(value = "/productdetail",method = RequestMethod.GET)
	public String showProductDetail() {
		return "front/productdetail";
	}
}

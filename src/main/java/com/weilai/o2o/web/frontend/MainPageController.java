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

import com.weilai.o2o.entity.HeadLine;
import com.weilai.o2o.entity.ShopCategory;
import com.weilai.o2o.service.HeadLineService;
import com.weilai.o2o.service.ShopCategoryService;
/**
 * 主页面
 * @author ASUS
 *
 */
@Controller
@RequestMapping("/front")
public class MainPageController {
	@Autowired
	ShopCategoryService shopCategoryService;
	
	@Autowired
	HeadLineService headLineService;
	/**
	 * 初始化前端展示系统的主页信息，包括获取一级店铺类别列表和头条列表
	 * @return
	 */
	@RequestMapping(value = "/listmainpageinfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listMainPageInfo(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取一级店铺类别列表
		try {
			List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategory(null);
			modelMap.put("shopCategoryList", shopCategoryList);
		} catch (Exception e) {
			modelMap.put("errMsg", e.getMessage());
			modelMap.put("success", false);
			return modelMap;
		}
		//获取头条列表
		try {
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			List<HeadLine> headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
			
		} catch (Exception e) {
			modelMap.put("errMsg", e.getMessage());
			modelMap.put("success", false);
			return modelMap;
		}
		
		modelMap.put("success", true);
		return modelMap;
	}
	
}

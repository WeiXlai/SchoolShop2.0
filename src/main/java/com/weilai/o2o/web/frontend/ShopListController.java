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
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.entity.ShopCategory;
import com.weilai.o2o.service.AreaService;
import com.weilai.o2o.service.ShopCategoryService;
import com.weilai.o2o.service.ShopService;
import com.weilai.o2o.utils.HttpServletRequestUtil;
/**
 * 店铺列表控制
 * @author ASUS
 *
 */
@Controller
@RequestMapping("/front")
public class ShopListController {
	@Autowired
	ShopCategoryService shopCategoryService;
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	ShopService shopService;
	
	/**
	 * 返回店铺列表页里的shopCategory列表（二级或一级），一级区域信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getshoppageinfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listShopPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		//如果parentId!= -1，则取出一级ShopCategory下的二级ShopCategory
		if (parentId != -1) {
			try {
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parentCategory = new ShopCategory();
				parentCategory.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parentCategory);
				//返回该父商品目录下的二级商品目录
				shopCategoryList = shopCategoryService.getShopCategory(shopCategoryCondition);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			
		}else { // parentId不存在， 即“全部商店”列表，即传入空条件
			try {
				shopCategoryList = shopCategoryService.getShopCategory(null);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		modelMap.put("success", true);
		
		//查询区域列表
		List<Area> areaList = null;
		try {
			areaList = areaService.getAreaList();
			modelMap.put("success", true);
			modelMap.put("areaList", areaList);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		
		return modelMap;		
	}
	

	/**
	 * 获取指定条件下该店铺类目下的所有店铺列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshop",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getProductListByShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取从前台传过来的页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取从前台传过来的每页要求返回的店铺数上限
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		
		//空值判断
		if ((pageIndex > -1) && (pageSize > -1)) {
			//获取传入的需要检索的条件，包括是否需要从某个店铺类别以及模糊查找店铺名去
			//刷选某个店铺下的店铺列表
			//从request中获取查询条件,条件可以进行排列组合
			//获试着取一级店铺类目id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			//试着获取二级店铺类目id
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			//试着获取区域信息
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			//试着获取模糊查询的商品名字
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			//获得查询组合条件productCondition
			Shop shopCondition = compactShopConditon4Search(parentId,shopCategoryId,areaId,shopName);
			//传入查询的条件以及分页信息进行查询，返回相应商品列表以及总数
			ShopExecution pe = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", pe.getShopList());
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
	 * @param shopName
	 * @return
	 */
	private Shop compactShopConditon4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		//查询一级店铺类目下的二级店铺类目所有商店列表
		if (parentId !=-1L) {
			ShopCategory childShop = new ShopCategory();
			ShopCategory parentShop = new ShopCategory();
			parentShop.setShopCategoryId(parentId);
			childShop.setParent(parentShop);
			shopCondition.setShopCategory(childShop);
		}
		//查询二级店铺下的所有商店列表
		if (shopCategoryId != -1L) {
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);;
		}
		//查询某个区域id下所有商店列表
		if (areaId != -1L) {			
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		//模糊查询商店名来获取所有商店列表
		if (shopName !=null) {
			shopCondition.setShopName(shopName);
		}
		//前端展示的店铺都是审核成功的店铺
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}


}

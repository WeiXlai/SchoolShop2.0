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

import com.weilai.o2o.dao.ProductImgDao;
import com.weilai.o2o.dto.ProductExecution;
import com.weilai.o2o.dto.ShopExecution;
import com.weilai.o2o.entity.Area;
import com.weilai.o2o.entity.Product;
import com.weilai.o2o.entity.ProductCategory;
import com.weilai.o2o.entity.ProductImg;
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
public class ProductDetailController {
	@Autowired
	ProductImgDao productImg;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ShopService shopService;
	
	/**
	 * 获取商品信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listproductdetailpageinfo",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listProductPageInfo(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		
		//如果shopId!= -1，则取出店铺详情信息以及该店铺下的商品类目信息
		if (productId != -1) {
			try {
				//根据商品id查询商品详情信息
				Product product = productService.getProductById(productId);
				modelMap.put("product", product);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "empty productId");
			}

		}

		return modelMap;		
	}
	

}

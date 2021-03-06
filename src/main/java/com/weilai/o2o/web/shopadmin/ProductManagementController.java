package com.weilai.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weilai.o2o.dto.ImageHolder;
import com.weilai.o2o.dto.ProductExecution;
import com.weilai.o2o.entity.Product;
import com.weilai.o2o.entity.ProductCategory;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.enums.OperationStatusEnum;
import com.weilai.o2o.enums.ProductStateEnum;
import com.weilai.o2o.exceptions.ProductOperationException;
import com.weilai.o2o.service.ProductCategoryService;
import com.weilai.o2o.service.ProductService;
import com.weilai.o2o.utils.CodeUtil;
import com.weilai.o2o.utils.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductCategoryService productCategoryService;
	
	//支持上传商品详情图的最大数量
	private static final int IMAGEMAXCOUNT = 6;
	
	@RequestMapping(value = "/addproduct",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success",false);
			modelMap.put("errMsg", "输入了错误的验证码");
		}
		//接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		//productStr保存有商品的信息
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		 //用来处理文件流
		MultipartHttpServletRequest multipartRequest = null;
		//缩略图(ImageHolder里包含文件的名字和流)
		ImageHolder thumbnail = null; 
		//商品详情图（里有文件名和流）
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		//从request.getSession()中获取到文件流，是一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			//若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handImage(request, productImgList);
			
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
			}

		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		
		try {
			//尝试获取前端传过来的表单string流并将其转换成Product实体类
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//调用service层
		//若Product信息，缩略图thumbnail以及商品详情图列表productImgList为非空，则开始进行商品添加操作
		if (product != null&& thumbnail != null && productImgList.size()>0) {
			try {
				//从session中获取店铺信息，取出shopId
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
//				Shop shop = new Shop();
//				shop.setShopId(currentShop.getShopId());
				product.setShop(currentShop);
				//执行添加操作addProduct,传入商品信息、缩略图、详情图
				ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
				if (pe.getState() == OperationStatusEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
				
			} catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e).toString();
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", ProductStateEnum.PRODUCT_EMPTY.getStateInfo());
		}
	
		return modelMap;
	}

	
	
	/**
	 * 通过商品id查询商品信息
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/getproductbyid",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getProductById(@RequestParam long productId){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//先判断商品是否为空
		if (productId > -1) {
			//获取商品信息
			Product product = productService.getProductById(productId);
			if (product != null) {
				//获取该商品x下的类别列表
				List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
				modelMap.put("product", product);
				modelMap.put("productCategoryList", productCategoryList);
				modelMap.put("success", true);
			}		
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty product");
		}
		return modelMap;
	}
	
	/**
	 * 更新商品信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyproduct",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//检验验证码，在点击商品下架时不需要输入验证码，编辑时需要输入验证码
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		//状态改变且验证码错误时，返回
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", OperationStatusEnum.VERIFYCODE_ERROR.getStateInfo());
			return modelMap;
		}
		//1.接收前端参数并转化为相应的参数，包括商品信息以及缩略图和详情图片信息
		String productStr = HttpServletRequestUtil.getString(request, "productStr");//从request中取出shopStr,下面对他进行转换
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		try {
			product = mapper.readValue(productStr, Product.class);//转换productStr用product实体类进行接收
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//用来处理文件流
		MultipartHttpServletRequest multipartRequest = null;
		//缩略图(ImageHolder里包含文件的名字和流)
		ImageHolder thumbnail = null; 
		//商品详情图（里有文件名和流）
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		//从request.getSession()中获取到文件流，是一个通用的多部分文件解析器，在spring-web.xml有bean
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			//若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
			if (multipartResolver.isMultipart(request)) {
				//处理缩略图以及详情图
				thumbnail = handImage(request, productImgList);
			
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
			}

		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		//判断商品是否为空，不为空则对商品进行更新
		if (product != null) {
			try {
				//从session中获取当前店铺的id并赋值给product，减少对前端数值的依赖
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				//开始对商品进行更新操作
				ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
				if (pe.getState() == OperationStatusEnum.SUCCESS.getState()) { //说明更新成功
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
				
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", ProductStateEnum.PRODUCT_EMPTY.getStateInfo());
		}
		
		return modelMap;
	}
	
	/**
	 * 处理缩略图以及详情图
	 * @param request
	 * @param productImgList
	 * @return
	 * @throws IOException
	 */
	private ImageHolder handImage(HttpServletRequest request, List<ImageHolder> productImgList) throws IOException {
		MultipartHttpServletRequest multipartRequest;
		ImageHolder thumbnail;
		multipartRequest = (MultipartHttpServletRequest) request;
		//取出缩略图并构建ImageHolder对象
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
		//取出详情图列表并构建List<ImageHolder>列表对象，最多支持六张图片上传
		for (int i = 0; i < IMAGEMAXCOUNT; i++) {
			CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg"+i);
			if (productImgFile !=null) {
				//若取出的第i个详情图片文件流不为空，则将其加入详情图列表
				ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
				productImgList.add(productImg);
				
			}else {
				//若取出的第i个详情图片文件流为空，则终止循环
				break;
			}
		}
		return thumbnail;
	}
	
	/**
	 * 获取该商品下的所有商品列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getproductlistbyshop",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getProductListByShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取从前台传过来的页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取从前台传过来的每页要求返回的商品数上限
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//从当前session获取店铺信息，主要是获取shopId
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		//空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop !=null) && (currentShop.getShopId() !=null)) {
			//获取传入的需要检索的条件，包括是否需要从某个商品类别以及模糊查找商品名去
			//刷选某个店铺下的商品列表
			//从request中获取查询条件,条件可以进行排列组合
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName = HttpServletRequestUtil.getString(request, "productName");
			//获得查询条件productCondition
			Product productCondition = compactProductConditon(currentShop.getShopId(), productCategoryId, productName);
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
	 * 设置查询商品的查询条件
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactProductConditon(Long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		//设置店铺id为查询条件
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		//若有则设置商品类目为查询条件
		if (productCategoryId != -1) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		//若有则设置商品名字为查询条件
		if (productName != null) {
			productCondition.setProductName(productName);
		}
				
		return productCondition;
	}

}

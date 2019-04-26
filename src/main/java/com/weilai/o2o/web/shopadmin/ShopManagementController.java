package com.weilai.o2o.web.shopadmin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weilai.o2o.dto.ImageHolder;
import com.weilai.o2o.dto.ShopExecution;
import com.weilai.o2o.entity.Area;
import com.weilai.o2o.entity.PersonInfo;
import com.weilai.o2o.entity.Shop;
import com.weilai.o2o.entity.ShopCategory;
import com.weilai.o2o.enums.ShopStateEnum;
import com.weilai.o2o.exceptions.ShopOperationException;
import com.weilai.o2o.service.AreaService;
import com.weilai.o2o.service.ShopCategoryService;
import com.weilai.o2o.service.ShopService;
import com.weilai.o2o.utils.CodeUtil;
import com.weilai.o2o.utils.HttpServletRequestUtil;
import com.weilai.o2o.utils.ImageUtil;
import com.weilai.o2o.utils.PathUtil;

@Controller
@RequestMapping("/shop")
public class ShopManagementController {
	@Autowired
	ShopService shopService;
	@Autowired
	ShopCategoryService shopCategoryService;
	@Autowired
	AreaService areaService;
	
	/**
	 * 获取店铺管理信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getshopmanageInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// 如果shopId不存在
		if (shopId <= 0) {
			// 从session中获取店铺信息
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if (currentShopObj == null) {
				// 如果session中没有店铺信息，重定向回店铺列表页面
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shop/getshopList");
			} else {
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		} else {
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}
	
	/**
	 * 从request对象中取出登录的店主信息获取店铺列表
	 */
	@RequestMapping(value = "/getshopList",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopList(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		PersonInfo user = new PersonInfo();
//		user.setUserId(1L);
//		user.setUserType(1);
//		user.setName("test");
//		request.getSession().setAttribute("user", user);
		user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("user", user);
			modelMap.put("success", true);
			
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
	
	
	/**
	 * 通过shopId来获取店铺消息
	 */
	@RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1) {
			try {
				//通过shopId来获取店铺
				Shop shop = shopService.getByShopId(shopId);
				//获取区域列表
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);			
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}
	
	
	/**
	 * 店铺信息初始化：店铺区域和店铺类别
	 * 
	 * @return
	 */
	@RequestMapping(value = "getshopinitinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopInitInfo() {
		Map<String, Object> modelMap = new HashMap<>();
		List<Area> areaList = new ArrayList<>();
		List<ShopCategory> shopCategoryList = new ArrayList<>();
		try {
			shopCategoryList = shopCategoryService.getShopCategory(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", e.getMessage());
		}
		return modelMap;
	}
	
	/**
	 * 注册店铺
	 */
	@RequestMapping(value = "/registershop",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//判断验证码
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1.接收并转化为相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");//从request中取出shopStr,下面对他进行转换
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);//转换shopStr用shop实体类进行接收
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//获取前端传过来的文件流
		 CommonsMultipartFile shopImg = null;
	     CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
	                request.getSession().getServletContext());
	        if (commonsMultipartResolver.isMultipart(request)) {
	            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
	            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
	        } else {
	            modelMap.put("success", false);
	            modelMap.put("errMsg", "上传图片不能为空");
	            return modelMap;
	        }
		
		//2.注册店铺(店家注册)
	    if (shop !=null&&shopImg !=null) {
	    	 //从session中获取用户对象，这个user是从用户登录时保存在session中的
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			//Session TODO
			//owner.setUserId(1L);
			shop.setOwner(owner);
			
			//添加商铺
			ShopExecution se;
			try {
				//shopImg.getInputStream()返回InputStream读取文件的内容，shopImg.getOriginalFilename()获取原本文件的名字
				ImageHolder thumbnail = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
				
				se = shopService.addShop(shop, thumbnail);
				if (se.getState()==ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					//一个用户可以有多个可操作的店铺
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if (shopList == null) {
						shopList = new ArrayList<Shop>();
					}
					shopList.add(se.getShop()); //往店铺列表中添加店铺
					request.getSession().setAttribute("shopList",shopList);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			}catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			
			return modelMap;
		} else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
	          
	        
		//3.返回结果(这一步穿插在各个try-catch中了)
	}
	/**
	 * 更新店铺
	 */
	@RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//判断验证码
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//1.接收并转化为相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");//从request中取出shopStr,下面对他进行转换
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);//转换shopStr用shop实体类进行接收
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		//获取前端传过来的文件流
		 CommonsMultipartFile shopImg = null;
	     CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
	                request.getSession().getServletContext());
	        if (commonsMultipartResolver.isMultipart(request)) {
	            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
	            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
	        }
		
		//2.更新店铺(店家更新)
	    if (shop !=null&&shop.getShopId() !=null) {
	    	 //从session中获取用户对象，这个user是从用户登录时保存在session中的
			//PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
//	    	PersonInfo owner = new PersonInfo();
//			//Session TODO
//			owner.setUserId(1L);
//			shop.setOwner(owner);
			//修改店铺信息时，不用修改用户信息
			//添加商铺
			ShopExecution se;
			try {
				if (shopImg == null) {  //如果店铺图片为空
					se = shopService.modifyShop(shop, null);
				}else {
					//shopImg.getInputStream()返回InputStream读取文件的内容，shopImg.getOriginalFilename()获取原本文件的名字
					ImageHolder thumbnail = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
					
					se = shopService.modifyShop(shop,thumbnail);
				}				
				if (se.getState()==ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			}catch (ShopOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			
			return modelMap;
		} else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺Id");
            return modelMap;
        }
	          
	        
		//3.返回结果(这一步穿插在各个try-catch中了)
	}
	//将CommonsMultipartFile转化为file文件
//	private static void inputStreamToFile(InputStream ins,File file) {
//		FileOutputStream os = null;
//		try {
//			os = new FileOutputStream(file);
//			int bytesRead = 0;
//			byte[] buffer = new byte[1024];
//			while((bytesRead = ins.read(buffer)) !=-1) {
//				os.write(buffer,0,bytesRead);
//			}
//			
//		} catch (Exception e) {
//			throw new RuntimeException("调用inputStreamToFile产生异常"+e.getMessage());
//		}finally {
//			try {
//				if (os !=null) {
//					os.close();
//				}
//				if (ins !=null) {
//					ins.close();
//				}
//			} catch (IOException e2) {
//				throw new RuntimeException("调用inputStreamToFile关闭io产生异常"+e2.getMessage());
//			}
//		}
//	}

}

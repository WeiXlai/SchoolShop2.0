package com.weilai.o2o.utils;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 封装Util
 * @author ASUS
 *
 */
@Configuration
public class PathUtil {
	
	private static String winPath;
	
	private static String linuxPath;
	
	private static String shopPath;
	@Value("${win.base.path}")
	public void setWinPath(String winPath) {
		PathUtil.winPath = winPath;
	}
	@Value("${linux.bast.path}")
	public void setLinuxPath(String linuxPath) {
		PathUtil.linuxPath = linuxPath;
	}
	@Value("${shop.base.path}")
	public void setShopPath(String shopPath) {
		PathUtil.shopPath = shopPath;
	}
	//获取系统文件的执行属性，分隔符
	private static String seperator = System.getProperty("file.separator");
	/**
	 * 返回项目图片的根路径
	 * @return
	 */
	public static String getImgBasePath() {
		
		
		//获取操作系统的名字
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().startsWith("win")) {
			basePath = winPath;
		}else {
			basePath = linuxPath;
		}
		basePath = basePath.replace("/", seperator);
		return basePath;
	}
	/**
	 * 根据不同的业务需求，返回不同的子路经
	 * @param shopId
	 * @return
	 */
	public static String getShopImagePath(long shopId) {
		String imagePath = shopPath+shopId+seperator;
		return imagePath.replace("/", seperator);
	}

}

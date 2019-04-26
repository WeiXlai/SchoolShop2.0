package com.weilai.o2o.utils;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.weilai.o2o.dto.ImageHolder;

import ch.qos.logback.core.util.FileUtil;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * Thumbnails处理图片
 * @author ASUS
 *
 */
public class ImageUtil {
	//获取classpath的绝对值路径
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    //时间格式化的格式  
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
    //随机数
    private static final Random r = new Random();
    /**
     * 处理商铺缩略图（商品小图、门面照）,并返回新生成图片的相对值路径
     *@param thumbnail  Spring自带的文件处理对象
	 * @param targetAddr 图片存储路径
     * @return
     */
    public static String generateThumbnail(ImageHolder thumbnail,String targetAddr) {
        String realFileName = getRandomFileName(); //获取文件随机名
        String extension = getFileExtension(thumbnail.getImageName());  //获取文件扩展名
        makeDirPath(targetAddr);  //在文件夹不存在时创建，创建文件路径下的目录
        String relativePath = targetAddr + realFileName + extension; 
        File dest = new File(PathUtil.getImgBasePath() + relativePath); //新生成的文件路径
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/gou.jpg")), 0.25f)
                    .outputQuality(0.8f).toFile(dest);  //在dest生成文件
        } catch (IOException e) { 

            e.printStackTrace();
        }
        return relativePath;

    }
    
	/**
	 * 处理首页头图
	 * 
	 * @param thumbnail  Spring自带的文件处理对象
	 * @param targetAddr 图片存储路径
	 * @return
	 */
	public static String generateHeadImg(ImageHolder thumbnail, String targetAddr) {
		// 获取随机文件名，防止文件重名
		String realFileName = getRandomFileName();
		// 获取文件扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		// 在文件夹不存在时创建
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(400, 300).outputQuality(0.9f).toFile(dest); 
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

	/**
	 * 处理商品分类图
	 * 
	 * @param thumbnail  Spring自带的文件处理对象
	 * @param targetAddr 图片存储路径
	 * @return
	 */
	public static String generateShopCategoryImg(ImageHolder thumbnail, String targetAddr) {
		// 获取随机文件名，防止文件重名
		String realFileName = getRandomFileName();
		// 获取文件扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		// 在文件夹不存在时创建
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(50, 50).outputQuality(0.9f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

	/**
	 * 处理商品详情图
	 * 
	 * @param thumbnail  Spring自带的文件处理对象
	 * @param targetAddr 图片存储路径
	 * @return
	 */
	public static String generateProductImg(ImageHolder thumbnail, String targetAddr) {
		// 获取随机文件名，防止文件重名
		String realFileName = getRandomFileName();
		// 获取文件扩展名如png,jpg
		String extension = getFileExtension(thumbnail.getImageName());
		// 在文件夹不存在时创建
		makeDirPath(targetAddr);
		//获取文件存储的相对路径（带文件名）
		String relativeAddr = targetAddr + realFileName + extension;
		//获取文件要保存的目标路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/gou.jpg")), 0.5f)
					.outputQuality(0.9f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

    
    

    /**
     * 创建目标路径所涉及的目录，即/home/work/xiangze/xxx.jpg
     * 那么home work xoamgze 这三个文件夹都得自动创建
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String parentBasePath = PathUtil.getImgBasePath()+targetAddr;
        File f=new File(parentBasePath);
        if(!f.exists()) {
            f.mkdirs();
        }
    }
    /**
     * 获取输入文件流的扩展名
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
//      // 以分隔符来获取扩展名
     // String orginFileName = fileName.getName();
        return fileName.substring(fileName.lastIndexOf("."));
    }
    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     * @return
     */
    public static String getRandomFileName() {
        // 获取随即五位数
        int ranNum = r.nextInt(89999) + 10000;
        String currentTime = sf.format(new Date());
        return currentTime + ranNum;
    }

    public static void main(String[] args) {

        try {
            Thumbnails.of(new File("G:\\img\\shu.jpg")).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/gou.jpg")), 0.25f)
                    .outputQuality(0.8).toFile("G:\\img\\shu1.jpg");
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    
    /**
     * 删除缩略图以及详情图的原图片地址
     * storePath是文件的路径还是目录的路径
         * 如果storePath是文件的路径则删除改文件
         * 如果storePath是目录路径则删除该目录下的所有文件
     */
    public static void deleteFileOrPath(String storePath) {
    	//获取文件路径
    	File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
    	if (fileOrPath.exists()) {
			if (fileOrPath.isDirectory()) { //是否是目录路径
				File file[] = fileOrPath.listFiles();
				for (int i = 0; i < file.length; i++) {
					file[i].delete();
				}
			}
			fileOrPath.delete(); //是文件路径
		}
    }
}

package com.weilai.o2o.dto;
/**
 * 重构文件名字和文件流（即对图片的重构）
 * @author ASUS
 *
 */

import java.io.InputStream;

public class ImageHolder {
	private String imageName;
	private InputStream image;
	
	public ImageHolder(String imageName,InputStream image) {
		this.imageName = imageName;
		this.image = image;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public InputStream getImage() {
		return image;
	}

	public void setImage(InputStream image) {
		this.image = image;
	}
	
}

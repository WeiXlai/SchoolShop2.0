package com.weilai.o2o.dao;

import java.util.List;

import com.weilai.o2o.entity.Area;
/**
 * 店铺区域
 * @author ASUS
 *
 */
public interface AreaDao {
	/**
	 * 列出区域列表
	 * @return
	 */
	List<Area> queryArea();
}

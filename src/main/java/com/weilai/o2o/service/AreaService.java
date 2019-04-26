package com.weilai.o2o.service;

import java.util.List;

import com.weilai.o2o.entity.Area;

public interface AreaService {
	public static final String AREALISTKEY = "arealist";
	//获得区域列表
	List<Area> getAreaList();

}

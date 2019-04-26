package com.weilai.o2o.service;
/**
 * 头条service
 * @author ASUS
 *
 */

import java.util.List;

import com.weilai.o2o.entity.HeadLine;

public interface HeadLineService {
	/**
	 * 通过头条的一些条件查询头条列表
	 * @param headLineCondition
	 * @return
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition);

}

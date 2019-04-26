package com.weilai.o2o.dao;
/**
 * 头条Dao
 * @author ASUS
 *
 */

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weilai.o2o.entity.HeadLine;

public interface HeadLineDao {
	/**
	 * 通过输入的条件查询头条列表(头条名查询头条)
	 * @param headLineCondition
	 * @return
	 */
	List<HeadLine> queryHeadLineList(@Param("headLineConditon")HeadLine headLineCondition);
	
	/**
	 * 通过headLineId唯一查询头条
	 * @param headLineId
	 * @return
	 */
	int queryHeadLineByIds(long headLineId);
	
	
}

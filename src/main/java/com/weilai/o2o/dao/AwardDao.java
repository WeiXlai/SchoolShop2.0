package com.weilai.o2o.dao;
/**
 * 奖品dao
 * @author ASUS
 *
 */

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weilai.o2o.entity.Award;

public interface AwardDao {
	
	List<Award> queryAwardList(@Param("awardCondition")Award awardCondition
			,@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);
	
	int queryCount(@Param("awardCondition")Award awardCondition);
	
	Award queryAwardByid(long awardId);
	
	int insertAwrad(Award award);
	
	int updateAward(Award award);
	
	int deleteAward(@Param("awardId")long awardId,@Param("shopId")long shopId);
}

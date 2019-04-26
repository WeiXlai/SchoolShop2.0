package com.weilai.o2o.dao;
/**
 * 顾客已经领取得奖品映射
 * @author ASUS
 *
 */

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weilai.o2o.entity.Award;
import com.weilai.o2o.entity.UserAwardMap;

public interface UserAwardMapDao {
	
	List<UserAwardMap> queryAwardList(@Param("userAwardCondition")UserAwardMap userAwardCondition
			,@Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);
	
	int queryCount(@Param("userAwardCondition")UserAwardMap userAwardCondition);
	
	UserAwardMap queryUserAwardMapByid(long userAwardId);
	
	int insertUserAwardMap(UserAwardMap userAward);
	
	int updateUserAwardMap(UserAwardMap userAward);
	

}

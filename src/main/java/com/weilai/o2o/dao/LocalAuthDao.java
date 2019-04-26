package com.weilai.o2o.dao;
/**
 * 本地账户实现登录dao
 * @author ASUS
 *
 */

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.weilai.o2o.entity.LocalAuth;

public interface LocalAuthDao {
	/**
	 * 通过账号密码来查询
	 * @param username
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalAuth(@Param("username")String username,@Param("password")String password);
	
	/**
	 * 通过用户id查询
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalAuthByUserId(@Param("userId")long userId);
	
	/**
	 * 插入
	 * @param username
	 * @param password
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);

	/**
	 * 更新信息
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @return
	 */
	int updateLocalAuth(@Param("userId")long userId,@Param("username")String username,
			@Param("password")String password,@Param("newPassword")String newPassword,@Param("lastEditTime")Date lastEditTime);
	


}

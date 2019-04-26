package com.weilai.o2o.service;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.weilai.o2o.dto.LocalAuthExceution;
import com.weilai.o2o.entity.LocalAuth;
import com.weilai.o2o.exceptions.LocalAuthOperationException;
/**
 * localAuthService 层
 * @author ASUS
 *
 */
public interface LocalAuthService {
	/**
	 * 通过账号密码来查询
	 * @param username
	 * @param password
	 * @return
	 */
	LocalAuth getLocalAuth(String username,String password);
	
	/**
	 * 通过用户id查询
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);
	
	/**
	 * 生成本地账号
	 * @param username
	 * @param password
	 * @return
	 */
	LocalAuthExceution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

	/**
	 * 更新信息
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @return
	 */
	LocalAuthExceution modifyLocalAuth(long userId,String username,
			String password,String newPassword) throws LocalAuthOperationException;
	

}

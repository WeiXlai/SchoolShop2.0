package com.weilai.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.weilai.o2o.dao.LocalAuthDao;
import com.weilai.o2o.dto.LocalAuthExceution;
import com.weilai.o2o.entity.LocalAuth;
import com.weilai.o2o.enums.LocalAuthStateEnum;
import com.weilai.o2o.exceptions.LocalAuthOperationException;
import com.weilai.o2o.service.LocalAuthService;
import com.weilai.o2o.utils.MD5;
@Service
public class LocalAuthServiceImpl implements LocalAuthService {
	@Autowired
	LocalAuthDao localAuthDao;
	

	public LocalAuth getLocalAuth(String username, String password) {
		//使用MD5加密方法对密码进行加密
		return localAuthDao.queryLocalAuth(username, MD5.getMd5(password));
	}


	public LocalAuth getLocalAuthByUserId(long userId) {
		// TODO Auto-generated method stub
		return localAuthDao.queryLocalAuthByUserId(userId);
	}
	/**
	 * 生成本地账号
	 */
	@Override
	@Transactional
	public LocalAuthExceution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		//空值判断，传入得localAuth账号密码、用户信息特别是userId不能为空，否则直接返回错误
		if (localAuth == null || localAuth.getUserName() ==null || localAuth.getPassword() ==
				null || localAuth.getPersonInfo() ==null || localAuth.getPersonInfo().getUserId() ==null) {
			return new LocalAuthExceution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		//验证不是空值之后，查询该用户是够已经注册过
		LocalAuth tempAuth = localAuthDao.queryLocalAuthByUserId(localAuth.getPersonInfo().getUserId());
		if (tempAuth != null) {
			//如果注册过则直接退出，保证账号唯一
			return new LocalAuthExceution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		try {
			//若之前没有注册过账号，则创建一个
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			//对密码进行加密
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);
			if (effectedNum <= 0) {
				throw new LocalAuthOperationException("账户创建失败");
			}else {
				
				return new LocalAuthExceution(LocalAuthStateEnum.SUCCESS, localAuth);
			}
			
		} catch (Exception e) {
			throw new LocalAuthOperationException("添加本地账户错误"+e.getMessage());
		}
 
	}
	
	/**
	 * 更新账户密码
	 */
	@Override
	@Transactional
	public LocalAuthExceution modifyLocalAuth(long userId, String username, String password, String newPassword)throws LocalAuthOperationException {
		// TODO Auto-generated method stub
		//非空判断
		if (userId != 0 && username !=null && password !=null&& newPassword !=
				null && !password.equals(newPassword)) {
			try {
				//更新密码
				int effectedNum = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password),  MD5.getMd5(newPassword), new Date());
				//判断更新是否成功
				
				if (effectedNum <= 0) {
					throw new LocalAuthOperationException("更新密码失败");
					
				}else {
					return new LocalAuthExceution(LocalAuthStateEnum.SUCCESS);
				}			
				
			} catch (Exception e) {
				throw new LocalAuthOperationException("更新密码失效"+e.getMessage());
			}
		}else {
			return new LocalAuthExceution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}

}

package com.weilai.o2o.dto;
/**
 * 本地账户中间值
 * @author ASUS
 *
 */

import java.util.List;

import com.weilai.o2o.entity.LocalAuth;
import com.weilai.o2o.enums.LocalAuthStateEnum;

public class LocalAuthExceution {
	//结果状态
	private int state;
	//状态标识
	private String stateInfo;
	//总数
	private int count;
	
	private LocalAuth localAuth;
	
	private List<LocalAuth> localAuthList;
	
	public LocalAuthExceution(){
		
	}
	
	//失败时的构造器
	public LocalAuthExceution(LocalAuthStateEnum localAuthStateEnum) {
		this.state = localAuthStateEnum.getState();
		this.stateInfo = localAuthStateEnum.getStateInfo();
	}
	
	//成功时的构造器
	public LocalAuthExceution(LocalAuthStateEnum localAuthStateEnum,LocalAuth localAuth) {
		this.state = localAuthStateEnum.getState();
		this.stateInfo = localAuthStateEnum.getStateInfo();
		this.localAuth = localAuth;
	}
	
	//成功时的构造器
	public LocalAuthExceution(LocalAuthStateEnum localAuthStateEnum,List<LocalAuth> localAuthList) {
		this.state = localAuthStateEnum.getState();
		this.stateInfo = localAuthStateEnum.getStateInfo();
		this.localAuthList = localAuthList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public LocalAuth getLocalAuth() {
		return localAuth;
	}

	public void setLocalAuth(LocalAuth localAuth) {
		this.localAuth = localAuth;
	}

	public List<LocalAuth> getLocalAuthList() {
		return localAuthList;
	}

	public void setLocalAuthList(List<LocalAuth> localAuthList) {
		this.localAuthList = localAuthList;
	}
	
}

package com.weilai.o2o.dto;

import java.util.List;

import com.weilai.o2o.entity.Product;
import com.weilai.o2o.enums.OperationStatusEnum;
import com.weilai.o2o.enums.ProductStateEnum;

public class ProductExecution {
	//结果状态
	private int state;
	//状态标识
	private String stateInfo;
	
	// 商品数量
	private int count;
	
	//操作的product(增删改商品时用到，针对单个商品)
	private Product product;
	//product列表（查询商品列表时用到）
	private List<Product> productList;
	
	public ProductExecution(){
		
	}
	
	//失败时的构造器
	public ProductExecution(ProductStateEnum productStateEnum) {
		this.state = productStateEnum.getState();
		this.stateInfo = productStateEnum.getStateInfo();
	}
	
	//成功时的构造器
	public ProductExecution(OperationStatusEnum stateEnum,Product product) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.product = product;
	}
		
		
	//成功时的构造器
	public ProductExecution(OperationStatusEnum stateEnum,List<Product> productList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.productList = productList;
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

	public Product getProduct() {
		return product;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	
	
}

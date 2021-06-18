/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.projectInfo.entity;

import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import java.math.BigDecimal;

/**
 * 项目管理Entity
 * @author Peter
 * @version 2020-12-02
 */
public class ProjectInfo extends DataEntity<ProjectInfo> {
	
	private static final long serialVersionUID = 1L;
	private String projectNo;		// 项目编号，客户编号
	private String projectName;		// 项目名称，客户简称
	private Office office;		// 机构id
	private String status;		// 项目状态，账号状态
	private BigDecimal longitude;		// 经度
	private BigDecimal latitude;		// 纬度

	private String loginName;// 账号 login_name
	private String customerType;// 客户类型 customer_type
	private String version;// 平台版本
	private String projectAllName;// 客户全称 project_all_name
	private String customer;// 客户联系人
	private String customerPhone;// 客户联系人电话 customer_phone
	private String business;// 业务负责人
	private String businessPhone;// 业务负责人电话 business_phone
	private String contractNo;//合同编号 contract_no
	
	public ProjectInfo() {
		super();
	}

	public ProjectInfo(String id){
		super(id);
	}

	@ExcelField(title="项目编号", align=2, sort=1)
	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}
	
	@ExcelField(title="项目名称", align=2, sort=2)
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@ExcelField(title="机构id", fieldType=Office.class, value="office.office.name", align=2, sort=3)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="项目状态", dictType="del_flag", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProjectAllName() {
		return projectAllName;
	}

	public void setProjectAllName(String projectAllName) {
		this.projectAllName = projectAllName;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.entity.base;

import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 部门流程配置Entity
 * @author crj
 * @version 2019-11-21
 */
public class FlowDepart extends DataEntity<FlowDepart> {
	
	private static final long serialVersionUID = 1L;
	private String flowNo;		// 流程编号
	private String billType;		// 工单类型
	private Office orgId;		// 部门
	
	public FlowDepart() {
		super();
	}

	public FlowDepart(String id){
		super(id);
	}

	@ExcelField(title="流程编号", align=2, sort=1)
	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}
	
	@ExcelField(title="工单类型", dictType="work_type", align=2, sort=2)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@ExcelField(title="部门", fieldType=Office.class, value="orgId.name", align=2, sort=3)
	public Office getOrgId() {
		return orgId;
	}

	public void setOrgId(Office orgId) {
		this.orgId = orgId;
	}
	
}
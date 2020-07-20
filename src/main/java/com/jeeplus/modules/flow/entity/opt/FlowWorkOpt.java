/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.entity.opt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.flow.entity.base.FlowProc;
import com.jeeplus.modules.sys.entity.Office;

import java.util.List;

/**
 * 工单操作记录Entity
 * @author crj
 * @version 2019-11-21
 */
public class FlowWorkOpt extends DataEntity<FlowWorkOpt> {
	
	private static final long serialVersionUID = 1L;
	private CoverWork billId;		// 工单
	private String billNo;		// 工单编号
	private FlowProc flowId;		// 流程信息
	private FlowOpt optId;		// 操作信息
	private String optName;		// 操作名称
	private String originState;		// 原状态
	private String resultState;		// 结果状态
	private String data;		// 操作数据
	private Office optOrg;		// 操作部门
	private Office targetOrg;		// 目标部门

	//临时属性
	@JsonIgnore
	private List<String> imagesList;
	@JsonIgnore
	private String optRemarks;
	
	public FlowWorkOpt() {
		super();
	}

	public FlowWorkOpt(String id){
		super(id);
	}

	@ExcelField(title="工单", fieldType=CoverWork.class, value="billId.work_num", align=2, sort=1)
	public CoverWork getBillId() {
		return billId;
	}

	public void setBillId(CoverWork billId) {
		this.billId = billId;
	}
	
	@ExcelField(title="工单编号", align=2, sort=2)
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	@ExcelField(title="流程信息", fieldType=FlowProc.class, value="flowId.flowNo", align=2, sort=3)
	public FlowProc getFlowId() {
		return flowId;
	}

	public void setFlowId(FlowProc flowId) {
		this.flowId = flowId;
	}
	
	@ExcelField(title="操作信息", fieldType=FlowOpt.class, value="optId.optName", align=2, sort=4)
	public FlowOpt getOptId() {
		return optId;
	}

	public void setOptId(FlowOpt optId) {
		this.optId = optId;
	}
	
	@ExcelField(title="操作名称", align=2, sort=5)
	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}
	
	@ExcelField(title="原状态", align=2, sort=6)
	public String getOriginState() {
		return originState;
	}

	public void setOriginState(String originState) {
		this.originState = originState;
	}
	
	@ExcelField(title="结果状态", align=2, sort=7)
	public String getResultState() {
		return resultState;
	}

	public void setResultState(String resultState) {
		this.resultState = resultState;
	}
	
	@ExcelField(title="操作数据", align=2, sort=8)
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@ExcelField(title="操作部门", fieldType=Office.class, value="optOrg.name", align=2, sort=11)
	public Office getOptOrg() {
		return optOrg;
	}

	public void setOptOrg(Office optOrg) {
		this.optOrg = optOrg;
	}
	
	@ExcelField(title="目标部门", fieldType=Office.class, value="targetOrg.name", align=2, sort=12)
	public Office getTargetOrg() {
		return targetOrg;
	}

	public void setTargetOrg(Office targetOrg) {
		this.targetOrg = targetOrg;
	}

	public List<String> getImagesList() {
		return imagesList;
	}

	public void setImagesList(List<String> imagesList) {
		this.imagesList = imagesList;
	}

	public String getOptRemarks() {
		return optRemarks;
	}

	public void setOptRemarks(String optRemarks) {
		this.optRemarks = optRemarks;
	}
}
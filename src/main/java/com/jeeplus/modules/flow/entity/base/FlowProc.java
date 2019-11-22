/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.entity.base;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单流程定义Entity
 * @author crj
 * @version 2019-11-21
 */
public class FlowProc extends DataEntity<FlowProc> {
	
	private static final long serialVersionUID = 1L;
	private String flowNo;		// 流程编号
	private String flowName;		// 流程名称
	private String billType;		// 工单类型
	private String version;		// 版本
	private Date startTime;		// 启用时间
	private Date endTime;		// 结束时间
	private String status;		// 状态
	private Date update_date;		// 更新时间
	
	public FlowProc() {
		super();
	}

	public FlowProc(String id){
		super(id);
	}

	@ExcelField(title="流程编号", align=2, sort=1)
	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}
	
	@ExcelField(title="流程名称", align=2, sort=2)
	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	
	@ExcelField(title="工单类型", dictType="work_type", align=2, sort=3)
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	
	@ExcelField(title="版本", align=2, sort=4)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="启用时间", align=2, sort=5)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=6)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@ExcelField(title="状态", dictType="flow_proc_status", align=2, sort=7)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="更新时间", align=2, sort=11)
	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	
}
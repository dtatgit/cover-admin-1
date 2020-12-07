/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.work;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单配置Entity
 * @author crj
 * @version 2019-11-07
 */
public class CoverWorkConfig extends DataEntity<CoverWorkConfig> {
	
	private static final long serialVersionUID = 1L;
	private String workType;		// 工单类型
	private Integer arrivalTime;		// 到达时长（分）

	private String projectId;//项目ID
	private String projectName;//项目名称
	
	public CoverWorkConfig() {
		super();
	}

	public CoverWorkConfig(String id){
		super(id);
	}

	@ExcelField(title="工单类型", dictType="work_type", align=2, sort=6)
	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}
	
	@ExcelField(title="到达时长（分）", align=2, sort=7)
	public Integer getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Integer arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	@ExcelField(title="所属项目",  align=2, sort=8)
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
}
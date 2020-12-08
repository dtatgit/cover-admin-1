/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.entity.base;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单流程状态Entity
 * @author crj
 * @version 2019-11-21
 */
public class FlowState extends DataEntity<FlowState> {
	
	private static final long serialVersionUID = 1L;
	private String stateName;		// 状态名称
	private String stateCode;		// 状态编码
	private String lifeCycle;		// 生命周期

	private String projectId;//项目ID
	private String projectName;//项目名称
	
	public FlowState() {
		super();
	}

	public FlowState(String id){
		super(id);
	}

	@ExcelField(title="状态名称", align=2, sort=1)
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	@ExcelField(title="状态编码", align=2, sort=2)
	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
	@ExcelField(title="生命周期", dictType="lifecycle", align=2, sort=3)
	public String getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(String lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
}
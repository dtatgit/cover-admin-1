/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.projectInfo.entity;

import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 项目管理Entity
 * @author Peter
 * @version 2020-12-02
 */
public class ProjectInfo extends DataEntity<ProjectInfo> {
	
	private static final long serialVersionUID = 1L;
	private String projectNo;		// 项目编号
	private String projectName;		// 项目名称
	private Office office;		// 机构id
	private String status;		// 项目状态
	
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
	
}
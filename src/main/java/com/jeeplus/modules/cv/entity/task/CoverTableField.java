/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.task;

import com.jeeplus.modules.cv.entity.task.CoverTaskInfo;
import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井盖任务数据权限Entity
 * @author crj
 * @version 2019-05-17
 */
public class CoverTableField extends DataEntity<CoverTableField> {
	
	private static final long serialVersionUID = 1L;
	private CoverTaskInfo coverTaskInfo;		// 所属任务
	private Office office;		// 所属部门
	private String tableName;		// 表名称
	private String tableTitle;		// 表中文名称
	private String fieldName;		// 字段名称
	private String fieldTitle;		// 字段中文名称
	private String isListField;		// 是否列表显示
	private String isEditField;		// 是否修改显示

	private String projectId;//项目ID
	private String projectName;//项目名称
	
	public CoverTableField() {
		super();
	}

	public CoverTableField(String id){
		super(id);
	}

	@ExcelField(title="所属任务", fieldType=CoverTaskInfo.class, value="coverTaskInfo.taskNo", align=2, sort=6)
	public CoverTaskInfo getCoverTaskInfo() {
		return coverTaskInfo;
	}

	public void setCoverTaskInfo(CoverTaskInfo coverTaskInfo) {
		this.coverTaskInfo = coverTaskInfo;
	}
	
	@ExcelField(title="所属部门", fieldType=Office.class, value="office.name", align=2, sort=7)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="表名称", align=2, sort=8)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@ExcelField(title="表中文名称", align=2, sort=9)
	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}
	
	@ExcelField(title="字段名称", align=2, sort=10)
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@ExcelField(title="字段中文名称", align=2, sort=11)
	public String getFieldTitle() {
		return fieldTitle;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}
	
	@ExcelField(title="是否列表显示", dictType="boolean", align=2, sort=12)
	public String getIsListField() {
		return isListField;
	}

	public void setIsListField(String isListField) {
		this.isListField = isListField;
	}
	
	@ExcelField(title="是否修改显示", dictType="boolean", align=2, sort=13)
	public String getIsEditField() {
		return isEditField;
	}

	public void setIsEditField(String isEditField) {
		this.isEditField = isEditField;
	}


	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	@ExcelField(title="所属项目",  align=2, sort=14)
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
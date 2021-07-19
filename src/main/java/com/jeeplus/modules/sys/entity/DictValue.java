/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.projectInfo.entity.ProjectInfo;

/**
 * 数据字典Entity
 * @author lgf
 * @version 2017-01-16
 */
public class DictValue extends DataEntity<DictValue> {
	
	private static final long serialVersionUID = 1L;
	private String label;		// 标签名
	private String value;		// 键值
	private String sort;		// 排序
	private DictType dictType;		// 外键 父类
	private String projectId;//项目ID
	private String projectName;//项目名称

	private ProjectInfo projectInfo;//所属项目
	
	public DictValue() {
		super();
	}

	public DictValue(String id){
		super(id);
	}

	public DictValue(DictType dictType){
		this.dictType = dictType;
	}

	@ExcelField(title="标签名", align=2, sort=1)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@ExcelField(title="键值", align=2, sort=2)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@ExcelField(title="排序", align=2, sort=3)
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public DictType getDictType() {
		return dictType;
	}

	public void setDictType(DictType dictType) {
		this.dictType = dictType;
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

	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}

	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.entity.base;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户组织关系配置Entity
 * @author crj
 * @version 2019-11-21
 */
public class FlowUserOrg extends DataEntity<FlowUserOrg> {
	
	private static final long serialVersionUID = 1L;
	private User sourceUser;		// 源用户
	private Office sourceOrg;		// 源组织
	private User targetUser;		// 目标用户
	private Office targetOrg;		// 目标组织
	private String relationship;		// 相对关系

	private String projectId;//项目ID
	private String projectName;//项目名称
	
	public FlowUserOrg() {
		super();
	}

	public FlowUserOrg(String id){
		super(id);
	}

	@ExcelField(title="源用户", fieldType=User.class, value="sourceUser.name", align=2, sort=1)
	public User getSourceUser() {
		return sourceUser;
	}

	public void setSourceUser(User sourceUser) {
		this.sourceUser = sourceUser;
	}
	
	@ExcelField(title="源组织", fieldType=Office.class, value="sourceOrg.name", align=2, sort=2)
	public Office getSourceOrg() {
		return sourceOrg;
	}

	public void setSourceOrg(Office sourceOrg) {
		this.sourceOrg = sourceOrg;
	}
	
	@ExcelField(title="目标用户", fieldType=User.class, value="targetUser.name", align=2, sort=3)
	public User getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}
	
	@ExcelField(title="目标组织", fieldType=Office.class, value="targetOrg.name", align=2, sort=4)
	public Office getTargetOrg() {
		return targetOrg;
	}

	public void setTargetOrg(Office targetOrg) {
		this.targetOrg = targetOrg;
	}
	
	@ExcelField(title="相对关系", align=2, sort=5)
	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
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
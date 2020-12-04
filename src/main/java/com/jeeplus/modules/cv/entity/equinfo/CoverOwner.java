/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.equinfo;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井盖权属单位Entity
 * @author crj
 * @version 2019-05-09
 */
public class CoverOwner extends DataEntity<CoverOwner> {
	
	private static final long serialVersionUID = 1L;
	private String coverId;		// cover_id
	private String ownerId;		// owner_id
	private String ownerName;		// owner_name
	private String ownerType;		// owner_type
	private String projectId;//项目ID
	private String projectName;//项目名称
	public CoverOwner() {
		super();
	}

	public CoverOwner(String id){
		super(id);
	}

	@ExcelField(title="cover_id", align=2, sort=1)
	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}
	
	@ExcelField(title="owner_id", align=2, sort=2)
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	@ExcelField(title="owner_name", align=2, sort=3)
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	@ExcelField(title="owner_type", align=2, sort=4)
	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
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
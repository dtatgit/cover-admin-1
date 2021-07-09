/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.equinfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.User;

import java.util.Date;

/**
 * 井盖审核信息Entity
 * @author crj
 * @version 2019-04-24
 */
public class CoverAudit extends DataEntity<CoverAudit> {
	
	private static final long serialVersionUID = 1L;
	private Cover cover;		// 井盖信息
	private Date auditTime;		// 审核时间
	private User auditUser;		// 审核人
	private String auditStatus;		// 审核状态
	private String auditResult;		// 审核结果
	private String applyItem;		// 申请事项
	private Date beginAuditTime;		// 开始 审核时间
	private Date endAuditTime;		// 结束 审核时间

	private Area area;//井盖所属区域

	private String projectId;//项目ID
	private String projectName;//项目名称

	private String imgIds;

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public CoverAudit() {
		super();
	}

	public CoverAudit(String id){
		super(id);
	}

	@ExcelField(title="井盖信息", fieldType=Cover.class, value="cover.no", align=2, sort=6)
	public Cover getCover() {
		return cover;
	}

	public void setCover(Cover cover) {
		this.cover = cover;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核时间", align=2, sort=7)
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	@ExcelField(title="审核人", fieldType=User.class, value="auditUser.name", align=2, sort=8)
	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}
	
	@ExcelField(title="审核状态", dictType="audit_status", align=2, sort=9)
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@ExcelField(title="审核结果", align=2, sort=10)
	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	
	@ExcelField(title="申请事项", dictType="apply_item", align=2, sort=11)
	public String getApplyItem() {
		return applyItem;
	}

	public void setApplyItem(String applyItem) {
		this.applyItem = applyItem;
	}
	
	public Date getBeginAuditTime() {
		return beginAuditTime;
	}

	public void setBeginAuditTime(Date beginAuditTime) {
		this.beginAuditTime = beginAuditTime;
	}
	
	public Date getEndAuditTime() {
		return endAuditTime;
	}

	public void setEndAuditTime(Date endAuditTime) {
		this.endAuditTime = endAuditTime;
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

	public String getImgIds() {
		return imgIds;
	}

	public void setImgIds(String imgIds) {
		this.imgIds = imgIds;
	}
}
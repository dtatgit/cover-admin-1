/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.task;

import com.jeeplus.modules.cv.entity.task.CoverTaskInfo;
import com.jeeplus.modules.cv.entity.equinfo.Cover;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 任务处理明细Entity
 * @author crj
 * @version 2019-05-16
 */
public class CoverTaskProcess extends DataEntity<CoverTaskProcess> {
	
	private static final long serialVersionUID = 1L;
	private CoverTaskInfo coverTaskInfo;		// 所属任务
	private String taskStatus;		// 任务状态
	private Cover cover;		// 井盖信息
	private Date auditTime;		// 审核时间
	private User auditUser;		// 审核人
	private String auditStatus;		// 审核状态
	private String auditResult;		// 审核结果
	private String applyItem;		// 申请事项

	//add by 2019-06-20 为解决性能问题，不关联cover表，增加3个字段
	private BigDecimal longitude;		// 经度
	private BigDecimal latitude;		// 纬度
	private String street;		// 地址：路（街巷）

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public CoverTaskProcess() {
		super();
	}

	public CoverTaskProcess(String id){
		super(id);
	}

	@ExcelField(title="所属任务", fieldType=CoverTaskInfo.class, value="coverTaskInfo.taskNo", align=2, sort=6)
	public CoverTaskInfo getCoverTaskInfo() {
		return coverTaskInfo;
	}

	public void setCoverTaskInfo(CoverTaskInfo coverTaskInfo) {
		this.coverTaskInfo = coverTaskInfo;
	}
	
	@ExcelField(title="任务状态", dictType="task_status", align=2, sort=7)
	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	@ExcelField(title="井盖信息", fieldType=Cover.class, value="cover.no", align=2, sort=8)
	public Cover getCover() {
		return cover;
	}

	public void setCover(Cover cover) {
		this.cover = cover;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核时间", align=2, sort=9)
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	@ExcelField(title="审核人", fieldType=User.class, value="auditUser.name", align=2, sort=10)
	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}
	
	@ExcelField(title="审核状态", dictType="audit_status", align=2, sort=11)
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@ExcelField(title="审核结果", align=2, sort=12)
	public String getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	
	@ExcelField(title="申请事项", dictType="apply_item", align=2, sort=14)
	public String getApplyItem() {
		return applyItem;
	}

	public void setApplyItem(String applyItem) {
		this.applyItem = applyItem;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
}
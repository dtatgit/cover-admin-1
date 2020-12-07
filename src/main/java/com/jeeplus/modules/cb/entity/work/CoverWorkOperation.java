/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.work;

import com.jeeplus.modules.cb.entity.work.CoverWork;
import java.util.Date;
import java.util.List;

import com.jeeplus.modules.cv.entity.equinfo.CoverImage;
import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单操作记录Entity
 * @author crj
 * @version 2019-06-26
 */
public class CoverWorkOperation extends DataEntity<CoverWorkOperation> {
	
	private static final long serialVersionUID = 1L;
	private CoverWork coverWork;		// 工单信息
	private String operationType;		// 操作类型
	private String operationStatus;		// 操作状态
	private String operationResult;		// 操作结果
	private Office createDepart;		// 操作部门
	private Date beginCreateDate;		// 开始 操作时间
	private Date endCreateDate;		// 结束 操作时间

	private String projectId;//项目ID
	private String projectName;//项目名称

	private List<CoverWorkOperationDetail> workOperationDetail;//工单操作记录明细信息
	
	public CoverWorkOperation() {
		super();
	}

	public CoverWorkOperation(String id){
		super(id);
	}

	@ExcelField(title="工单信息", fieldType=CoverWork.class, value="coverWork.workNum", align=2, sort=4)
	public CoverWork getCoverWork() {
		return coverWork;
	}

	public void setCoverWork(CoverWork coverWork) {
		this.coverWork = coverWork;
	}
	
	@ExcelField(title="操作类型", dictType="work_operation_Type", align=2, sort=5)
	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	@ExcelField(title="操作状态", align=2, sort=6)
	public String getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}
	
	@ExcelField(title="操作结果", align=2, sort=7)
	public String getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(String operationResult) {
		this.operationResult = operationResult;
	}
	
	@ExcelField(title="操作部门", fieldType=Office.class, value="createDepart.name", align=2, sort=10)
	public Office getCreateDepart() {
		return createDepart;
	}

	public void setCreateDepart(Office createDepart) {
		this.createDepart = createDepart;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public List<CoverWorkOperationDetail> getWorkOperationDetail() {
		return workOperationDetail;
	}

	public void setWorkOperationDetail(List<CoverWorkOperationDetail> workOperationDetail) {
		this.workOperationDetail = workOperationDetail;
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
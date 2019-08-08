/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.work;

import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单信息Entity
 * @author crj
 * @version 2019-06-26
 */
public class CoverWork extends DataEntity<CoverWork> {
	
	private static final long serialVersionUID = 1L;
	private Cover cover;		// 井盖信息
	private String coverNo;		// 井盖编号
	private String coverBellId;		// 井铃ID
	private String workNum;		// 工单编号
	private String workType;		// 工单类型
	private String workStatus;		// 工单状态
	private String source;		// 工单来源
	private String constructionContent;		// 施工内容
	private User constructionUser;		// 施工人员
	private String phone;		// 联系电话
	private Office constructionDepart;		// 施工部门
	private String workLevel;		// 紧急程度
	private String createDepart;		// 创建部门
	private String updateDepart;		// 更新部门

	private String batch;		// 工单批次
	
	//临时变量
	private String ids;		// 工单编号
	private String workNums;		// 工单编号
	private String coverIds;//井盖IDS
	private String coverNos;//井盖编号
	//审核信息
	private String operationStatus;		// 审核状态
	private String operationResult;		// 结果描述

	public String getCoverIds() {
		return coverIds;
	}

	public void setCoverIds(String coverIds) {
		this.coverIds = coverIds;
	}

	public String getCoverNos() {
		return coverNos;
	}

	public void setCoverNos(String coverNos) {
		this.coverNos = coverNos;
	}
	
	public CoverWork() {
		super();
	}

	public CoverWork(String id){
		super(id);
	}

	@ExcelField(title="井盖信息", fieldType=Cover.class, value="cover.no", align=2, sort=6)
	public Cover getCover() {
		return cover;
	}

	public void setCover(Cover cover) {
		this.cover = cover;
	}
	
	@ExcelField(title="井盖编号", align=2, sort=7)
	public String getCoverNo() {
		return coverNo;
	}

	public void setCoverNo(String coverNo) {
		this.coverNo = coverNo;
	}
	
	@ExcelField(title="井铃ID", align=2, sort=8)
	public String getCoverBellId() {
		return coverBellId;
	}

	public void setCoverBellId(String coverBellId) {
		this.coverBellId = coverBellId;
	}
	
	@ExcelField(title="工单编号", align=2, sort=9)
	public String getWorkNum() {
		return workNum;
	}

	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}
	
	@ExcelField(title="工单类型", dictType="work_type", align=2, sort=10)
	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}
	
	@ExcelField(title="工单状态", dictType="work_status", align=2, sort=11)
	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	
	@ExcelField(title="工单来源", align=2, sort=12)
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	@ExcelField(title="施工内容", align=2, sort=13)
	public String getConstructionContent() {
		return constructionContent;
	}

	public void setConstructionContent(String constructionContent) {
		this.constructionContent = constructionContent;
	}
	
	@ExcelField(title="施工人员", fieldType=User.class, value="constructionUser.name", align=2, sort=14)
	public User getConstructionUser() {
		return constructionUser;
	}

	public void setConstructionUser(User constructionUser) {
		this.constructionUser = constructionUser;
	}
	
	@ExcelField(title="联系电话", align=2, sort=15)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="施工部门", fieldType=Office.class, value="constructionDepart.name", align=2, sort=16)
	public Office getConstructionDepart() {
		return constructionDepart;
	}

	public void setConstructionDepart(Office constructionDepart) {
		this.constructionDepart = constructionDepart;
	}
	
	@ExcelField(title="紧急程度", dictType="work_level", align=2, sort=17)
	public String getWorkLevel() {
		return workLevel;
	}

	public void setWorkLevel(String workLevel) {
		this.workLevel = workLevel;
	}
	
	@ExcelField(title="创建部门", align=2, sort=18)
	public String getCreateDepart() {
		return createDepart;
	}

	public void setCreateDepart(String createDepart) {
		this.createDepart = createDepart;
	}
	
	@ExcelField(title="更新部门", align=2, sort=19)
	public String getUpdateDepart() {
		return updateDepart;
	}

	public void setUpdateDepart(String updateDepart) {
		this.updateDepart = updateDepart;
	}

	
	@ExcelField(title="工单批次", align=2, sort=21)
	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getWorkNums() {
		return workNums;
	}

	public void setWorkNums(String workNums) {
		this.workNums = workNums;
	}

	public String getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(String operationStatus) {
		this.operationStatus = operationStatus;
	}

	public String getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(String operationResult) {
		this.operationResult = operationResult;
	}
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.work;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 超时工单Entity
 * @author crj
 * @version 2019-11-07
 */
public class CoverWorkOvertime extends DataEntity<CoverWorkOvertime> {
	
	private static final long serialVersionUID = 1L;
	private String coverWorkId;		// 工单ID
	private String workNum;		// 工单编号
	private String workType;		// 工单类型
	private String workStatus;		// 工单状态
	private User constructionUser;		// 施工人员
	private Office constructionDepart;		// 施工部门
	private String overType;		// 超时类型
	private Integer overTime;		// 超时时长（分）
	
	public CoverWorkOvertime() {
		super();
	}

	public CoverWorkOvertime(String id){
		super(id);
	}

	@ExcelField(title="工单ID", align=2, sort=7)
	public String getCoverWorkId() {
		return coverWorkId;
	}

	public void setCoverWorkId(String coverWorkId) {
		this.coverWorkId = coverWorkId;
	}
	
	@ExcelField(title="工单编号", align=2, sort=8)
	public String getWorkNum() {
		return workNum;
	}

	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}
	
	@ExcelField(title="工单类型", dictType="work_type", align=2, sort=9)
	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}
	
	@ExcelField(title="工单状态", dictType="work_status", align=2, sort=10)
	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	
	@ExcelField(title="施工人员", fieldType=User.class, value="constructionUser.name", align=2, sort=11)
	public User getConstructionUser() {
		return constructionUser;
	}

	public void setConstructionUser(User constructionUser) {
		this.constructionUser = constructionUser;
	}
	
	@ExcelField(title="施工部门", fieldType=Office.class, value="constructionDepart.name", align=2, sort=12)
	public Office getConstructionDepart() {
		return constructionDepart;
	}

	public void setConstructionDepart(Office constructionDepart) {
		this.constructionDepart = constructionDepart;
	}
	
	@ExcelField(title="超时类型", dictType="over_type", align=2, sort=13)
	public String getOverType() {
		return overType;
	}

	public void setOverType(String overType) {
		this.overType = overType;
	}
	
	@ExcelField(title="超时时长（分）", align=2, sort=14)
	public Integer getOverTime() {
		return overTime;
	}

	public void setOverTime(Integer overTime) {
		this.overTime = overTime;
	}
	
}
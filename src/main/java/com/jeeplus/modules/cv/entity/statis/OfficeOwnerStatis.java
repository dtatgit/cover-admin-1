/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.statis;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import javax.persistence.Transient;

/**
 * 维护部门权属单位统计Entity
 * @author crj
 * @version 2021-03-22
 */
public class OfficeOwnerStatis extends DataEntity<OfficeOwnerStatis> {
	
	private static final long serialVersionUID = 1L;
	private String officeId;		// 部门
	private String officeName;		// 部门
	private String ownerDepart;		// 权属单位
	private String addWorkNum;		// 工单总数（当天新增）
	private String completeWorkNum;		// 已完成工单总数（当天）
	private String proWorkNum;		// 未完成工单总数（累计）
	private String workNumTotal;		// 工单总数（累计）
	private String completeWorkNumTotal;		// 已完成工单总数（累计）
	private String statisTime;		// 统计时间
	private String flag;		// 信息标识
    private String completionRate;//完成率
	
	public OfficeOwnerStatis() {
		super();
	}

	public OfficeOwnerStatis(String id){
		super(id);
	}

	@ExcelField(title="部门", align=2, sort=7)
	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
	@ExcelField(title="部门", align=2, sort=8)
	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	
	@ExcelField(title="权属单位", align=2, sort=9)
	public String getOwnerDepart() {
		return ownerDepart;
	}

	public void setOwnerDepart(String ownerDepart) {
		this.ownerDepart = ownerDepart;
	}
	
	@ExcelField(title="工单总数（当天新增）", align=2, sort=10)
	public String getAddWorkNum() {
		return addWorkNum;
	}

	public void setAddWorkNum(String addWorkNum) {
		this.addWorkNum = addWorkNum;
	}
	
	@ExcelField(title="已完成工单总数（当天）", align=2, sort=11)
	public String getCompleteWorkNum() {
		return completeWorkNum;
	}

	public void setCompleteWorkNum(String completeWorkNum) {
		this.completeWorkNum = completeWorkNum;
	}
	
	@ExcelField(title="未完成工单总数（累计）", align=2, sort=12)
	public String getProWorkNum() {
		return proWorkNum;
	}

	public void setProWorkNum(String proWorkNum) {
		this.proWorkNum = proWorkNum;
	}
	
	@ExcelField(title="工单总数（累计）", align=2, sort=13)
	public String getWorkNumTotal() {
		return workNumTotal;
	}

	public void setWorkNumTotal(String workNumTotal) {
		this.workNumTotal = workNumTotal;
	}
	
	@ExcelField(title="已完成工单总数（累计）", align=2, sort=14)
	public String getCompleteWorkNumTotal() {
		return completeWorkNumTotal;
	}

	public void setCompleteWorkNumTotal(String completeWorkNumTotal) {
		this.completeWorkNumTotal = completeWorkNumTotal;
	}
	
	@ExcelField(title="统计时间", align=2, sort=15)
	public String getStatisTime() {
		return statisTime;
	}

	public void setStatisTime(String statisTime) {
		this.statisTime = statisTime;
	}
	
	@ExcelField(title="信息标识", align=2, sort=16)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

    public String getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(String completionRate) {
        this.completionRate = completionRate;
    }
	@Transient
	private String statisMethods;//统计方式

	public String getStatisMethods() {
		return statisMethods;
	}

	public void setStatisMethods(String statisMethods) {
		this.statisMethods = statisMethods;
	}
}
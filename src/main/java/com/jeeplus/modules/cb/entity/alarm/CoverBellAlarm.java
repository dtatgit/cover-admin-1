/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.alarm;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井卫报警信息Entity
 * @author crj
 * @version 2019-06-24
 */
public class CoverBellAlarm extends DataEntity<CoverBellAlarm> {
	
	private static final long serialVersionUID = 1L;
	private String coverBellId;		// 井卫ID
	private String bellNo;		// 井卫编号
	private String coverId;		// 井盖ID
	private String coverNo;		// 井盖编号
	private String alarmNum;		// 报警编号
	private String alarmType;		// 报警类型
	private Double currentValue;		// 当前值
	private Date alarmDate;		// 报警时间
	private String isGwo;		// 是否生成工单
	private Date beginAlarmDate;		// 开始 报警时间
	private Date endAlarmDate;		// 结束 报警时间

	private String projectId; //项目id
	private String projectName; //项目名称

	public CoverBellAlarm() {
		super();
	}

	public CoverBellAlarm(String id){
		super(id);
	}

	@ExcelField(title="井卫ID", align=2, sort=6)
	public String getCoverBellId() {
		return coverBellId;
	}

	public void setCoverBellId(String coverBellId) {
		this.coverBellId = coverBellId;
	}
	
	@ExcelField(title="井卫编号", align=2, sort=7)
	public String getBellNo() {
		return bellNo;
	}

	public void setBellNo(String bellNo) {
		this.bellNo = bellNo;
	}
	
	@ExcelField(title="井盖ID", align=2, sort=8)
	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}
	
	@ExcelField(title="井盖编号", align=2, sort=9)
	public String getCoverNo() {
		return coverNo;
	}

	public void setCoverNo(String coverNo) {
		this.coverNo = coverNo;
	}
	
	@ExcelField(title="报警编号", align=2, sort=10)
	public String getAlarmNum() {
		return alarmNum;
	}

	public void setAlarmNum(String alarmNum) {
		this.alarmNum = alarmNum;
	}
	
	@ExcelField(title="报警类型", dictType="alarm_type", align=2, sort=11)
	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	
	@ExcelField(title="当前值", align=2, sort=12)
	public Double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(Double currentValue) {
		this.currentValue = currentValue;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="报警时间", align=2, sort=13)
	public Date getAlarmDate() {
		return alarmDate;
	}

	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}
	
	@ExcelField(title="是否生成工单", dictType="boolean", align=2, sort=14)
	public String getIsGwo() {
		return isGwo;
	}

	public void setIsGwo(String isGwo) {
		this.isGwo = isGwo;
	}
	
	public Date getBeginAlarmDate() {
		return beginAlarmDate;
	}

	public void setBeginAlarmDate(Date beginAlarmDate) {
		this.beginAlarmDate = beginAlarmDate;
	}
	
	public Date getEndAlarmDate() {
		return endAlarmDate;
	}

	public void setEndAlarmDate(Date endAlarmDate) {
		this.endAlarmDate = endAlarmDate;
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
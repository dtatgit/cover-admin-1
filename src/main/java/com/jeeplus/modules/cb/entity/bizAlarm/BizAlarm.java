/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.bizAlarm;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 业务报警Entity
 * @author Peter
 * @version 2020-10-13
 */
public class BizAlarm extends DataEntity<BizAlarm> {
	
	private static final long serialVersionUID = 1L;
	private String alarmNo;		// 报警编号
	private String coverId;		// 井盖id
	private String coverNo;		// 井盖编号
	private String coverBellId;		// 井卫id
	private String coverBellNo;		// 井卫no
	private String address;		// 地址
	private String alarmType;		// 报警类型
	private Date alarmTime;		// 报警时间
	private String isCreateWork;		// 是否派单处理
	private String coverWorkId;
	private String dealStatus;  //处理状态

	private Date beginDate;//开始时间

	private Date endDate; //结束时间

	public BizAlarm() {
		super();
	}

	public BizAlarm(String id){
		super(id);
	}

	@ExcelField(title="报警编号", align=2, sort=7)
	public String getAlarmNo() {
		return alarmNo;
	}

	public void setAlarmNo(String alarmNo) {
		this.alarmNo = alarmNo;
	}
	
	@ExcelField(title="井盖id", align=2, sort=8)
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
	
	@ExcelField(title="井卫id", align=2, sort=10)
	public String getCoverBellId() {
		return coverBellId;
	}

	public void setCoverBellId(String coverBellId) {
		this.coverBellId = coverBellId;
	}
	
	@ExcelField(title="地址", align=2, sort=11)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="报警类型", dictType="del_flag", align=2, sort=12)
	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="报警时间", align=2, sort=13)
	public Date getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}
	
	@ExcelField(title="是否派单处理", dictType="del_flag", align=2, sort=14)
	public String getIsCreateWork() {
		return StringUtils.isNotBlank(this.coverWorkId) ? "是" : "否";
	}

	public void setIsCreateWork(String isCreateWork) {
		this.isCreateWork = isCreateWork;
	}


	public String getCoverWorkId() {
		return coverWorkId;
	}

	public void setCoverWorkId(String coverWorkId) {
		this.coverWorkId = coverWorkId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}


	public String getCoverBellNo() {
		return coverBellNo;
	}

	public void setCoverBellNo(String coverBellNo) {
		this.coverBellNo = coverBellNo;
	}
}
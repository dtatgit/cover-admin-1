/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.work;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单巡查Entity
 * @author crj
 * @version 2021-02-08
 */
public class CoverWorkInspection extends DataEntity<CoverWorkInspection> {
	
	private static final long serialVersionUID = 1L;
	private String coverWorkNum;		// 工单编号
	private String coverWorkId;		// 工单ID
	private String area;		// 所属区域
	private String longitude;		// 经度
	private String latitude;		// 纬度
	private String signInPersonId;		// 签到人
	private String signInPersonName;		// 签到人
	private Date signInTime;		// 签到时间
	private Date beginSignInTime;		// 开始 签到时间
	private Date endSignInTime;		// 结束 签到时间
	
	public CoverWorkInspection() {
		super();
	}

	public CoverWorkInspection(String id){
		super(id);
	}

	@ExcelField(title="工单编号", align=2, sort=6)
	public String getCoverWorkNum() {
		return coverWorkNum;
	}

	public void setCoverWorkNum(String coverWorkNum) {
		this.coverWorkNum = coverWorkNum;
	}
	
	@ExcelField(title="工单ID", align=2, sort=7)
	public String getCoverWorkId() {
		return coverWorkId;
	}

	public void setCoverWorkId(String coverWorkId) {
		this.coverWorkId = coverWorkId;
	}
	
	@ExcelField(title="所属区域", align=2, sort=8)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	@ExcelField(title="经度", align=2, sort=9)
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	@ExcelField(title="纬度", align=2, sort=10)
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	@ExcelField(title="签到人", align=2, sort=11)
	public String getSignInPersonId() {
		return signInPersonId;
	}

	public void setSignInPersonId(String signInPersonId) {
		this.signInPersonId = signInPersonId;
	}
	
	@ExcelField(title="签到人", align=2, sort=12)
	public String getSignInPersonName() {
		return signInPersonName;
	}

	public void setSignInPersonName(String signInPersonName) {
		this.signInPersonName = signInPersonName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="签到时间", align=2, sort=13)
	public Date getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(Date signInTime) {
		this.signInTime = signInTime;
	}
	
	public Date getBeginSignInTime() {
		return beginSignInTime;
	}

	public void setBeginSignInTime(Date beginSignInTime) {
		this.beginSignInTime = beginSignInTime;
	}
	
	public Date getEndSignInTime() {
		return endSignInTime;
	}

	public void setEndSignInTime(Date endSignInTime) {
		this.endSignInTime = endSignInTime;
	}
		
}
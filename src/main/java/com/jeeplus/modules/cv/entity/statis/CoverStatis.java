/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.statis;

import java.util.Date;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井盖相关统计Entity
 * @author crj
 * @version 2021-02-09
 */
public class CoverStatis extends DataEntity<CoverStatis> {

	private static final long serialVersionUID = 1L;
	private String district;		// 区域
	private String coverType;		// 井盖类型
	private String ownerDepart;		// 权属单位
	private String coverNum;		// 井盖数
	private String installEqu;		// 已安装设备数
	private String onlineNum;		// 当前在线数
	private String offlineNum;		// 当前离线数
	private String coverAlarmNum;		// 报警井盖数
	private String alarmTotalNum;		// 报警总数
	private String addWorkNum;		// 工单总数（当天新增）
	private String completeWorkNum;		// 已完成工单总数（当天）
	private String proWorkNum;		// 未完成工单总数（累计）
	private String statisTime;		// 统计时间
	private String flag;		// 信息标识
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间

	public CoverStatis() {
		super();
	}

	public CoverStatis(String id){
		super(id);
	}

	@ExcelField(title="区域", align=2, sort=7)
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@ExcelField(title="井盖类型", dictType="cover_type", align=2, sort=8)
	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}

	@ExcelField(title="权属单位", dictType="cover_owner_depart", align=2, sort=9)
	public String getOwnerDepart() {
		return ownerDepart;
	}

	public void setOwnerDepart(String ownerDepart) {
		this.ownerDepart = ownerDepart;
	}

	@ExcelField(title="井盖数", align=2, sort=10)
	public String getCoverNum() {
		return coverNum;
	}

	public void setCoverNum(String coverNum) {
		this.coverNum = coverNum;
	}

	@ExcelField(title="已安装设备数", align=2, sort=11)
	public String getInstallEqu() {
		return installEqu;
	}

	public void setInstallEqu(String installEqu) {
		this.installEqu = installEqu;
	}

	@ExcelField(title="当前在线数", align=2, sort=12)
	public String getOnlineNum() {
		return onlineNum;
	}

	public void setOnlineNum(String onlineNum) {
		this.onlineNum = onlineNum;
	}

	@ExcelField(title="当前离线数", align=2, sort=13)
	public String getOfflineNum() {
		return offlineNum;
	}

	public void setOfflineNum(String offlineNum) {
		this.offlineNum = offlineNum;
	}

	@ExcelField(title="报警井盖数", align=2, sort=14)
	public String getCoverAlarmNum() {
		return coverAlarmNum;
	}

	public void setCoverAlarmNum(String coverAlarmNum) {
		this.coverAlarmNum = coverAlarmNum;
	}

	@ExcelField(title="报警总数", align=2, sort=15)
	public String getAlarmTotalNum() {
		return alarmTotalNum;
	}

	public void setAlarmTotalNum(String alarmTotalNum) {
		this.alarmTotalNum = alarmTotalNum;
	}

	@ExcelField(title="工单总数（当天新增）", align=2, sort=16)
	public String getAddWorkNum() {
		return addWorkNum;
	}

	public void setAddWorkNum(String addWorkNum) {
		this.addWorkNum = addWorkNum;
	}

	@ExcelField(title="已完成工单总数（当天）", align=2, sort=17)
	public String getCompleteWorkNum() {
		return completeWorkNum;
	}

	public void setCompleteWorkNum(String completeWorkNum) {
		this.completeWorkNum = completeWorkNum;
	}

	@ExcelField(title="未完成工单总数（累计）", align=2, sort=18)
	public String getProWorkNum() {
		return proWorkNum;
	}

	public void setProWorkNum(String proWorkNum) {
		this.proWorkNum = proWorkNum;
	}

	@ExcelField(title="统计时间", align=2, sort=19)
	public String getStatisTime() {
		return statisTime;
	}

	public void setStatisTime(String statisTime) {
		this.statisTime = statisTime;
	}

	@ExcelField(title="信息标识", align=2, sort=20)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

}
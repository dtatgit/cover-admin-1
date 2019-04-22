/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.statis;

import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 窨井盖采集统计Entity
 * @author crj
 * @version 2019-04-22
 */
public class CoverCollectStatis extends DataEntity<CoverCollectStatis> {
	
	private static final long serialVersionUID = 1L;
	private User collectUser;		// 采集人员
	private String collectNum;		// 采集数量
	private String startDate;		// 开始时间
	private String endDate;		// 结束时间
	private Date statisDate;		// 统计时间
	private Date beginStatisDate;		// 开始 统计时间
	private Date endStatisDate;		// 结束 统计时间
	
	public CoverCollectStatis() {
		super();
	}

	public CoverCollectStatis(String id){
		super(id);
	}

	@ExcelField(title="采集人员", fieldType=User.class, value="collectUser.name", align=2, sort=5)
	public User getCollectUser() {
		return collectUser;
	}

	public void setCollectUser(User collectUser) {
		this.collectUser = collectUser;
	}
	
	@ExcelField(title="采集数量", align=2, sort=6)
	public String getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(String collectNum) {
		this.collectNum = collectNum;
	}
	
	@ExcelField(title="开始时间", align=2, sort=7)
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@ExcelField(title="结束时间", align=2, sort=8)
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="统计时间", align=2, sort=9)
	public Date getStatisDate() {
		return statisDate;
	}

	public void setStatisDate(Date statisDate) {
		this.statisDate = statisDate;
	}
	
	public Date getBeginStatisDate() {
		return beginStatisDate;
	}

	public void setBeginStatisDate(Date beginStatisDate) {
		this.beginStatisDate = beginStatisDate;
	}
	
	public Date getEndStatisDate() {
		return endStatisDate;
	}

	public void setEndStatisDate(Date endStatisDate) {
		this.endStatisDate = endStatisDate;
	}
		
}
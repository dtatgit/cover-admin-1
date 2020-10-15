/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.coverBizAlarm;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井盖业务报警Entity
 * @author Peter
 * @version 2020-10-13
 */
public class CoverBizAlarm extends DataEntity<CoverBizAlarm> {
	
	private static final long serialVersionUID = 1L;
	private String coverId;		// 井盖id
	private String alarmType;		// 业务报警类型
	
	public CoverBizAlarm() {
		super();
	}

	public CoverBizAlarm(String coverId) {
		this.coverId = coverId;
	}

	public CoverBizAlarm(String coverId, String alarmType) {
		this.coverId = coverId;
		this.alarmType = alarmType;
	}

	@ExcelField(title="井盖id", align=2, sort=1)
	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}
	
	@ExcelField(title="业务报警类型", align=2, sort=2)
	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.equinfo;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井铃状态上报Entity
 * @author crj
 * @version 2019-06-24
 */
public class CoverBellState extends DataEntity<CoverBellState> {
	
	private static final long serialVersionUID = 1L;
	private String coverBellId;		// 井铃ID
	private Double voltage;		// 电压值
	private Double waterLevel;		// 水位值
	private Double temperature;		// 温度值
	private Double signalValue;		// 信号值
	
	public CoverBellState() {
		super();
	}

	public CoverBellState(String id){
		super(id);
	}

	@ExcelField(title="井铃ID", align=2, sort=5)
	public String getCoverBellId() {
		return coverBellId;
	}

	public void setCoverBellId(String coverBellId) {
		this.coverBellId = coverBellId;
	}
	
	@ExcelField(title="电压值", align=2, sort=6)
	public Double getVoltage() {
		return voltage;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}
	
	@ExcelField(title="水位值", align=2, sort=7)
	public Double getWaterLevel() {
		return waterLevel;
	}

	public void setWaterLevel(Double waterLevel) {
		this.waterLevel = waterLevel;
	}
	
	@ExcelField(title="温度值", align=2, sort=8)
	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	
	@ExcelField(title="信号值", align=2, sort=9)
	public Double getSignalValue() {
		return signalValue;
	}

	public void setSignalValue(Double signalValue) {
		this.signalValue = signalValue;
	}
	
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.equinfo;

import java.util.Date;
import java.util.List;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.api.vo.ParamResVo;
import com.jeeplus.modules.cv.entity.equinfo.Cover;

/**
 * 井卫设备信息Entity
 * @author crj
 * @version 2019-06-24
 */
public class CoverBell extends DataEntity<CoverBell> {
	
	private static final long serialVersionUID = 1L;
	private String coverId;		// 井盖ID
	private String coverNo;		// 井盖编号
	private String bellNo;		// 井卫编号
	private String bellModel;		// 井卫型号
	private String bellType;		// 设备类型
	private String city; //井盖城市
	private String district;//井盖区
	private String township;//井盖街道
	private String purpose;//井盖用途

	private String version;		// 固件版本号
	private String imei;		// 设备IMEI号
	private String sim;		// 设备SIM卡号
	private String iccid;		// 设备ICCID
	private String rssi; //信号强度
	private String workMode; //工作模式
	private String batteryVoltage; //电池电压
	private String lightVoltage; //光感电压
	private String angle; //当前角度
	private String temperature; //当前温度
	private String depth; //水深
	private String angleThreshold; //当前角度
	private String temperatureThreshold; //当前温度

	private String initDepth; //初始深度
	private String waterLevelThreshold; //水位阈值
	private String bellStatus;		// 生命周期
	private String workStatus;		// 工作状态
	private String defenseStatus;		// 设防状态
	private String createOffice;		// 创建部门
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间

	private Cover cover;		// 井盖信息

	private String projectId; //项目id
	private String projectName; //项目名称

	private List<ParamResVo> paramList; //井卫对应的参数



	public CoverBell() {
		super();
	}

	public CoverBell(String id){
		super(id);
	}


	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}
	

	public String getCoverNo() {
		return coverNo;
	}

	public void setCoverNo(String coverNo) {
		this.coverNo = coverNo;
	}
	
	@ExcelField(title="井卫编号", align=2, sort=1)
	public String getBellNo() {
		return bellNo;
	}

	public void setBellNo(String bellNo) {
		this.bellNo = bellNo;
	}
	
	@ExcelField(title="井卫型号", align=2, sort=2)
	public String getBellModel() {
		return bellModel;
	}

	public void setBellModel(String bellModel) {
		this.bellModel = bellModel;
	}
	
	//@ExcelField(title="设备类型", dictType="bellType", align=2, sort=8)
	public String getBellType() {
		return bellType;
	}

	public void setBellType(String bellType) {
		this.bellType = bellType;
	}
	
	//@ExcelField(title="固件版本号", align=2, sort=9)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@ExcelField(title="设备IMEI号", align=2, sort=3)
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
	
	@ExcelField(title="设备SIM卡号", align=2, sort=4)
	public String getSim() {
		return sim;
	}

	public void setSim(String sim) {
		this.sim = sim;
	}
	
	//@ExcelField(title="生命周期", dictType="bell_status", align=2, sort=10)
	public String getBellStatus() {
		return bellStatus;
	}

	public void setBellStatus(String bellStatus) {
		this.bellStatus = bellStatus;
	}
	
	//@ExcelField(title="工作状态", dictType="work_status", align=2, sort=11)
	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	
	//@ExcelField(title="设防状态", dictType="defense_status", align=2, sort=12)
	public String getDefenseStatus() {
		return defenseStatus;
	}

	public void setDefenseStatus(String defenseStatus) {
		this.defenseStatus = defenseStatus;
	}
	
	//@ExcelField(title="创建部门", align=2, sort=13)
	public String getCreateOffice() {
		return createOffice;
	}

	public void setCreateOffice(String createOffice) {
		this.createOffice = createOffice;
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

	public Cover getCover() {
		return cover;
	}

	public void setCover(Cover cover) {
		this.cover = cover;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getTownship() {
		return township;
	}

	public void setTownship(String township) {
		this.township = township;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@ExcelField(title="所属项目", align=2, sort=5)
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public List<ParamResVo> getParamList() {
		return paramList;
	}

	public void setParamList(List<ParamResVo> paramList) {
		this.paramList = paramList;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public String getRssi() {
		return rssi;
	}

	public void setRssi(String rssi) {
		this.rssi = rssi;
	}

	public String getWorkMode() {
		return workMode;
	}

	public void setWorkMode(String workMode) {
		this.workMode = workMode;
	}

	public String getBatteryVoltage() {
		return batteryVoltage;
	}

	public void setBatteryVoltage(String batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	public String getLightVoltage() {
		return lightVoltage;
	}

	public void setLightVoltage(String lightVoltage) {
		this.lightVoltage = lightVoltage;
	}

	public String getAngle() {
		return angle;
	}

	public void setAngle(String angle) {
		this.angle = angle;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getAngleThreshold() {
		return angleThreshold;
	}

	public void setAngleThreshold(String angleThreshold) {
		this.angleThreshold = angleThreshold;
	}

	public String getTemperatureThreshold() {
		return temperatureThreshold;
	}

	public void setTemperatureThreshold(String temperatureThreshold) {
		this.temperatureThreshold = temperatureThreshold;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public String getInitDepth() {
		return initDepth;
	}

	public void setInitDepth(String initDepth) {
		this.initDepth = initDepth;
	}

	public String getWaterLevelThreshold() {
		return waterLevelThreshold;
	}

	public void setWaterLevelThreshold(String waterLevelThreshold) {
		this.waterLevelThreshold = waterLevelThreshold;
	}
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.equinfo;

import java.util.Date;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.cv.entity.equinfo.Cover;

/**
 * 井铃设备信息Entity
 * @author crj
 * @version 2019-06-24
 */
public class CoverBell extends DataEntity<CoverBell> {
	
	private static final long serialVersionUID = 1L;
	private String coverId;		// 井盖ID
	private String coverNo;		// 井盖编号
	private String bellNo;		// 井铃编号
	private String bellModel;		// 井铃型号
	private String bellType;		// 设备类型
	private String city; //井盖城市
	private String district;//井盖区
	private String township;//井盖街道
	private String purpose;//管网用途

	private String version;		// 固件版本号
	private String imei;		// 设备IMEI号
	private String sim;		// 设备SIM卡号
	private String bellStatus;		// 生命周期
	private String workStatus;		// 工作状态
	private String defenseStatus;		// 设防状态
	private String createOffice;		// 创建部门
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间

	private Cover cover;		// 井盖信息
	
	public CoverBell() {
		super();
	}

	public CoverBell(String id){
		super(id);
	}

	@ExcelField(title="井盖ID", align=2, sort=4)
	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}
	
	@ExcelField(title="井盖编号", align=2, sort=5)
	public String getCoverNo() {
		return coverNo;
	}

	public void setCoverNo(String coverNo) {
		this.coverNo = coverNo;
	}
	
	@ExcelField(title="井铃编号", align=2, sort=6)
	public String getBellNo() {
		return bellNo;
	}

	public void setBellNo(String bellNo) {
		this.bellNo = bellNo;
	}
	
	@ExcelField(title="井铃型号", align=2, sort=7)
	public String getBellModel() {
		return bellModel;
	}

	public void setBellModel(String bellModel) {
		this.bellModel = bellModel;
	}
	
	@ExcelField(title="设备类型", dictType="bellType", align=2, sort=8)
	public String getBellType() {
		return bellType;
	}

	public void setBellType(String bellType) {
		this.bellType = bellType;
	}
	
	@ExcelField(title="固件版本号", align=2, sort=9)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@ExcelField(title="设备IMEI号", align=2, sort=10)
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
	
	@ExcelField(title="设备SIM卡号", align=2, sort=9)
	public String getSim() {
		return sim;
	}

	public void setSim(String sim) {
		this.sim = sim;
	}
	
	@ExcelField(title="生命周期", dictType="bell_status", align=2, sort=10)
	public String getBellStatus() {
		return bellStatus;
	}

	public void setBellStatus(String bellStatus) {
		this.bellStatus = bellStatus;
	}
	
	@ExcelField(title="工作状态", dictType="work_status", align=2, sort=11)
	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	
	@ExcelField(title="设防状态", dictType="defense_status", align=2, sort=12)
	public String getDefenseStatus() {
		return defenseStatus;
	}

	public void setDefenseStatus(String defenseStatus) {
		this.defenseStatus = defenseStatus;
	}
	
	@ExcelField(title="创建部门", align=2, sort=13)
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
}
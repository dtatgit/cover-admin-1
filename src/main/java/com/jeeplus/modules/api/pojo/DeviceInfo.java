package com.jeeplus.modules.api.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备基础信息表  add by ffy
 */
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = -7461669590591225028L;

    private String id;
    private String devNo;
    private String devId;
    private String dType; //设备厂家类型（sy,sz,wx）
    private String deviceType; //设备类型
    private String imei;		// imei
    private String imsi;		// imsi
    private String iccid; // iccid
    private Integer category; //设备类别（世源文星使用）
    private String deviceKey;   //设备秘钥（捷讯设备专有）
    private Integer onlineState;	// 在线状态（0：在线1:离线）
    private Integer fortifyState; //设防状态（0：撤防1：设防）
    private Integer wellCoverState; //井盖状态(0: 正常,1: 打开,2: 松动3: 复位)
    private Integer waterLevelState; //水位状态(0: 正常,1: 水满,2: 中水位,3: 低水位)
    private Integer voltageState; //电压状态(0: 正常,1: 低电压,2: 超低电压,3: 高电压)
    private Integer tempState; //温度状态(0: 正常,1: 低温,2: 高温)
    private Integer vibrationAlarm; //振动报警(0：正常,1：告警)
    private String version; //版本
    private Integer state; //状态（0：正常1：休眠2：报废）
    private LocalDateTime createDate;	// 创建日期


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getdType() {
        return dType;
    }

    public void setdType(String dType) {
        this.dType = dType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public Integer getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(Integer onlineState) {
        this.onlineState = onlineState;
    }

    public Integer getFortifyState() {
        return fortifyState;
    }

    public void setFortifyState(Integer fortifyState) {
        this.fortifyState = fortifyState;
    }

    public Integer getWellCoverState() {
        return wellCoverState;
    }

    public void setWellCoverState(Integer wellCoverState) {
        this.wellCoverState = wellCoverState;
    }

    public Integer getWaterLevelState() {
        return waterLevelState;
    }

    public void setWaterLevelState(Integer waterLevelState) {
        this.waterLevelState = waterLevelState;
    }

    public Integer getVoltageState() {
        return voltageState;
    }

    public void setVoltageState(Integer voltageState) {
        this.voltageState = voltageState;
    }

    public Integer getTempState() {
        return tempState;
    }

    public void setTempState(Integer tempState) {
        this.tempState = tempState;
    }

    public Integer getVibrationAlarm() {
        return vibrationAlarm;
    }

    public void setVibrationAlarm(Integer vibrationAlarm) {
        this.vibrationAlarm = vibrationAlarm;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}

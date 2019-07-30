package com.jeeplus.modules.api.pojo;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 设备信息
 */
public class DeviceResult {
    private String devId;  //设备编号

    private String dType; //设备类型（sy,sz,wx）

    private Integer wellCoverState;  //井盖状态   0：井盖关,1: 井盖开

    private Integer waterLevelAlarm;  //水位状态  0：正常,1：水满

    private Integer vibrationAlarm; //震动告警  0：未告警1：告警

    private Integer highTemperatureAlarm; //高温告警  0：未告警1：告警

    private Integer fortificationState;  //设防状态： 0:已设防1：已撤防

    private String rssi; //信号强度

    private String batteryVoltage;         //电池电压

    private String version; //版本号

    private Date updateTime;  //更新时间

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

    public Integer getWellCoverState() {
        return wellCoverState;
    }

    public void setWellCoverState(Integer wellCoverState) {
        this.wellCoverState = wellCoverState;
    }

    public Integer getWaterLevelAlarm() {
        return waterLevelAlarm;
    }

    public void setWaterLevelAlarm(Integer waterLevelAlarm) {
        this.waterLevelAlarm = waterLevelAlarm;
    }

    public Integer getVibrationAlarm() {
        return vibrationAlarm;
    }

    public void setVibrationAlarm(Integer vibrationAlarm) {
        this.vibrationAlarm = vibrationAlarm;
    }

    public Integer getHighTemperatureAlarm() {
        return highTemperatureAlarm;
    }

    public void setHighTemperatureAlarm(Integer highTemperatureAlarm) {
        this.highTemperatureAlarm = highTemperatureAlarm;
    }

    public Integer getFortificationState() {
        return fortificationState;
    }

    public void setFortificationState(Integer fortificationState) {
        this.fortificationState = fortificationState;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(String batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

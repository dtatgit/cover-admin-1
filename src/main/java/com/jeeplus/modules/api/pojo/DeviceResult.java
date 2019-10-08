package com.jeeplus.modules.api.pojo;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 设备信息
 */
public class DeviceResult {

    private String devNo;  //设备编号
    private String devId;  //硬件原始设备号

    private String dType; //设备类型（sy,sz,wx）

    private Integer wellCoverState;  //井盖状态   0：正常,1: 打开,2: 松动,3: 复位

    private Integer waterLevelAlarm;  //水位状态  0：正常,1：水满, 2: 中水位, 3: 低水位

    private String waterLevel;//水位

    private Integer voltageState;//电源状态 0: 正常1: 低电压2: 超低电压3: 高电压

    private String batteryVoltage;         //电池电压

    private Integer tempState;//温度状态

    private String temperature;  //温度

    private Integer fortifyState;  //设防状态： 0:已设防1：已撤防

    private Integer vibrationAlarm; //震动告警  0：未告警1：告警

    private String rssi; //信号强度

    private String version; //版本号

    private String updateTime;  //更新时间

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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(String waterLevel) {
        this.waterLevel = waterLevel;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
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

    public Integer getFortifyState() {
        return fortifyState;
    }

    public void setFortifyState(Integer fortifyState) {
        this.fortifyState = fortifyState;
    }
}

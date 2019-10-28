package com.jeeplus.modules.api.pojo;

import java.util.Date;

/**
 * 设备信息列表getDeviceList
 */
public class DeviceListResult {

    private String id;  //主键

    private String devNo;  //设备编号

    private String devId;  //硬件原始设备号

    private String dType; //设备类型（sy,sz,wx）

    private String imei;  //imei

    private String imsi;  //imsi

    private int deviceType;  //设备类型（世源设备专有)

    private String deviceKey;  //设备秘钥（捷讯设备专有）

    private int onlineState;  //在线状态 0：在线1:离线

    private int fortifyState;  //设防状态 0：撤防1：设防

    private int wellCoverState;  //井盖状态 0: 正常,1: 打开,2: 松动3: 复位

    private int waterLevelState;  //水位状态 0: 正常,1: 水满,2: 中水位,3: 低水位

    private int voltageState;  //电压状态 0: 正常,1: 低电压,2: 超低电压,3: 高电压

    private int tempState;  //温度状态 0: 正常,1: 低温,2: 高温

    private int vibrationAlarm;  //振动报警 0：正常,1：告警

    private int state;  //状态 0：正常1：休眠2：报废

    private String version;  //版本

    private Date createDate;  //创建时间

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

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public int getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(int onlineState) {
        this.onlineState = onlineState;
    }

    public int getFortifyState() {
        return fortifyState;
    }

    public void setFortifyState(int fortifyState) {
        this.fortifyState = fortifyState;
    }

    public int getWellCoverState() {
        return wellCoverState;
    }

    public void setWellCoverState(int wellCoverState) {
        this.wellCoverState = wellCoverState;
    }

    public int getWaterLevelState() {
        return waterLevelState;
    }

    public void setWaterLevelState(int waterLevelState) {
        this.waterLevelState = waterLevelState;
    }

    public int getVoltageState() {
        return voltageState;
    }

    public void setVoltageState(int voltageState) {
        this.voltageState = voltageState;
    }

    public int getTempState() {
        return tempState;
    }

    public void setTempState(int tempState) {
        this.tempState = tempState;
    }

    public int getVibrationAlarm() {
        return vibrationAlarm;
    }

    public void setVibrationAlarm(int vibrationAlarm) {
        this.vibrationAlarm = vibrationAlarm;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

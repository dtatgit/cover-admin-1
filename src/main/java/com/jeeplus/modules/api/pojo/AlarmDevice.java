package com.jeeplus.modules.api.pojo;

/**
 * 报警设备信息
 */
public class AlarmDevice {
    private String devNo;  //设备编号
    private String devId;  //硬件原始设备号

    private String dType; //设备类型（sy,sz,wx）

    private Integer wellCoverState;  //井盖状态   0：正常,1: 打开,2: 松动,3: 复位

    private Integer waterLevelState;  //水位状态  0：正常,1：水满, 2: 中水位, 3: 低水位

    private Integer voltageState;//电源状态 0: 正常1: 低电压2: 超低电压3: 高电压

    private Integer tempState;//温度状态0: 正常,1: 低温,2: 高温

    private Integer vibrationAlarm; //震动告警  0：未告警1：告警

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
}

package com.jeeplus.modules.api.pojo;

/**
 * 设备参数信息
 */
public class DeviceParameterResult {
    private String devNo;  //设备编号
    private Integer durationMinutes;  //心跳时间，单位小时
    private Integer shakeAlarmDurationMinutes; //震动上报时间,单位小时
    private Integer gSensorLevel; //震动触发等级
    private Integer angleThreshold;  //倾斜角度阈值，超过则报警
    private String depthThreshold; //深度阈值
    private String temperatureThreshold;// 温度阈值
    private Integer offlineTimeThreshold; //离线告警阈值

    public DeviceParameterResult() {
    }

    public DeviceParameterResult(String devNo, Integer durationMinutes, Integer shakeAlarmDurationMinutes, Integer gSensorLevel, Integer angleThreshold, String depthThreshold, String temperatureThreshold, Integer offlineTimeThreshold) {
        this.devNo = devNo;
        this.durationMinutes = durationMinutes;
        this.shakeAlarmDurationMinutes = shakeAlarmDurationMinutes;
        this.gSensorLevel = gSensorLevel;
        this.angleThreshold = angleThreshold;
        this.depthThreshold = depthThreshold;
        this.temperatureThreshold = temperatureThreshold;
        this.offlineTimeThreshold = offlineTimeThreshold;
    }

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Integer getShakeAlarmDurationMinutes() {
        return shakeAlarmDurationMinutes;
    }

    public void setShakeAlarmDurationMinutes(Integer shakeAlarmDurationMinutes) {
        this.shakeAlarmDurationMinutes = shakeAlarmDurationMinutes;
    }

    public Integer getgSensorLevel() {
        return gSensorLevel;
    }

    public void setgSensorLevel(Integer gSensorLevel) {
        this.gSensorLevel = gSensorLevel;
    }

    public Integer getAngleThreshold() {
        return angleThreshold;
    }

    public void setAngleThreshold(Integer angleThreshold) {
        this.angleThreshold = angleThreshold;
    }

    public String getDepthThreshold() {
        return depthThreshold;
    }

    public void setDepthThreshold(String depthThreshold) {
        this.depthThreshold = depthThreshold;
    }

    public String getTemperatureThreshold() {
        return temperatureThreshold;
    }

    public void setTemperatureThreshold(String temperatureThreshold) {
        this.temperatureThreshold = temperatureThreshold;
    }

    public Integer getOfflineTimeThreshold() {
        return offlineTimeThreshold;
    }

    public void setOfflineTimeThreshold(Integer offlineTimeThreshold) {
        this.offlineTimeThreshold = offlineTimeThreshold;
    }
}

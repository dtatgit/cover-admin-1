package com.jeeplus.modules.api.pojo;

/**
 * 设备参数信息
 */
public class DeviceParameterResult {
    private String devNo;  //设备编号
    private Integer heartbeatTime;  //心跳时间，单位分钟
    private Integer angleThreshold;  //角度阈值，超过则报警

    public DeviceParameterResult() {
    }

    public DeviceParameterResult(String devNo, Integer heartbeatTime, Integer angleThreshold) {
        this.devNo = devNo;
        this.heartbeatTime = heartbeatTime;
        this.angleThreshold = angleThreshold;
    }

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public Integer getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(Integer heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

    public Integer getAngleThreshold() {
        return angleThreshold;
    }

    public void setAngleThreshold(Integer angleThreshold) {
        this.angleThreshold = angleThreshold;
    }
}

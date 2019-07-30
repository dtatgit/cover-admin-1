package com.jeeplus.modules.api.pojo;

/**
 * 设备参数信息
 */
public class DeviceParameterResult {
    private String devId;  //设备编号
    private String heartbeatTime;  //心跳时间，单位分钟
    private String angleThreshold;  //角度阈值，超过则报警

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(String heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

    public String getAngleThreshold() {
        return angleThreshold;
    }

    public void setAngleThreshold(String angleThreshold) {
        this.angleThreshold = angleThreshold;
    }
}

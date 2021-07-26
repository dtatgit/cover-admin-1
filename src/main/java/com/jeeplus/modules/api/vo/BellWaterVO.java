package com.jeeplus.modules.api.vo;

import java.math.BigDecimal;

public class BellWaterVO {
    private String devNo;//设备编号
    private String devId;//设备ID
    private String dtype;//厂商代码号
    private BigDecimal initDistance;//初始距离,安装时传感器到井盖距离（长沙设备）
    private BigDecimal threshold;//阈值,高水位阈值
    private BigDecimal depth;//当前深度,传感器测得的水深（长沙设备）
    private BigDecimal distance;//当前水位,当前水位到井盖的距离

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

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public BigDecimal getInitDistance() {
        return initDistance;
    }

    public void setInitDistance(BigDecimal initDistance) {
        this.initDistance = initDistance;
    }

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }
}

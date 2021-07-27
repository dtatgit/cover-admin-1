package com.jeeplus.modules.api.vo;

import java.math.BigDecimal;

public class CoverResVo {

    private String no;		// 编号
    private BigDecimal longitude;		// 经度
    private BigDecimal latitude;		// 纬度
    private BigDecimal wgs84X;		// 经度
    private BigDecimal wgs84Y;		// 纬度

    private String status; //状态  0:在线  1： 离线  2：排障  3：维护

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getWgs84X() {
        return wgs84X;
    }

    public void setWgs84X(BigDecimal wgs84X) {
        this.wgs84X = wgs84X;
    }

    public BigDecimal getWgs84Y() {
        return wgs84Y;
    }

    public void setWgs84Y(BigDecimal wgs84Y) {
        this.wgs84Y = wgs84Y;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

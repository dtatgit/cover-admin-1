package com.jeeplus.modules.api.vo;
import java.math.BigDecimal;

public class CoverBellData {
    //井盖编码,经度,纬度,实时水位,阈值（井盖编码不传返回全部数据）
    private String coverId;		// 井盖id
    private String coverNo;		// 井盖编号
    private String addressDetail;		// 地址：详细地址
    private String purpose;//管网用途
    private String longitude;		// 经度
    private String latitude;		// 纬度
    private String bellNo;		// 井卫编号
    private String depth;		// 当前水位到井盖的距离
    private String waterLevelThreshold;		// 高水位阈值

    public String getCoverNo() {
        return coverNo;
    }

    public void setCoverNo(String coverNo) {
        this.coverNo = coverNo;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getBellNo() {
        return bellNo;
    }

    public void setBellNo(String bellNo) {
        this.bellNo = bellNo;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getWaterLevelThreshold() {
        return waterLevelThreshold;
    }

    public void setWaterLevelThreshold(String waterLevelThreshold) {
        this.waterLevelThreshold = waterLevelThreshold;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }
}

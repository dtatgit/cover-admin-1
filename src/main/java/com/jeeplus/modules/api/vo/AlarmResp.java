package com.jeeplus.modules.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AlarmResp {

    private String id;
    private String alarmNo;		// 报警编号
    private String coverId;		// 井盖id
    private String coverNo;		// 井盖编号
    private String coverBellId;		// 井卫id
    private String coverBellNo;		// 井卫no
    private String address;		// 地址
    private String alarmType;		// 报警类型
    private Date alarmTime;		// 报警时间
    private String isCreateWork;		// 是否派单处理
    private String coverWorkId;
    private String dealStatus;  //处理状态
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createDate;	// 创建日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateDate;	// 更新日期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlarmNo() {
        return alarmNo;
    }

    public void setAlarmNo(String alarmNo) {
        this.alarmNo = alarmNo;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public String getCoverNo() {
        return coverNo;
    }

    public void setCoverNo(String coverNo) {
        this.coverNo = coverNo;
    }

    public String getCoverBellId() {
        return coverBellId;
    }

    public void setCoverBellId(String coverBellId) {
        this.coverBellId = coverBellId;
    }

    public String getCoverBellNo() {
        return coverBellNo;
    }

    public void setCoverBellNo(String coverBellNo) {
        this.coverBellNo = coverBellNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getIsCreateWork() {
        return isCreateWork;
    }

    public void setIsCreateWork(String isCreateWork) {
        this.isCreateWork = isCreateWork;
    }

    public String getCoverWorkId() {
        return coverWorkId;
    }

    public void setCoverWorkId(String coverWorkId) {
        this.coverWorkId = coverWorkId;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

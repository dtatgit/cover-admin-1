package com.jeeplus.modules.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AlarmResp {

    private String id;
    private String coverBellId;		// 井铃ID
    private String bellNo;		// 井铃编号
    private String coverId;		// 井盖ID
    private String coverNo;		// 井盖编号
    private String alarmNum;		// 报警编号
    private String alarmType;		// 报警类型
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alarmDate;		// 报警时间
    private String isCreateWork;		// 是否生成工单
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

    public String getCoverBellId() {
        return coverBellId;
    }

    public void setCoverBellId(String coverBellId) {
        this.coverBellId = coverBellId;
    }

    public String getBellNo() {
        return bellNo;
    }

    public void setBellNo(String bellNo) {
        this.bellNo = bellNo;
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

    public String getAlarmNum() {
        return alarmNum;
    }

    public void setAlarmNum(String alarmNum) {
        this.alarmNum = alarmNum;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(Date alarmDate) {
        this.alarmDate = alarmDate;
    }

    public String getIsCreateWork() {
        return isCreateWork;
    }

    public void setIsCreateWork(String isCreateWork) {
        this.isCreateWork = isCreateWork;
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

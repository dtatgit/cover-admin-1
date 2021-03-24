package com.jeeplus.modules.cb.service.bizAlarm;

import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cv.entity.equinfo.Cover;

import java.util.Date;

public class DataParam {

    private String devNo;//设备编号

    private String devPurpose;//管网用途

    private String streetName; //归属街道

    private String alarmType;//告警类型

    private String bizAlarmType; //业务报警类型

    private String value;//告警具体值

    private Date alarmTime;//告警时间

    private Cover cover;

    private CoverBell coverBell;

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getDevPurpose() {
        return devPurpose;
    }

    public void setDevPurpose(String devPurpose) {
        this.devPurpose = devPurpose;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public CoverBell getCoverBell() {
        return coverBell;
    }

    public void setCoverBell(CoverBell coverBell) {
        this.coverBell = coverBell;
    }


    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public String getBizAlarmType() {
        return bizAlarmType;
    }

    public void setBizAlarmType(String bizAlarmType) {
        this.bizAlarmType = bizAlarmType;
    }
}

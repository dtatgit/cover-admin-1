package com.jeeplus.modules.api.pojo;

import java.util.Date;

/**
 * 数据订阅
 * 井卫设备上报报警数据详细参数
 */
public class DataSubParam {

    private String devNo;//设备编号

    private String alarmType;//告警类型

    private String value;//告警具体值

    private Date alarmTime;//告警时间

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
}

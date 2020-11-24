package com.jeeplus.modules.cv.entity.statis;

import java.util.Date;

public class BizAlarmStatisBo {

    private String alarmType;

    private String lifeCycle;

    private Long count;


    public String getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

}

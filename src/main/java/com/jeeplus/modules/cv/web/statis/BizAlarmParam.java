package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.core.persistence.BaseEntity;
import com.jeeplus.modules.act.entity.Act;

import java.util.Date;

public class BizAlarmParam {

    private String township; //街道/镇

    private Date beginDate;

    private Date endDate;

    private String alarmType;


    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }
}

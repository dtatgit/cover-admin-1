package com.jeeplus.modules.cv.entity.statis;

import com.jeeplus.modules.sys.entity.Office;

import java.util.Date;
import java.util.List;

public class CoverWorkParam {

    private String lifeCycle;

    private String workType;

    private Date beginDate;

    private Date endDate;

    private List<Office> officeList; //本单位及子单位


    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public List<Office> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Office> officeList) {
        this.officeList = officeList;
    }
}

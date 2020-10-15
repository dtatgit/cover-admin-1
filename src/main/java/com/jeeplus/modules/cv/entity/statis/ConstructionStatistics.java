package com.jeeplus.modules.cv.entity.statis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;

/**
 * @Description: 施工信息统计Entity
 * @Author: ycp
 * @Date: 2020/10/15
 */
public class ConstructionStatistics extends DataEntity<ConstructionStatistics> {
    private static final long serialVersionUID = 1L;
    private String workStatus;  //工单状态
    private Date beginDateCreateTime;    //工单生成时间开始
    private Date endDateCreateTime;    //工单生成时间结束
    private String processingDepart;    //处理单位
    private String streetTown;  //街道/镇
    private Date beginDateComplete;   //完成时间开始
    private Date endDateComplete;   //完成时间结束

    public ConstructionStatistics() {
        super();
    }

    @ExcelField(title = "工单状态", align = 2, sort = 6)
    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    @ExcelField(title = "处理单位", align = 2, sort = 6)
    public String getProcessingDepart() {
        return processingDepart;
    }

    public void setProcessingDepart(String processingDepart) {
        this.processingDepart = processingDepart;
    }

    @ExcelField(title = "街道/镇", align = 2, sort = 6)
    public String getStreetTown() {
        return streetTown;
    }

    public void setStreetTown(String streetTown) {
        this.streetTown = streetTown;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "工单生成时间", align = 2, sort = 6)
    public Date getBeginDateCreateTime() {
        return beginDateCreateTime;
    }

    public void setBeginDateCreateTime(Date beginDateCreateTime) {
        this.beginDateCreateTime = beginDateCreateTime;
    }

    public Date getEndDateCreateTime() {
        return endDateCreateTime;
    }

    public void setEndDateCreateTime(Date endDateCreateTime) {
        this.endDateCreateTime = endDateCreateTime;
    }

    public Date getBeginDateComplete() {
        return beginDateComplete;
    }

    public void setBeginDateComplete(Date beginDateComplete) {
        this.beginDateComplete = beginDateComplete;
    }

    public Date getEndDateComplete() {
        return endDateComplete;
    }

    public void setEndDateComplete(Date endDateComplete) {
        this.endDateComplete = endDateComplete;
    }
}

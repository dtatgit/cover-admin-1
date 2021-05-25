package com.jeeplus.modules.cv.entity.statis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;

public class CoverWorkStatisVo extends DataEntity<CoverWorkStatisVo> {


    private String coverWorkNo; //工单编号

    private String userId; //操作人id

    private String userName; //操作人名字

    private String status; //工单状态

    private String workType; //工单类型

    private Date createDate; //创建工单时间

    private Date completeDate; //完成时间

    private Date beginDate; //开始时间

    private Date endDate; //开始时间



    public String getCoverWorkNo() {
        return coverWorkNo;
    }

    public void setCoverWorkNo(String coverWorkNo) {
        this.coverWorkNo = coverWorkNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @ExcelField(title="工单类型", dictType="work_type", align=2, sort=10)
    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

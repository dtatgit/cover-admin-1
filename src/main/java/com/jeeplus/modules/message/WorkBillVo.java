package com.jeeplus.modules.message;

import com.jeeplus.modules.cb.entity.work.CoverWork;

import java.math.BigDecimal;

/**
 * 工单摘要信息
 */
public class WorkBillVo {
    private String id;
    private String coverId;
    private String coverNo;
    private String coverBellId;
    private String workNum;
    private String workType;
    private String workStatus;
    private String source;
    private String constructionContent;
    private String constructionUser;
    private String phone;
    private String constructionDepart;
    private String workLevel;
    private String createDepart;
    private String updateDepart;

    private String batch;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String flowId;
    private String parentWorkId;
    private String lifeCycle;

    public WorkBillVo() {
        // do nothing
    }

    public WorkBillVo(CoverWork coverWork) {
        this.id = coverWork.getId();
        this.coverId = coverWork.getCover().getId();
        this.coverNo = coverWork.getCoverNo();
        this.coverBellId = coverWork.getCoverBellId();
        this.workNum = coverWork.getWorkNum();
        this.workType = coverWork.getWorkType();
        this.workStatus = coverWork.getWorkStatus();
        this.source = coverWork.getSource();
        this.constructionContent = coverWork.getConstructionContent();
        if (coverWork.getConstructionDepart() != null) {
            this.constructionDepart = coverWork.getConstructionDepart().getId();
        }
        if (coverWork.getConstructionUser() != null) {
            this.constructionUser = coverWork.getConstructionUser().getId();
        }
        this.phone = coverWork.getPhone();
        this.workLevel = coverWork.getWorkLevel();
        this.createDepart = coverWork.getCreateDepart();
        this.updateDepart = coverWork.getUpdateDepart();
        this.batch = coverWork.getBatch();
        this.longitude = coverWork.getLongitude();
        this.latitude = coverWork.getLatitude();
        if (coverWork.getFlowId() != null) {
            this.flowId = coverWork.getFlowId().getId();
        }
        if (coverWork.getParentWorkId() != null) {
            this.parentWorkId = coverWork.getParentWorkId().getId();
        }
        this.lifeCycle = coverWork.getLifeCycle();
    }

    public static WorkBillVo of(CoverWork coverWork) {
        return new WorkBillVo(coverWork);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getConstructionContent() {
        return constructionContent;
    }

    public void setConstructionContent(String constructionContent) {
        this.constructionContent = constructionContent;
    }

    public String getConstructionUser() {
        return constructionUser;
    }

    public void setConstructionUser(String constructionUser) {
        this.constructionUser = constructionUser;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getConstructionDepart() {
        return constructionDepart;
    }

    public void setConstructionDepart(String constructionDepart) {
        this.constructionDepart = constructionDepart;
    }

    public String getWorkLevel() {
        return workLevel;
    }

    public void setWorkLevel(String workLevel) {
        this.workLevel = workLevel;
    }

    public String getCreateDepart() {
        return createDepart;
    }

    public void setCreateDepart(String createDepart) {
        this.createDepart = createDepart;
    }

    public String getUpdateDepart() {
        return updateDepart;
    }

    public void setUpdateDepart(String updateDepart) {
        this.updateDepart = updateDepart;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getParentWorkId() {
        return parentWorkId;
    }

    public void setParentWorkId(String parentWorkId) {
        this.parentWorkId = parentWorkId;
    }

    public String getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }
}

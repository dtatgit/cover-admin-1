package com.jeeplus.modules.cv.vo;

public class CoverStatisVO {
    private String district;		// 区域
//    private String coverType;		// 井盖类型
//    private String ownerDepart;		// 权属单位
    private Long coverNum;		// 井盖数
    private Long installEqu;		// 已安装设备数
    private Long onlineNum;		// 当前在线数
    private Long offlineNum;		// 当前离线数
    private Long coverAlarmNum;		// 报警井盖数
    private Long alarmTotalNum;		// 报警总数
    private Long addWorkNum;		// 工单总数（当天新增）
    private Long completeWorkNum;		// 已完成工单总数（当天）
    private Long proWorkNum;		// 未完成工单总数（累计）
    private String statisTime;		// 统计时间

    private Long workNumTotal;		// 工单总数（累计总共）
    private Long completeWorkNumTotal;		// 已完成工单总数（累计总共）

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Long getCoverNum() {
        return coverNum;
    }

    public void setCoverNum(Long coverNum) {
        this.coverNum = coverNum;
    }

    public Long getInstallEqu() {
        return installEqu;
    }

    public void setInstallEqu(Long installEqu) {
        this.installEqu = installEqu;
    }

    public Long getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Long onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Long getOfflineNum() {
        return offlineNum;
    }

    public void setOfflineNum(Long offlineNum) {
        this.offlineNum = offlineNum;
    }

    public Long getCoverAlarmNum() {
        return coverAlarmNum;
    }

    public void setCoverAlarmNum(Long coverAlarmNum) {
        this.coverAlarmNum = coverAlarmNum;
    }

    public Long getAlarmTotalNum() {
        return alarmTotalNum;
    }

    public void setAlarmTotalNum(Long alarmTotalNum) {
        this.alarmTotalNum = alarmTotalNum;
    }

    public Long getAddWorkNum() {
        return addWorkNum;
    }

    public void setAddWorkNum(Long addWorkNum) {
        this.addWorkNum = addWorkNum;
    }

    public Long getCompleteWorkNum() {
        return completeWorkNum;
    }

    public void setCompleteWorkNum(Long completeWorkNum) {
        this.completeWorkNum = completeWorkNum;
    }

    public Long getProWorkNum() {
        return proWorkNum;
    }

    public void setProWorkNum(Long proWorkNum) {
        this.proWorkNum = proWorkNum;
    }

    public String getStatisTime() {
        return statisTime;
    }

    public void setStatisTime(String statisTime) {
        this.statisTime = statisTime;
    }

    public Long getWorkNumTotal() {
        return workNumTotal;
    }

    public void setWorkNumTotal(Long workNumTotal) {
        this.workNumTotal = workNumTotal;
    }

    public Long getCompleteWorkNumTotal() {
        return completeWorkNumTotal;
    }

    public void setCompleteWorkNumTotal(Long completeWorkNumTotal) {
        this.completeWorkNumTotal = completeWorkNumTotal;
    }
}

package com.jeeplus.modules.cv.vo;

public class CoverStatisVO {
    private String district;		// 区域
//    private String coverType;		// 井盖类型
//    private String ownerDepart;		// 权属单位
    private String coverNum;		// 井盖数
    private String installEqu;		// 已安装设备数
    private String onlineNum;		// 当前在线数
    private String offlineNum;		// 当前离线数
    private String coverAlarmNum;		// 报警井盖数
    private String alarmTotalNum;		// 报警总数
    private String addWorkNum;		// 工单总数（当天新增）
    private String completeWorkNum;		// 已完成工单总数（当天）
    private String proWorkNum;		// 未完成工单总数（累计）
    private String statisTime;		// 统计时间

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCoverNum() {
        return coverNum;
    }

    public void setCoverNum(String coverNum) {
        this.coverNum = coverNum;
    }

    public String getInstallEqu() {
        return installEqu;
    }

    public void setInstallEqu(String installEqu) {
        this.installEqu = installEqu;
    }

    public String getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(String onlineNum) {
        this.onlineNum = onlineNum;
    }

    public String getOfflineNum() {
        return offlineNum;
    }

    public void setOfflineNum(String offlineNum) {
        this.offlineNum = offlineNum;
    }

    public String getCoverAlarmNum() {
        return coverAlarmNum;
    }

    public void setCoverAlarmNum(String coverAlarmNum) {
        this.coverAlarmNum = coverAlarmNum;
    }

    public String getAlarmTotalNum() {
        return alarmTotalNum;
    }

    public void setAlarmTotalNum(String alarmTotalNum) {
        this.alarmTotalNum = alarmTotalNum;
    }

    public String getAddWorkNum() {
        return addWorkNum;
    }

    public void setAddWorkNum(String addWorkNum) {
        this.addWorkNum = addWorkNum;
    }

    public String getCompleteWorkNum() {
        return completeWorkNum;
    }

    public void setCompleteWorkNum(String completeWorkNum) {
        this.completeWorkNum = completeWorkNum;
    }

    public String getProWorkNum() {
        return proWorkNum;
    }

    public void setProWorkNum(String proWorkNum) {
        this.proWorkNum = proWorkNum;
    }

    public String getStatisTime() {
        return statisTime;
    }

    public void setStatisTime(String statisTime) {
        this.statisTime = statisTime;
    }
}

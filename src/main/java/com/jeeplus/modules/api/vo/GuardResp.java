package com.jeeplus.modules.api.vo;

public class GuardResp {

    private String id;
    private String bellNo;		// 井铃编号
    private String bellType;		// 设备类型
    private String bellStatus;		// 生命周期
    private String workStatus;		// 工作状态
    private String defenseStatus;		// 设防状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBellNo() {
        return bellNo;
    }

    public void setBellNo(String bellNo) {
        this.bellNo = bellNo;
    }

    public String getBellType() {
        return bellType;
    }

    public void setBellType(String bellType) {
        this.bellType = bellType;
    }

    public String getBellStatus() {
        return bellStatus;
    }

    public void setBellStatus(String bellStatus) {
        this.bellStatus = bellStatus;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getDefenseStatus() {
        return defenseStatus;
    }

    public void setDefenseStatus(String defenseStatus) {
        this.defenseStatus = defenseStatus;
    }
}

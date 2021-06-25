package com.jeeplus.modules.cb.entity.equinfo;

public class CoverBellVo {

    private String id;
    private String bellNo;		// 井卫编号
    private String bellType;		// 设备类型
    private String imei;		// 设备IMEI号
    private String workStatus;		// 工作状态
    private String defenseStatus;	 	// 设防状态
    private String angle;   //角度
    private String temperature; //温度
    private String waterLevel; //水位


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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
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

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(String waterLevel) {
        this.waterLevel = waterLevel;
    }
}

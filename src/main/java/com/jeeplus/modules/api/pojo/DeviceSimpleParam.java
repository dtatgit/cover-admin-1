package com.jeeplus.modules.api.pojo;

import java.io.Serializable;


/**
 * 井卫设备简单数据封装   add by ffy
 */
public class DeviceSimpleParam implements Serializable {


    private static final long serialVersionUID = -2353795899679968730L;

    private String devNo;
    private String deviceType; //设备类型
    private String imei;		// imei
    private String iccid; // iccid
    private String version; //版本

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "DeviceSimpleParam{" +
                "devNo='" + devNo + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", imei='" + imei + '\'' +
                ", iccid='" + iccid + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}

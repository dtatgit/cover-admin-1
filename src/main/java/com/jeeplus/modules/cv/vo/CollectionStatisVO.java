package com.jeeplus.modules.cv.vo;

import java.util.List;

public class CollectionStatisVO {
    Integer coverTotalNum=0;		// 采集数量
    String  collectionTime;//采集时间
    String purpose;		// 井位用途
    String damageLaber;//井盖损坏形式字典laber
    String damageName;//井盖损坏形式字典name
    String damagePerNum;//井盖损坏形式百分之比

    Integer alarmNum=0;		// 报警数量
    String  alarmTime;//报警时间

    private String material;		// 井盖材质
    private String situation;		// 井盖地理场合

    public Integer getCoverTotalNum() {
        return coverTotalNum;
    }

    public void setCoverTotalNum(Integer coverTotalNum) {
        this.coverTotalNum = coverTotalNum;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDamageLaber() {
        return damageLaber;
    }

    public void setDamageLaber(String damageLaber) {
        this.damageLaber = damageLaber;
    }

    public String getDamageName() {
        return damageName;
    }

    public void setDamageName(String damageName) {
        this.damageName = damageName;
    }

    public String getDamagePerNum() {
        return damagePerNum;
    }

    public void setDamagePerNum(String damagePerNum) {
        this.damagePerNum = damagePerNum;
    }

    public Integer getAlarmNum() {
        return alarmNum;
    }

    public void setAlarmNum(Integer alarmNum) {
        this.alarmNum = alarmNum;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
}

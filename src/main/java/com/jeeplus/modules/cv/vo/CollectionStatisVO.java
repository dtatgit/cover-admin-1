package com.jeeplus.modules.cv.vo;

import java.util.List;

public class CollectionStatisVO {
    Integer coverTotalNum=0;		// 采集数量
    String  collectionTime;//采集时间
    String purpose;		// 井位用途


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
}

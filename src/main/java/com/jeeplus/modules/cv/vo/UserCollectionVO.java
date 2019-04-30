package com.jeeplus.modules.cv.vo;

public class UserCollectionVO {
     String  collectionName;//采集人员姓名
     Integer collectNum;		// 采集数量

     public String getCollectionName() {
          return collectionName;
     }

     public void setCollectionName(String collectionName) {
          this.collectionName = collectionName;
     }

     public Integer getCollectNum() {
          return collectNum;
     }

     public void setCollectNum(Integer collectNum) {
          this.collectNum = collectNum;
     }
}

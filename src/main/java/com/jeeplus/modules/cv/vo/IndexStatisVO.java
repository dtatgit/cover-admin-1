package com.jeeplus.modules.cv.vo;

import java.util.List;

/**
 * 首页统计数据
 */
public class IndexStatisVO {

    Integer coverTotalNum=0;		// 总勘察数
    Integer coverTodayNum=0;		// 今日勘察数
    Integer coverNoDepartNum=0;	//ownerDepart 无权属单位

    //井盖损坏形式数据
    Integer damageGoodNum=0;		// 损坏形式-完好0
    Integer damageDefectNum=0;		// 损坏形式-井盖缺失1
    Integer damageDestroyNum=0;		// 损坏形式-井盖破坏2
    Integer damageRiftNum=0;		// 损坏形式-井周沉降、龟裂3
    Integer damageOwnerNum=0;		// 损坏形式-井筒本身破坏4
    Integer damageOtherNum=0;		//其他9

    //井盖损坏形式数据百分之比
    double perDamageGoodNum;		// 损坏形式-完好0Percentage
    double perDamageDefectNum;		// 损坏形式-井盖缺失1
    double perDamageDestroyNum;		// 损坏形式-井盖破坏2
    double perDamageRiftNum;		// 损坏形式-井周沉降、龟裂3
    double perDamageOwnerNum;		// 损坏形式-井筒本身破坏4
    double perDamageOtherNum;	//其他9

    List<CollectionStatisVO> collectionList;//最近一周采集井盖数据汇总
    List<UserCollectionVO> userCollectionList;//人员采集井盖情况汇总

    Integer numArr[]=new Integer[7];//首页采集数量数组
    String[] dateArr;//首页日期数组

//    String num1;
//    String date1;

//    public String getNum1() {
//        return num1;
//    }
//
//    public void setNum1(String num1) {
//        this.num1 = num1;
//    }
//
//    public String getDate1() {
//        return date1;
//    }
//
//    public void setDate1(String date1) {
//        this.date1 = date1;
//    }

    public Integer getCoverTotalNum() {
        return coverTotalNum;
    }

    public void setCoverTotalNum(Integer coverTotalNum) {
        this.coverTotalNum = coverTotalNum;
    }

    public Integer getCoverTodayNum() {
        return coverTodayNum;
    }

    public void setCoverTodayNum(Integer coverTodayNum) {
        this.coverTodayNum = coverTodayNum;
    }

    public Integer getCoverNoDepartNum() {
        return coverNoDepartNum;
    }

    public void setCoverNoDepartNum(Integer coverNoDepartNum) {
        this.coverNoDepartNum = coverNoDepartNum;
    }

    public Integer getDamageGoodNum() {
        return damageGoodNum;
    }

    public void setDamageGoodNum(Integer damageGoodNum) {
        this.damageGoodNum = damageGoodNum;
    }

    public Integer getDamageDefectNum() {
        return damageDefectNum;
    }

    public void setDamageDefectNum(Integer damageDefectNum) {
        this.damageDefectNum = damageDefectNum;
    }

    public Integer getDamageDestroyNum() {
        return damageDestroyNum;
    }

    public void setDamageDestroyNum(Integer damageDestroyNum) {
        this.damageDestroyNum = damageDestroyNum;
    }

    public Integer getDamageRiftNum() {
        return damageRiftNum;
    }

    public void setDamageRiftNum(Integer damageRiftNum) {
        this.damageRiftNum = damageRiftNum;
    }

    public Integer getDamageOwnerNum() {
        return damageOwnerNum;
    }

    public void setDamageOwnerNum(Integer damageOwnerNum) {
        this.damageOwnerNum = damageOwnerNum;
    }

    public Integer getDamageOtherNum() {
        return damageOtherNum;
    }

    public void setDamageOtherNum(Integer damageOtherNum) {
        this.damageOtherNum = damageOtherNum;
    }

    public List<CollectionStatisVO> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<CollectionStatisVO> collectionList) {
        this.collectionList = collectionList;
    }

    public List<UserCollectionVO> getUserCollectionList() {
        return userCollectionList;
    }

    public void setUserCollectionList(List<UserCollectionVO> userCollectionList) {
        this.userCollectionList = userCollectionList;
    }

    public double getPerDamageGoodNum() {
        return perDamageGoodNum;
    }

    public void setPerDamageGoodNum(double perDamageGoodNum) {
        this.perDamageGoodNum = perDamageGoodNum;
    }

    public double getPerDamageDefectNum() {
        return perDamageDefectNum;
    }

    public void setPerDamageDefectNum(double perDamageDefectNum) {
        this.perDamageDefectNum = perDamageDefectNum;
    }

    public double getPerDamageDestroyNum() {
        return perDamageDestroyNum;
    }

    public void setPerDamageDestroyNum(double perDamageDestroyNum) {
        this.perDamageDestroyNum = perDamageDestroyNum;
    }

    public double getPerDamageRiftNum() {
        return perDamageRiftNum;
    }

    public void setPerDamageRiftNum(double perDamageRiftNum) {
        this.perDamageRiftNum = perDamageRiftNum;
    }

    public double getPerDamageOwnerNum() {
        return perDamageOwnerNum;
    }

    public void setPerDamageOwnerNum(double perDamageOwnerNum) {
        this.perDamageOwnerNum = perDamageOwnerNum;
    }

    public double getPerDamageOtherNum() {
        return perDamageOtherNum;
    }

    public void setPerDamageOtherNum(double perDamageOtherNum) {
        this.perDamageOtherNum = perDamageOtherNum;
    }

    public Integer[] getNumArr() {
        return numArr;
    }

    public void setNumArr(Integer[] numArr) {
        this.numArr = numArr;
    }

    public String[] getDateArr() {
        return dateArr;
    }

    public void setDateArr(String[] dateArr) {
        this.dateArr = dateArr;
    }
}

package com.jeeplus.modules.cv.vo;

/**
 * 工单完成情况统计
 */
public class CoverWorkVO {
    String  updateDate;//数据更新时间
    String  statisTime;//统计时间
    String addNum;		// 工单新增数量
    String proNum;		// 工单未完成数量

    public String getStatisTime() {
        return statisTime;
    }

    public void setStatisTime(String statisTime) {
        this.statisTime = statisTime;
    }

    public String getAddNum() {
        return addNum;
    }

    public void setAddNum(String addNum) {
        this.addNum = addNum;
    }

    public String getProNum() {
        return proNum;
    }

    public void setProNum(String proNum) {
        this.proNum = proNum;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}

package com.jeeplus.modules.cb.constant.bizAlarm;

public interface BizAlarmConstant {


    public interface BizAlarmType {
        String OPEN = "open";
        String WATER_LEVEL = "waterLevel";
        String VOTAGE = "votage";
        String TEMPERATURE = "temperature";
        String VIBRATE = "vibrate";
        String BROKEN = "broken";
        String OFFLINE = "offline";
        String MANUAL = "manual";
        String PULLOFF = "pullOff";
        String SETTLE = "settle";//沉降报警

    }

    public interface BizAlarmDealStatus {
        String NOT_DEAL = "0"; //未处理
        String DEALED = "1"; //已处理
    }



}

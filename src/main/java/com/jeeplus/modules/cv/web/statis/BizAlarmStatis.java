package com.jeeplus.modules.cv.web.statis;

public class BizAlarmStatis {

    private String id;

    private String alarmType;

    private Long untreated;

    private Long processed;

    private Long processing;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public Long getUntreated() {
        return untreated;
    }

    public void setUntreated(Long untreated) {
        this.untreated = untreated;
    }

    public Long getProcessed() {
        return processed;
    }

    public void setProcessed(Long processed) {
        this.processed = processed;
    }

    public Long getProcessing() {
        return processing;
    }

    public void setProcessing(Long processing) {
        this.processing = processing;
    }
}

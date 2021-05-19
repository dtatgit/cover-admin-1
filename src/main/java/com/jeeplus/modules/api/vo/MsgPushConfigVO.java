package com.jeeplus.modules.api.vo;

public class MsgPushConfigVO {
    String pushMode;// 推送方式
    String content;//推送内容
    String mobile;	// 手机
    String userId;	// 通知人员Id
    public String getPushMode() {
        return pushMode;
    }

    public void setPushMode(String pushMode) {
        this.pushMode = pushMode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

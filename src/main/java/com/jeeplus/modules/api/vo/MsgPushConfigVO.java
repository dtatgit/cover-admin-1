package com.jeeplus.modules.api.vo;

import com.jeeplus.modules.sys.entity.User;

public class MsgPushConfigVO {
    String pushMode;// 推送方式
    String content;//推送内容
    User noticePerson;// 通知人员

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

    public User getNoticePerson() {
        return noticePerson;
    }

    public void setNoticePerson(User noticePerson) {
        this.noticePerson = noticePerson;
    }
}

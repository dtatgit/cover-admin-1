package com.jeeplus.modules.api.pojo;

/**
 * 数据订阅接口
 * 数据订阅接口功能为卫设备上报数据，第三方平台提供一个集中接收数据的URL链接
 */
public class DataSubParamInfo {
    /**
     * 设备 ID
     */
    private String devNo;

    /**
     * 接口指令
     */
    private String cmd;

    /**
     * 数据域
     */
    private Object data;

    public String getDevNo() {
        return devNo;
    }

    public void setDevNo(String devNo) {
        this.devNo = devNo;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

package com.jeeplus.modules.api.pojo;

public class Result {

    /**
     * 返回结果
     * 0  成功
     * 1  失败
     */
    private Integer code;
    /**
     * 返回结果
     * true 成功
     * false  失败
     */
    private String success;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

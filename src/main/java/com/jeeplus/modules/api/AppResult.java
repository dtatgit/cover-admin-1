package com.jeeplus.modules.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class AppResult<T> implements Serializable {

    private static final long serialVersionUID = -6738687138801995598L;

    private T data; // 响应数据
    private boolean success = true;
    @JsonIgnore
    private int code; // 业务状态码
    private String msg = "获取数据成功";

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

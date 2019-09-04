package com.jeeplus.modules.api.pojo;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public class PageData {

    /**
     * 总记录数
     */
    private Integer total;

    private List<Object> list;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }
}

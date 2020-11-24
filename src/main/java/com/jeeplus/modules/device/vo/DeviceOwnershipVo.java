package com.jeeplus.modules.device.vo;

import java.util.List;

public class DeviceOwnershipVo {

    private List<String> devIds;		// 设备id
    private String dtype;		// 设备类型
    private String serverUrlId;		// url地址(表server_url的主键)

    public List<String> getDevIds() {
        return devIds;
    }

    public void setDevIds(List<String> devIds) {
        this.devIds = devIds;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getServerUrlId() {
        return serverUrlId;
    }

    public void setServerUrlId(String serverUrlId) {
        this.serverUrlId = serverUrlId;
    }
}

package com.jeeplus.modules.api.constant;

public interface Constants {
    interface CMD {

        /**设备上线*/
        String ONLINE = "online";

        /**设备离线*/
        String OFFLINE = "offline";

    }

    interface MSG {
        String SUCCESS = "success";
        String FAIL = "fail";

        String OPERATE_OK = "操作成功";

        String DEVICE_OFFLINE = "设备已离线";

        String DEVICE_EXIST = "设备已经存在";
        String DEVICE_NOT_EXIST = "设备不存在";
        String PARAM_ERROR = "参数错误";
        String CMD_ERROR = "cmd错误";

    }


}

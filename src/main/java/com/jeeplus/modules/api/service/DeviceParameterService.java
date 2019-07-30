package com.jeeplus.modules.api.service;

import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.modules.api.pojo.DeviceParameterResult;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * 调用硬件平台接口获取数据（参数信息）
 */
@Service
public class DeviceParameterService {
    Logger logger = Logger.getLogger(DeviceParameterService.class);

    public Result setDeviceParameter(DeviceParameterResult deviceParameter){
        Result result = null;
        JSONObject param = new JSONObject();
        param.put("devId",deviceParameter.getDevId());//设备编号
        param.put("heartbeatTime",deviceParameter.getHeartbeatTime());//心跳时间，单位分钟
        param.put("angleThreshold",deviceParameter.getAngleThreshold());//角度阈值，超过则报警


        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/setDeviceParameter";
        try {
            String str = HttpClientUtil.post(deviceUrl,param);
            System.out.println("str:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){

            }else{
                logger.info("设置参数信息失败！硬件编号："+deviceParameter.getDevId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


}

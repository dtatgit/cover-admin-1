package com.jeeplus.modules.api;


import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目前提供给大屏的接口(不直接连硬件管理平台，在这里做个中转)
 */
@RestController
@RequestMapping("/guard/api")
public class DeviceApiController {

    private static Logger logger = LoggerFactory.getLogger(DeviceApiController.class);

    private static final String coverBellServerUrl = Global.getConfig("coverBell.server.url");

    @GetMapping("/device/deviceSimpleInfo/{devNo}")
    public AppResult deviceSimpleInfo(@PathVariable("devNo") String devNo) {

        AppResult result = new AppResult();

        String deviceUrl = coverBellServerUrl + "/device/deviceSimpleInfo/"+devNo;
        logger.info("******井卫硬件接口地址deviceSimpleInfo()：********{}",deviceUrl);
        try {
            String str = HttpClientUtil.get(deviceUrl);

            logger.info("deviceSimpleInfo(),结果:{}",str);

            Result resultTemp = JSONObject.parseObject(str,Result.class);

            if(resultTemp.getSuccess().equals("true")){
                Object data= resultTemp.getData();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                result.setData(jsonObject);
            }else{
                result.setSuccess(false);
                result.setMsg("获取数据失败");
            }
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg("接口调用失败:"+e.getMessage());
            e.printStackTrace();
        }

        return result;
    }


    @RequestMapping("/device/filterAlarmDevice")
    public AppResult filterAlarmDevice(@RequestParam("list") List<String> list){
        AppResult result = new AppResult();


        String deviceUrl = coverBellServerUrl + "/device/filterAlarmDevice";
        logger.info("******井卫硬件接口地址filterAlarmDevice()：********{}",deviceUrl);
        try {

            Map<String,Object> map = new HashMap<>();
            map.put("list",list);
            logger.info("参数list:"+list);
            String str = HttpClientUtil.doPost(deviceUrl,map);

            logger.info("deviceSimpleInfo(),结果:{}",str);

            Result resultTemp = JSONObject.parseObject(str,Result.class);

            if(resultTemp.getSuccess().equals("true")){
                Object data= resultTemp.getData();
                result.setData(data);
            }else{
                result.setSuccess(false);
                result.setMsg("获取数据失败");
            }
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg("接口调用失败:"+e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


}

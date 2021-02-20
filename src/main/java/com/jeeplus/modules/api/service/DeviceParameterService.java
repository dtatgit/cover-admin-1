package com.jeeplus.modules.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.api.pojo.DeviceParameterResult;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import com.jeeplus.modules.api.vo.ParamResVo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调用硬件平台接口获取数据（参数信息）
 */
@Service
public class DeviceParameterService {
    Logger logger = Logger.getLogger(DeviceParameterService.class);

    /**
     * 设置设备参数
     * @param deviceParameter
     * @return
     */
    public Result setDeviceParameter(DeviceParameterResult deviceParameter){
        Result result = new Result();
        Map param=new HashMap();
        param.put("durationMinutes",deviceParameter.getDurationMinutes());//心跳时间，单位分钟
        param.put("shakeAlarmDurationMinutes",deviceParameter.getShakeAlarmDurationMinutes());// 震动上报时间,单位小时
        param.put("gSensorLevel",deviceParameter.getGSensorLevel()); //震动触发等级
        param.put("angleThreshold",deviceParameter.getAngleThreshold()); //倾斜角度阈值，超过则报警
        param.put("depthThreshold",deviceParameter.getDepthThreshold()); //深度阈值
        param.put("temperatureThreshold",deviceParameter.getTemperatureThreshold()); // 温度阈值
        param.put("offlineTimeThreshold",deviceParameter.getOfflineTimeThreshold());  //离线告警阈值

        //String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/setDeviceParameter";
        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/wx/setParam?devNo=" + deviceParameter.getDevNo();
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            //System.out.println("7.设置设备参数:"+str);
            logger.info("7.设置设备参数:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){

            }else{
                logger.info("设置参数信息失败！硬件编号："+deviceParameter.getDevNo());
            }
        }catch (Exception e){
            e.printStackTrace();
            result.setSuccess("false");
            result.setMsg("设置参数异常");
        }
        return result;
    }

    /**
     * 获取设备参数
     * @param devNo 设备编号
     * @return
     */
    public DeviceParameterResult getDeviceParameter(String devNo){
        Result result = null;
        DeviceParameterResult deviceParameterResult=null;
        Map param=new HashMap();
        param.put("devNo",devNo);//设备编号
        //String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/getDeviceParameter";
        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/wx/getParam";
        try {
            logger.info("deviceUrl:{}"+deviceUrl);
            String str = HttpClientUtil.doPost(deviceUrl,param);
            //System.out.println("8.获取设备参数接口:"+str);
            logger.info("8.获取设备参数接口："+str);
            if(StringUtils.isNotBlank(str)){
                result = JSONObject.parseObject(str,Result.class);

                if(result.getSuccess().equals("true")){
                    Object data= result.getData();
                    JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                    /* String devId = jsonObject.getString("devId");*/
                    deviceParameterResult = JSONObject.parseObject(jsonObject.toString(),DeviceParameterResult.class);
                }else{
                    logger.info("获取设备参数信息失败！硬件编号："+devNo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deviceParameterResult;
    }

    /**
     * 最新，公共获取参数信息
     * @param devNo
     * @return
     */
    public List<ParamResVo> getDeviceParamete2(String devNo){
        List<ParamResVo> paramResVos = null;
        Map param=new HashMap();
        param.put("devNo",devNo);//设备编号
        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/getParamByDevNo";
        try {
            logger.info("deviceUrl:{}"+deviceUrl);
            String str = HttpClientUtil.doPost(deviceUrl,param);
            logger.info("8.获取设备参数接口："+str);
            if(StringUtils.isNotBlank(str)){
                JSONObject jsonObject = JSON.parseObject(str);
                Boolean success = jsonObject.getBoolean("success");
                if(success){
                    paramResVos = new ArrayList<>();
                    String dataStr = jsonObject.getString("data");
                    logger.info("dataStr:"+dataStr);
                    paramResVos = JSON.parseArray(dataStr, ParamResVo.class);
                }else{
                    logger.info("获取设备参数信息失败！硬件编号："+devNo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return paramResVos;
    }


    /**
     * 获取水位折线图数据
     * @param devNo 设备编号
     * @param startDateTime 日期
     * @param endDateTime 日期
     * @return
     */
    public Result queryDistanceData(String devNo,String startDateTime,String endDateTime){
        Result result = null;
        Map param=new HashMap();
        param.put("devNo",devNo);//设备编号
        param.put("beginTime",startDateTime);//开始日期
        param.put("endTime",endDateTime);//结束日期
        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/distanceData";
        //String deviceUrl = "http://192.168.1.133:8082/guard/api/device/distanceData";
        System.out.println("********deviceUrl***********"+deviceUrl);
        try {
            logger.info("deviceUrl:{}"+deviceUrl);
            String str = HttpClientUtil.doPost(deviceUrl,param);

            logger.info("水位折线图数据接口："+str);
            if(StringUtils.isNotBlank(str)){
                result = JSONObject.parseObject(str,Result.class);

                if(result.getSuccess().equals("true")){

                }else{
                    logger.info("获取水位折线图数据失败！硬件编号："+devNo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取水位折线图数据
     * @param devNo 设备编号
     * @param startDateTime 日期
     * @param endDateTime 日期
     * @return
     */
    public Result queryTemperatureData(String devNo,String startDateTime,String endDateTime){
        Result result = null;
        Map param=new HashMap();
        param.put("devNo",devNo);//设备编号
        param.put("beginTime",startDateTime);//开始日期
        param.put("endTime",endDateTime);//结束日期
        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/temperatureData";
        //String deviceUrl = "http://192.168.1.133:8082/guard/api/device/temperatureData";
        System.out.println("********deviceUrl***********"+deviceUrl);
        try {
            logger.info("deviceUrl:{}"+deviceUrl);
            String str = HttpClientUtil.doPost(deviceUrl,param);

            logger.info("水位折线图数据接口："+str);
            if(StringUtils.isNotBlank(str)){
                result = JSONObject.parseObject(str,Result.class);

                if(result.getSuccess().equals("true")){

                }else{
                    logger.info("取水位折线图数据失败！硬件编号："+devNo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}

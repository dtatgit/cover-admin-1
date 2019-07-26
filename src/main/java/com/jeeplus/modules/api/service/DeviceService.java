package com.jeeplus.modules.api.service;

import com.jeeplus.modules.api.pojo.DeviceResult;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;

/**
 * 调用硬件平台接口获取数据
 */
@Service
public class DeviceService {
    public static final String DEVICE_URL="http://192.168.0.35:8080/guard/api/device";
    //public static final String TOKEN  = "273f5b6b8c298bc718312fa8f54e5f6d";

    Logger logger = Logger.getLogger(DeviceService.class);
    /**
     * 获得单台设备信息
     * @param deviceId
     * @return
     */
    public  DeviceResult queryDeviceInfo(String deviceId){
        Result result = null;
        DeviceResult deviceResult=null;

        //JSONObject param = new JSONObject();
       // param.put("devid",deviceId);
        //param.put("token",TOKEN);

        String deviceUrl = DEVICE_URL + "/"+deviceId;
        try {
            String str = HttpClientUtil.get(deviceUrl);
            //String str = HttpClientUtil.post(deviceUrl,param);
            System.out.println("str:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){
                Object data= result.getData();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                /* String devId = jsonObject.getString("devId");*/
                deviceResult = JSONObject.parseObject(jsonObject.toString(),DeviceResult.class);
            }else{
                logger.info("获得单台设备信息信息失败！设备编号："+deviceId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deviceResult;
    }


    public static void main(String[] args) {
        try{
/*            DeviceResult f=queryDeviceInfo("00001111000011110000");
            System.out.println("str:"+f.getDevId());*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

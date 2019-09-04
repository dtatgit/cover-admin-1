package com.jeeplus.modules.api.service;

import com.jeeplus.common.config.Global;
import com.jeeplus.modules.api.pojo.*;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import com.jeeplus.modules.cv.constant.CodeConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调用硬件平台接口获取数据
 */
@Service
public class DeviceService {
    //public static final String DEVICE_URL="http://192.168.0.35:8080/guard/api/device";
    //public static final String TOKEN  = "273f5b6b8c298bc718312fa8f54e5f6d";

    Logger logger = Logger.getLogger(DeviceService.class);
    /**
     * 1.获得单台设备信息（根据设备编号）
     * @param deviceId
     * @return
     */
    public   DeviceResult getDeviceInfo(String deviceId){
        Result result = null;
        DeviceResult deviceResult=null;

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/"+deviceId;
        logger.info("******井卫硬件接口地址：********"+Global.getConfig("coverBell.server.url"));
        try {
            String str = HttpClientUtil.get(deviceUrl);
            //String str = HttpClientUtil.post(deviceUrl,param);
            System.out.println("1.获得单台设备信息（根据设备编号）接口:"+str);
            logger.info("******1.获得单台设备信息（根据设备编号）接口：********"+result);
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

    /**
     * 根据devId和时间点获取设备信息
     * @param devId
     * @param beginTime  格式：yyyy-MM-dd HH:mm:ss
     * @param endTime     格式：yyyy-MM-dd HH:mm:ss
     * @param pageNo  当前页
     * @param pageSize  每页数
     * @return
     */
    public  DeviceResult getDeviceInfoByTime(String devId, String beginTime,String endTime,Integer pageNo,Integer pageSize){
        Result result = null;
        DeviceResult deviceResult=null;
        Map param=new HashMap();
        //JSONObject param = new JSONObject();
        param.put("devId",devId);
        param.put("beginTime",beginTime);
        param.put("endTime",endTime);
        param.put("beginTime",pageNo);
        param.put("pageSize",pageSize);

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/deviceInfoRecordList";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            System.out.println("str:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){
                Object data= result.getData();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                /* String devId = jsonObject.getString("devId");*/
                deviceResult = JSONObject.parseObject(jsonObject.toString(),DeviceResult.class);
            }else{
                logger.info("2.获得单台设备信息信息失败！设备编号："+devId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deviceResult;
    }

    /**
     * 硬件接口待完善
     * @param pageNo
     * @param pageSize
     * @return
     */
    public  List<DeviceResult> getDeviceList(String pageNo, String pageSize){
        //TODO
        Result result = null;
        List<DeviceResult> deviceResultList = null;
        Map param=new HashMap();
        //JSONObject param = new JSONObject();
        param.put("pageNo",pageNo);
        param.put("pageSize",pageSize);

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/getDeviceList";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            System.out.println("str:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){
                Object data= result.getData();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                deviceResultList = JSONObject.parseArray(jsonObject.toString(),DeviceResult.class);
            }else{
                logger.info("获得批量设备信息失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deviceResultList;
    }

    /**
     *4.设置ip(域名)和端口
     * @param devId 设备编号
     * @param host  ip/域名
     * @param port  端口
     */
    public  Result setHostAndPort(String devId, String host, String port){
        Result result = null;
        //JSONObject param = new JSONObject();
        Map param=new HashMap();
        param.put("devId",devId);
        param.put("host",host);
        param.put("port",port);

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/setHostAndPort";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            System.out.println("4.设置ip(域名)和端口:"+str);
            logger.info("4.设置ip(域名)和端口:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){

            }else{
                logger.info("设置ip(域名)和端口信息失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     *5.获取ip(域名)和端口
     * @param devId 设备编号
     */
    public   Map<String,String> getHostAndPort(String devId){
        Result result = null;
        Map param=new HashMap();
        param.put("devId",devId);
        Map<String,String> map=new HashMap<String,String>();

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/getHostAndPort";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            System.out.println("5.获取ip(域名)和端口:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){
                Object data= result.getData();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                 String devNo = jsonObject.getString("devId");
                String host = jsonObject.getString("host");
                String port = jsonObject.getString("port");
                map.put("flag", "true");
                map.put("devId",devNo );
                map.put("host", host);
                map.put("port",port );
            }else{
                map.put("devId",devId );
                map.put("flag", "false");
                logger.info("获取ip(域名)和端口信息失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    /**
     *设防或撤防
     * @param devId  设备编号
     * @param opt   操作类型  1：设防;11：撤防
     * @return
     */
    public  Result setDefense(String devId, String opt){
        Result result = null;
        Map param=new HashMap();
        param.put("devId",devId);
        if(CodeConstant.DEFENSE_STATUS.FORTIFY.equals(opt)){//设防
            opt="1";
        }else if(CodeConstant.DEFENSE_STATUS.REVOKE.equals(opt)){
            opt="11";
        }
        param.put("opt",opt);
        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/defenceOrwithdrawal";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            System.out.println("6.设防或撤防:"+str);
            logger.info("6.设防或撤防接口！"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){

            }else{
                logger.info("设防或撤防操作失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 设备固件版本更新
     * @param devId
     * @return
     */
    public  Result updateDevice(String devId){
        Result result = null;
        //JSONObject param = new JSONObject();
        Map param=new HashMap();
        param.put("devId",devId);
        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/deviceUpdate";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            System.out.println("str:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){

            }else{
                logger.info("设防或撤防操作失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设备废弃
     * @param devId
     * @return
     */
    public  Result deviceScrap(String devId){
        Result result = null;
        Map param=new HashMap();
        param.put("devId",devId);
        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/deviceDiscard";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            System.out.println("11.设备废弃:"+str);
            logger.info("11.设备废弃！"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){

            }else{
                logger.info("设备废弃操作失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 2019-08-26
     * @return
     */
    public PageData getDeviceStateList(Map map){
        List<DeviceStateResult> deviceStateResultList = null;
        Result result = null;
        PageData pageData = new PageData();
        Map param=new HashMap();
        //JSONObject param = new JSONObject();
        param.put("devId",map.get("devId"));
        param.put("pageNo",map.get("pageNo"));
        param.put("pageSize",map.get("pageSize"));

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/deviceInfoSimpleList";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            System.out.println("获取设备状态信息str:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){
                Object data= result.getData();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                pageData = JSONObject.parseObject(jsonObject.toString(),PageData.class);

            /*    JSONObject f = (JSONObject) JSONObject.toJSON(pageData.getList());
                deviceStateResultList = JSONObject.parseArray(f.toString(),DeviceStateResult.class);
*/
            }else{
                logger.info("获得设备状态信息失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pageData;
    }




    public static void main(String[] args) {
        try{
            DeviceParameterService p=new DeviceParameterService();
            DeviceService service=new DeviceService();
/*            DeviceResult f=service.getDeviceInfo("00001111000011110000");
            service.setHostAndPort("00001111000011110000", "192.168.0.11", "88");
            service.getHostAndPort("00001111000011110000");
           service.setDefense("00001111000011110000", CodeConstant.DEFENSE_STATUS.FORTIFY);
            p.getDeviceParameter("00001111000011110000");
            DeviceParameterResult deviceParameter=new DeviceParameterResult();
            deviceParameter.setDevId("00001111000011110000");
            deviceParameter.setHeartbeatTime(66);
            deviceParameter.setAngleThreshold(88);
            p.setDeviceParameter(deviceParameter);
            p.getDeviceParameter("00001111000011110000");*/
            //System.out.println("接口一：获得单台设备信息（根据设备编号）:"+f.getDevId());

            Map map=new HashMap();
            map.put("devId","AT20190827000001");
            map.put("pageNo","1");
            map.put("pageSize","2");
            PageData pageData=   service.getDeviceStateList(map);
            System.out.println("getDeviceStateList:"+pageData.getTotal());
            List<Object> m = pageData.getList();
            for(Object o:m){
                DeviceStateResult r = JSONObject.parseObject(o.toString(),DeviceStateResult.class);
                System.out.println("**************"+r.getDevId());

            }


            System.out.println("getDeviceStateList:"+pageData.getList().size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

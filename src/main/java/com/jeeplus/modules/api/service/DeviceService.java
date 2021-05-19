package com.jeeplus.modules.api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.api.pojo.*;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import com.jeeplus.modules.cv.constant.CodeConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 1.获得单台设备信息（根据设备编号）
     * @param devNo
     * @return
     */
    public   DeviceResult getDeviceInfo(String devNo){
        Result result = null;
        DeviceResult deviceResult=null;

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/"+devNo;
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
                logger.info("获得单台设备信息信息失败！设备编号："+devNo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deviceResult;
    }

    /**
     * 获取设备表基础信息  add  by ffy
     * @param devNo
     * @return
     */
    public   DeviceInfo getDeviceInfo2(String devNo){
        Result result = null;
        DeviceInfo deviceResult=null;

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device2/"+devNo;
        logger.info("******getDeviceInfo2()------>设备:{},获取基础信息:{}********",devNo,deviceUrl);
        try {
            String str = HttpClientUtil.get(deviceUrl);
            //String str = HttpClientUtil.post(deviceUrl,param);
            //System.out.println("1.获得单台设备信息（根据设备编号）接口2:"+str);
            logger.info("************getDeviceInfo2()（根据设备编号）, 结果：********:"+str);

            result = JSONObject.parseObject(str,Result.class);

            if(result.getSuccess().equals("true")){
                Object data= result.getData();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                /* String devId = jsonObject.getString("devId");*/
                deviceResult = JSONObject.parseObject(jsonObject.toString(),DeviceInfo.class);
            }else{
                logger.info("getDeviceInfo2()------>获得单台设备信息信息失败2！设备编号："+devNo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deviceResult;
    }

    /**
     * 根据devNo和时间点获取设备信息
     * @param devNo
     * @param beginTime  格式：yyyy-MM-dd HH:mm:ss
     * @param endTime     格式：yyyy-MM-dd HH:mm:ss
     * @param pageNo  当前页
     * @param pageSize  每页数
     * @return
     */
    public  DeviceResult getDeviceInfoByTime(String devNo, String beginTime,String endTime,Integer pageNo,Integer pageSize){
        Result result = null;
        DeviceResult deviceResult=null;
        Map param=new HashMap();
        //JSONObject param = new JSONObject();
        param.put("devNo",devNo);
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
                logger.info("2.获得单台设备信息信息失败！设备编号："+devNo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deviceResult;
    }

    /**
     * 获取设备列表信息
     * @param devNo 设备编号
     * @param onlineState 在线状态 0：在线1：离线
     * @param fortificationState 设防状态 0：撤防1：设防
     * @param state 状态 0：正常1：休眠2：报废
     * @param beginTime
     * @param endTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    public  PageData getDeviceList(String devNo,String onlineState,String fortificationState,String state,String beginTime,String endTime,String pageNo, String pageSize){
        //TODO
        Result result = null;
        PageData pageData = new PageData();
        Map param=new HashMap();
        //JSONObject param = new JSONObject();
        if(StringUtils.isNotEmpty(devNo)){
            param.put("devNo",devNo);
        }
        if(StringUtils.isNotEmpty(onlineState)){
            param.put("onlineState",onlineState);
        }
        if(StringUtils.isNotEmpty(fortificationState)){
            param.put("fortificationState",fortificationState);
        }
        if(StringUtils.isNotEmpty(state)){
            param.put("state",state);
        }
        if(StringUtils.isNotEmpty(beginTime)){
            param.put("beginTime",beginTime);
        }
        if(StringUtils.isNotEmpty(endTime)){
            param.put("endTime",endTime);
        }
        if(StringUtils.isNotEmpty(pageNo)){
            param.put("pageNo",pageNo);
        }
        if(StringUtils.isNotEmpty(pageSize)){
            param.put("pageSize",pageSize);
        }

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/getDeviceList";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            System.out.println("str:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){
                Object data= result.getData();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                pageData = JSONObject.parseObject(jsonObject.toString(),PageData.class);

            }else{
                logger.info("获得批量设备信息失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pageData;
    }

    /**
     *4.设置ip(域名)和端口
     * @param devNo 设备编号
     * @param host  ip/域名
     * @param port  端口
     */
    public  Result setHostAndPort(String devNo, String host, String port){
        Result result = null;
        //JSONObject param = new JSONObject();
        Map param=new HashMap();
        param.put("devNo",devNo);
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
     * @param devNo 设备编号
     */
    public   Map<String,String> getHostAndPort(String devNo){
        Result result = null;
        Map param=new HashMap();
        param.put("devNo",devNo);
        Map<String,String> map=new HashMap<String,String>();

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/getHostAndPort";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            System.out.println("5.获取ip(域名)和端口:"+str);
            result = JSONObject.parseObject(str,Result.class);
            if(result.getSuccess().equals("true")){
                Object data= result.getData();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                 //String devNo = jsonObject.getString("devNo");
                String host = jsonObject.getString("host");
                String port = jsonObject.getString("port");
                map.put("flag", "true");
                map.put("devId",devNo );
                map.put("host", host);
                map.put("port",port );
            }else{
                map.put("devNo",devNo );
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
     * @param devNo  设备编号
     * @param opt   操作类型  1：设防;0：撤防
     * @return
     */
    public  Result setDefense(String devNo, String opt){
        Result result = null;
        Map param=new HashMap();
        param.put("devNo",devNo);
        if(CodeConstant.DEFENSE_STATUS.FORTIFY.equals(opt)){//设防
            opt="1";
        }else if(CodeConstant.DEFENSE_STATUS.REVOKE.equals(opt)){
            opt="0";
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
                logger.info("-->"+result.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 设备固件版本更新
     * @param devNo
     * @return
     */
    public  Result updateDevice(String devNo){
        Result result = null;
        //JSONObject param = new JSONObject();
        Map param=new HashMap();
        param.put("devNo",devNo);
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
     * @param devNo
     * @return
     */
    public  Result deviceScrap(String devNo){
        Result result = null;
        Map param=new HashMap();
        param.put("devNo",devNo);
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
        param.put("devNo",map.get("devNo"));
        param.put("pageNo",map.get("pageNo"));
        param.put("pageSize",map.get("pageSize"));

        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/deviceInfoSimpleList";
        try {
            logger.info("deviceUrl:"+deviceUrl);
            logger.info("参数:"+param);
            String str = HttpClientUtil.doPost(deviceUrl,param);
            logger.info("获取设备状态信息str:"+str);
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


    /**
     *13获取告警设备列表
     * @return
     */
    public  List<AlarmDevice> getAlarmDeviceList(){
        Result result = null;
        List<AlarmDevice> AlarmDeviceList = null;
        Map param=new HashMap();
  /*      param.put("pageNo",pageNo);
        param.put("pageSize",pageSize);*/
        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/alarmDeviceList";
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            //System.out.println("str:"+str);
            logger.info("13.获取告警设备列表结果---》"+str);
            if(StringUtils.isNotBlank(str)){
                result = JSONObject.parseObject(str,Result.class);
                if(result.getSuccess().equals("true")){
                    Object data= result.getData();
                    System.out.println("data获取告警设备列表！"+data);

                    //String jsonString=JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue);
                    //AlarmDeviceList = JSONObject.parseArray(jsonString,AlarmDevice.class);
                    JSONArray jsonObject = (JSONArray) JSONObject.toJSON(data);
                    AlarmDeviceList = JSONObject.parseArray(jsonObject.toString(),AlarmDevice.class);
                }else{
                    logger.info("13.获取告警设备列表失败！");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return AlarmDeviceList;
    }

    /**
     * 14、根据devNo获取告警设备
     * @param devNo
     * @return
     */
    public   AlarmDevice getAlarmDeviceInfo(String devNo){
        Result result = null;
        AlarmDevice alarmDevice=null;
        Map param=new HashMap();
        param.put("devNo",devNo);


        String deviceUrl = Global.getConfig("coverBell.server.url") + "/device/alarmDevice";
        logger.info("******井卫硬件接口地址：********"+Global.getConfig("coverBell.server.url"));
        try {
            String str = HttpClientUtil.doPost(deviceUrl,param);
            //String str = HttpClientUtil.post(deviceUrl,param);
            System.out.println("14、根据devNo获取告警设备接口:"+str);
            logger.info("******14、根据devNo获取告警设备接口：********"+result);
            result = JSONObject.parseObject(str,Result.class);

            if(result.getSuccess().equals("true")){
                Object data= result.getData();
                JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
                /* String devId = jsonObject.getString("devId");*/
                alarmDevice = JSONObject.parseObject(jsonObject.toString(),AlarmDevice.class);
            }else{
                logger.info("获得告警设备信息失败！设备编号："+devNo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return alarmDevice;
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

  /*          Map map=new HashMap();
           map.put("devId","AS190930000012");
            map.put("pageNo","1");
            map.put("pageSize","2");
          PageData pageData=   service.getDeviceStateList(map);
            System.out.println("getDeviceStateList:"+pageData.getTotal());
            List<Object> m = pageData.getList();
            for(Object o:m){
                DeviceStateResult r = JSONObject.parseObject(o.toString(),DeviceStateResult.class);
                System.out.println("**************"+r.getDevNo());

            }*/
           // List<AlarmDevice> alarmDeviceList = service.getAlarmDeviceList();
            //System.out.println("getDeviceStateList:"+alarmDeviceList.size());
            //AlarmDevice g=service.getAlarmDeviceInfo("BT191022000005");
            //System.out.println("*************:"+g.getDevNo());
            PageData pageDatas= service.getDeviceList(null,null,null,null,null,null,"1", "100");
            List<Object> list =pageDatas.getList();
            for(Object o:list){
                DeviceListResult r = JSONObject.parseObject(o.toString(),DeviceListResult.class);
                System.out.println("*************:"+r.getDevNo());

            }
/*            for(DeviceListResult d:pageDatas.getList()){
                System.out.println("*************:"+d.getDevNo());
            }*/


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

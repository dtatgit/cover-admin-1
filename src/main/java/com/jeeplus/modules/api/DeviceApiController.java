package com.jeeplus.modules.api;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import com.jeeplus.modules.api.vo.BellWaterVO;
import com.jeeplus.modules.api.vo.CoverBellData;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 目前提供给大屏的接口(不直接连硬件管理平台，在这里做个中转)
 */
@RestController
@RequestMapping("/guard/api")
public class DeviceApiController {

    private static Logger logger = LoggerFactory.getLogger(DeviceApiController.class);
    @Autowired
    private CoverService coverService;

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


    @RequestMapping("/queryData")
    public AppResult queryBellData(@RequestParam(value = "coverNo",required = false)String coverNo) {

        AppResult result = new AppResult();
       //测矩形 ranging
       // if(StringUtils.isNotEmpty(coverNos)){
//            List<String> list = Arrays.asList(coverNos.split(","));
//            Map<String, Object> param = new HashMap<>();
//            param.put("list", list);
            List<CoverBellData> dataList=   coverService.queryCoverData(coverNo);
            if(CollectionUtil.isNotEmpty(dataList)){
                List<String> bellNoIds = dataList.stream().map(CoverBellData::getBellNo).collect(Collectors.toList());
                String bellNos=StringUtils.join(bellNoIds.toArray(), ",");
                //String deviceUrl = coverBellServerUrl + "/device/waterLevelInfo";
                String deviceUrl="http://221.229.200.157:8086/guard/api/device/waterLevelInfo";
                Map<String, Object> params = new HashMap<>();
                params.put("devNos", bellNos);
                String str = HttpClientUtil.doPost(deviceUrl, params);
                Result result2 = JSONObject.parseObject(str, Result.class);
                boolean success = result2.getSuccess().equals("true");
                Object data = result2.getData();
                if (success) {
                    List<BellWaterVO> list= JSON.parseArray(JSON.toJSONString(data), BellWaterVO.class);
                    result.setData(changeData(dataList,list));
                    logger.info("获取数位数据成功：" + data.toString());
                } else {
                    logger.info("获取数位数据失败！" );
                }
            }
         result.setSuccess(true);
        return result;
    }


    public List<CoverBellData> changeData( List<CoverBellData> dataList,List<BellWaterVO> BellWaterList){
        List<CoverBellData> data=new ArrayList<CoverBellData>();
        if(CollectionUtil.isEmpty(dataList)){
            return dataList;
        }
        Map<String, Object> data2 = new HashMap<>();
        if(CollectionUtil.isNotEmpty(BellWaterList)){
          for(BellWaterVO vo:BellWaterList){
              data2.put(vo.getDevNo(), vo);
          }
        }
        dataList.stream().forEach(coverBellData -> {
           String bellNo= coverBellData.getBellNo();
            BellWaterVO water= (BellWaterVO)data2.get(bellNo);
            if(null!=water){
                coverBellData.setDepth(water.getDistance().toString());
                coverBellData.setWaterLevelThreshold(water.getThreshold().toString());
            }
        });
        return dataList;
    }


}

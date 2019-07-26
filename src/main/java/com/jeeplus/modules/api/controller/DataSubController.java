package com.jeeplus.modules.api.controller;
import com.jeeplus.modules.api.pojo.*;
import com.jeeplus.modules.api.service.DataSubService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 数据订阅接口
 * 数据订阅接口功能为井卫设备上报报警数据，该控制层集中接收报警数据，并对报警数据进行业务处理
 */
@Controller
@RequestMapping("/api/")
public class DataSubController {
    @Autowired
    private DataSubService service;
    Logger logger = Logger.getLogger(DataSubController.class);
    @RequestMapping(value="data_sub",method = RequestMethod.POST,
            consumes="application/json", produces="application/json")
    @ResponseBody
    public Result dataSub(@RequestBody DataSubParam param){
        logger.info("########报警数据上报start#######");
        Result result = new Result();
        try {
            //CgChargingPoint chargingPoint = cgChargingPointService.findUniqueByProperty("equ_num", param.getDevid());
            //if(chargingPoint==null){
                //设备不存在，注册设备信息
                result = service.processData(param);
            //}else{

           // }
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(0);
            //result.setMsg(Constants.MSG.PARAM_ERROR);
        }

        logger.info("########报警数据上报end#######");
        return result;
    }
}

package com.jeeplus.modules.api.controller;

import com.antu.message.Message;
import com.antu.message.dispatch.MessageDispatcher;
import com.jeeplus.modules.api.pojo.DataSubParam;
import com.jeeplus.modules.api.pojo.DataSubParamInfo;
import com.jeeplus.modules.api.pojo.DeviceSimpleParam;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.service.DataSubService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(DataSubController.class);

    private final DataSubService service;
    private final MessageDispatcher messageDispatcher;

    @Autowired
    public DataSubController(DataSubService service, MessageDispatcher messageDispatcher) {
        this.service = service;
        this.messageDispatcher = messageDispatcher;
    }

    @RequestMapping(value="data_sub",method = RequestMethod.POST,
            consumes="application/json", produces="application/json")
    @ResponseBody
    public Result dataSub(@RequestBody DataSubParam param){
        logger.info("########报警数据上报start#######");
        Result result = new Result();
        try {
            //设备不存在，注册设备信息
            result = service.processData(param);
            messageDispatcher.publish(CodeConstant.GUARD_TOPIC.ALARM, Message.of(param));
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(0);
            //result.setMsg(Constants.MSG.PARAM_ERROR);
        }

        logger.info("########报警数据上报end#######");
        return result;
    }

    @RequestMapping(value="data_sub_info",method = RequestMethod.POST,
            consumes="application/json", produces="application/json")
    @ResponseBody
    public Result dataSubInfo(@RequestBody DataSubParamInfo param){
        logger.info("########数据订阅接口start#######");
        Result result = new Result();
        try {
            //设备不存在，注册设备信息
            result = service.processDataInfo(param);
            messageDispatcher.publish(CodeConstant.GUARD_TOPIC.ONLINE, Message.of(param));
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(0);
            //result.setMsg(Constants.MSG.PARAM_ERROR);
        }

        logger.info("########数据订阅接口end#######");
        return result;
    }


    /**
     * 完善井卫设备信息
     * @param param
     * @return
     */
    @RequestMapping(value="perfect_device_info",method = RequestMethod.POST,
            consumes="application/json", produces="application/json")
    @ResponseBody
    public Result perfectDeviceInfo(@RequestBody DeviceSimpleParam param){
        Result result = new Result();
        //设备不存在，注册设备信息
        result = service.processDeviceInfo(param);

        logger.info("########数据订阅接口end#######");
        return result;
    }
}

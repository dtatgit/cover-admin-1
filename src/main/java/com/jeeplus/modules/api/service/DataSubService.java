package com.jeeplus.modules.api.service;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.api.constant.Constants;
import com.jeeplus.modules.api.pojo.*;
import com.jeeplus.modules.api.utils.bellUtils;
import com.jeeplus.modules.cb.entity.alarm.CoverBellAlarm;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.service.alarm.CoverBellAlarmService;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 数据订阅
 * 井卫设备上报报警数据处理业务
 */
@Service
public class DataSubService {
    Logger logger = Logger.getLogger(DataSubService.class);
    @Autowired
    private CoverBellAlarmService coverBellAlarmService;
    @Autowired
    private CoverBellService coverBellService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CoverWorkService coverWorkService;
    public Result processData(DataSubParam param){

        logger.info("###################进入processData###############################");
        Result result = new Result();

        String retMsg = "";
        //解析参数
        String devNo= param.getDevNo();//设备编号
        String alarmType=param.getAlarmType();//告警类型
        String value=param.getValue();//告警具体值
        Date alarmTime=param.getAlarmTime();//告警时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("报警时间:" + sdf.format(new Date()) +"########" + "设备编号:" + devNo);
        try {
            CoverBellAlarm coverBellAlarm = new CoverBellAlarm();
            coverBellAlarm.setBellNo(devNo);// 井铃编号
            coverBellAlarm.setAlarmNum(IdGen.getInfoCode("AR"));// 报警编号
            coverBellAlarm.setAlarmType(alarmType);// 报警类型
            if (StringUtils.isNotEmpty(value)) {
                coverBellAlarm.setCurrentValue(Double.parseDouble(value));// 当前值
            }
            coverBellAlarm.setAlarmDate(alarmTime);// 报警时间
            CoverBell coverBell = coverBellService.findUniqueByProperty("bell_no", devNo);
            if (null != coverBell) {
                coverBellAlarm.setCoverId(coverBell.getCoverId());// 井盖ID
                coverBellAlarm.setCoverNo(coverBell.getCoverNo());// 井盖编号
                coverBellAlarm.setCoverBellId(coverBell.getId());
            }else{
                //井卫数据为空，则自动注册
                DeviceResult deviceResult= deviceService.getDeviceInfo(devNo);
                CoverBell bell=new CoverBell();
                bell.setBellNo(devNo);//设备编号
                bell.setBellType(deviceResult.getdType());//设备类型（sy,sz,wx）
                bell.setVersion(deviceResult.getVersion());//版本号
                bell.setDefenseStatus(bellUtils.changeDefenseStatus(deviceResult.getFortifyState()));//设防状态
                bell.setWorkStatus(CodeConstant.BELL_WORK_STATUS.ON);// 工作状态
                bell.setBellStatus(CodeConstant.BELL_STATUS.init);// 生命周期
                coverBellService.save(bell);
            }

            //2019-10-09 没有安装的井卫不进行报警数据接收
            String  coverId= coverBellAlarm.getCoverId();
            if(StringUtils.isNotEmpty(coverId)){

                coverBellAlarm.setIsGwo(CodeConstant.BOOLEAN.NO);
                coverBellAlarmService.save(coverBellAlarm);
                //add by 2019-11-12 井卫报警自动生成工单, 对报警工单根据井盖维护单位自动派单,注意：无权属单位
                coverWorkService.createCoverWork(coverBellAlarm);


                result.setCode(0);
                result.setData(coverBellAlarm);
                retMsg="报警数据上报成功!";
            }else{
                result.setCode(1);
                result.setData(null);
                retMsg="报警数据上报失败,请核实井卫是否安装？";
            }

        }catch (Exception e){
            e.printStackTrace();
            result.setCode(1);
            result.setData(null);
            retMsg="报警数据上报失败!";
        }
        result.setMsg(retMsg);
        return result;
    }





    public Result processDataInfo(DataSubParamInfo param){
        logger.info("###################进入processData###############################");
        Result result = new Result();

        String retMsg = "";
        //解析参数
        String cmd = param.getCmd();

        Object data = param.getData();

        String devNo = param.getDevNo();

        logger.info("###################CMD命令:"+cmd +"执行开始###############################");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("时间:" + sdf.format(new Date()) +"########" + "设备编号:" + devNo + "参数:" + data);
        if(Constants.CMD.ONLINE.equals(cmd)){
            //设备上线  1
            //retMsg = deviceService.processOnline(deviceId);
            retMsg = coverBellService.processWorkStatus(devNo, CodeConstant.BELL_WORK_STATUS.ON);
        }else if(Constants.CMD.OFFLINE.equals(cmd)){
            //设备离线  0
            //retMsg = deviceService.processOffline(deviceId);
            retMsg = coverBellService.processWorkStatus(devNo, CodeConstant.BELL_WORK_STATUS.OFF);
        }

        if(Constants.MSG.SUCCESS.equals(retMsg)){
            result.setCode(0);
            result.setMsg(Constants.MSG.OPERATE_OK);
        }else{
            result.setCode(1);
            result.setMsg(retMsg);
        }
        logger.info("###############CMD命令:"+cmd +"执行结果"+result.getCode()+","+result.getMsg());
        logger.info("###############CMD命令:"+cmd +"执行结束!!#############################");
        return result;

    }

}

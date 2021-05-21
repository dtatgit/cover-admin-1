package com.jeeplus.modules.api.service;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.concurrent.ThreadLocalContext;
import com.jeeplus.modules.api.constant.Constants;
import com.jeeplus.modules.api.pojo.DataSubParam;
import com.jeeplus.modules.api.pojo.DataSubParamInfo;
import com.jeeplus.modules.api.pojo.DeviceSimpleParam;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.cb.entity.alarm.CoverBellAlarm;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.service.alarm.CoverBellAlarmService;
import com.jeeplus.modules.cb.service.bizAlarm.BizAlarmService;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据订阅
 * 井卫设备上报报警数据处理业务
 */
@Service
public class DataSubService {
    private static Logger logger = LoggerFactory.getLogger(DataSubService.class);
    @Autowired
    private CoverBellAlarmService coverBellAlarmService;
    @Autowired
    private CoverBellService coverBellService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CoverWorkService coverWorkService;
    @Autowired
    private BizAlarmService bizAlarmService;


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
            //释放本地线程
            ThreadLocalContext.remove(ThreadLocalContext.PROJECT_ID);
            ThreadLocalContext.remove(ThreadLocalContext.PROJECT_NAME);
            //查询井卫信息
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("bellNo", devNo);
            CoverBell coverBellObj = coverBellService.queryCoverBell(paramMap);
            if (coverBellObj != null) {
                logger.debug("井卫项目开始====(数据上报): {}({})", coverBellObj.getProjectId(), coverBellObj.getProjectName());
                String city = StringUtils.isBlank(coverBellObj.getCity()) ? "" : coverBellObj.getCity();
                String district = StringUtils.isBlank(coverBellObj.getDistrict()) ? "" : coverBellObj.getDistrict();
                String township = StringUtils.isBlank(coverBellObj.getTownship()) ? "" : coverBellObj.getTownship();
                param.setStreetName(city + district + township);
                param.setDevPurpose(coverBellObj.getPurpose());
                ThreadLocalContext.put(ThreadLocalContext.PROJECT_ID, coverBellObj.getProjectId());
                ThreadLocalContext.put(ThreadLocalContext.PROJECT_NAME, coverBellObj.getProjectName());
                logger.debug("井卫项目(数据上报): {}({})", coverBellObj.getProjectId(), coverBellObj.getProjectName());
            } else {
                throw new Exception("井卫项目上报失败：井卫查询不到");
            }

            CoverBellAlarm coverBellAlarm = new CoverBellAlarm();
            coverBellAlarm.setBellNo(devNo);// 井卫编号
            coverBellAlarm.setAlarmNum(IdGen.getInfoCode("AR"));// 报警编号
            coverBellAlarm.setAlarmType(alarmType);// 报警类型
            if (StringUtils.isNotEmpty(value)) {
                coverBellAlarm.setCurrentValue(Double.parseDouble(value));// 当前值
            }
            coverBellAlarm.setAlarmDate(alarmTime);// 报警时间
            //CoverBell coverBell = coverBellService.findUniqueByProperty("a.bell_no", devNo);
            if (null != coverBellObj) {
                coverBellAlarm.setCoverId(coverBellObj.getCoverId());// 井盖ID
                coverBellAlarm.setCoverNo(coverBellObj.getCoverNo());// 井盖编号
                coverBellAlarm.setCoverBellId(coverBellObj.getId());
            }else{
                //暂时去掉报警注册 （add by ffy）
                //井卫数据为空，则自动注册
//                DeviceInfo deviceResult= deviceService.getDeviceInfo2(devNo);
//                CoverBell bell=new CoverBell();
//                bell.setBellNo(devNo);//设备编号
//                bell.setDefenseStatus(CodeConstant.DEFENSE_STATUS.REVOKE);  //先给默认撤防状态，如果能查到数据，则会修改
//                if(deviceResult!=null){
//                    bell.setBellType(deviceResult.getDeviceType());//设备类型
//                    bell.setVersion(deviceResult.getVersion());//版本号
//                    bell.setImei(deviceResult.getImei());   //add by ffy
//                    bell.setSim(deviceResult.getIccid());   //add by ffy
//                    bell.setDefenseStatus(bellUtils.changeDefenseStatus(deviceResult.getFortifyState()));//设防状态
//                }
//                bell.setWorkStatus(CodeConstant.BELL_WORK_STATUS.ON);// 工作状态
//                bell.setBellStatus(CodeConstant.BELL_STATUS.init);// 生命周期
//                coverBellService.save(bell);
            }

            //2019-10-09 没有安装的井卫不进行报警数据接收
            String  coverId= coverBellAlarm.getCoverId();
            if(StringUtils.isNotEmpty(coverId)){

                coverBellAlarm.setIsGwo(CodeConstant.BOOLEAN.NO);
                coverBellAlarmService.save(coverBellAlarm);
                //add by 2019-11-12 井卫报警自动生成工单, 对报警工单根据井盖维护单位自动派单,注意：无权属单位
                //coverWorkService.createCoverWork(coverBellAlarm);

                //处理业务报警
                bizAlarmService.processBizAlarm(param);

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
            logger.error("processData 数据上报失败："+ e.getMessage());
            result.setCode(1);
            result.setData(null);
            retMsg="报警数据上报失败!";
        } finally {
            //释放本地线程
            ThreadLocalContext.remove(ThreadLocalContext.PROJECT_ID);
            ThreadLocalContext.remove(ThreadLocalContext.PROJECT_NAME);
        }
        result.setMsg(retMsg);
        return result;
    }





    public Result processDataInfo(DataSubParamInfo param) throws Exception{
        logger.info("###################进入processData###############################");
        Result result = new Result();
        Boolean flag = true;
        String retMsg = "";
        //解析参数
        String cmd = param.getCmd();

        Object data = param.getData();

        String devNo = param.getDevNo();

        logger.info("###################CMD命令:"+cmd +"执行开始###############################");
        //释放本地线程
        ThreadLocalContext.remove(ThreadLocalContext.PROJECT_ID);
        ThreadLocalContext.remove(ThreadLocalContext.PROJECT_NAME);
        //查询井卫信息
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bellNo", devNo);
        CoverBell coverBellObj = coverBellService.queryCoverBell(paramMap);
        ThreadLocalContext.put(ThreadLocalContext.PROJECT_ID, coverBellObj.getProjectId());
        ThreadLocalContext.put(ThreadLocalContext.PROJECT_NAME, coverBellObj.getProjectName());
        logger.debug("井卫项目(设备上线): {}({})", coverBellObj.getProjectId(), coverBellObj.getProjectName());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            logger.info("时间:" + sdf.format(new Date()) + "########" + "设备编号:" + devNo + "参数:" + data);
            if (Constants.CMD.ONLINE.equals(cmd)) {
                //设备上线  1
                //retMsg = deviceService.processOnline(deviceId);
                retMsg = coverBellService.processWorkStatus(devNo, CodeConstant.BELL_WORK_STATUS.ON);
            } else if (Constants.CMD.OFFLINE.equals(cmd)) {
                //设备离线  0
                //retMsg = deviceService.processOffline(deviceId);
                retMsg = coverBellService.processWorkStatus(devNo, CodeConstant.BELL_WORK_STATUS.OFF);

                //处理业务报警
                flag = bizAlarmService.processOfflineBizAlarm(param);
                retMsg = flag ? Constants.MSG.SUCCESS : Constants.MSG.FAIL;
            }

            if (Constants.MSG.SUCCESS.equals(retMsg)) {
                result.setCode(0);
                result.setMsg(Constants.MSG.OPERATE_OK);
            } else {
                result.setCode(1);
                result.setMsg(retMsg);
            }
        } finally {
            //释放本地线程
            ThreadLocalContext.remove(ThreadLocalContext.PROJECT_ID);
            ThreadLocalContext.remove(ThreadLocalContext.PROJECT_NAME);
        }
        logger.info("###############CMD命令:"+cmd +"执行结果"+result.getCode()+","+result.getMsg());
        logger.info("###############CMD命令:"+cmd +"执行结束!!#############################");
        return result;

    }


    /**
     * 完善井卫设备基础信息
     * @param param
     * @return
     */
    public Result processDeviceInfo(DeviceSimpleParam param){

        Result result = new Result();

        try {
            logger.info("###################进入processDeviceInfo()begin###############################");
            logger.info("参数:{}",param);
            String devNo = param.getDevNo();

            int count = coverBellService.selCountByDevNo(devNo);
            if(count > 0){
                coverBellService.updateByDevNo(param);

                result.setMsg("设备:"+devNo+",修改简单数据成功");
            }else{
                //添加(注册)
                CoverBell bell=new CoverBell();
                bell.setBellNo(devNo);//设备编号
                bell.setBellType(param.getDeviceType());//设备类型
                bell.setVersion(param.getVersion());//版本号
                bell.setImei(param.getImei());   //add by ffy
                bell.setSim(param.getIccid());   //add by ffy
                bell.setDefenseStatus(CodeConstant.DEFENSE_STATUS.REVOKE);//设防状态
                bell.setWorkStatus(CodeConstant.BELL_WORK_STATUS.ON);// 工作状态
                bell.setBellStatus(CodeConstant.BELL_STATUS.notinstalled);// 生命周期

                coverBellService.save(bell);

                result.setMsg("设备:"+devNo+",注册成功");
            }
            result.setCode(0);
            logger.info("###################进入processDeviceInfo()end###############################");
        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("接口调用异常");
            e.printStackTrace();
        }

        return result;
    }

}

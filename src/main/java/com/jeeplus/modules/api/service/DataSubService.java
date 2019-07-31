package com.jeeplus.modules.api.service;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.api.pojo.*;
import com.jeeplus.modules.cb.entity.alarm.CoverBellAlarm;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.service.alarm.CoverBellAlarmService;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
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
    public Result processData(DataSubParam param){

        logger.info("###################进入processData###############################");
        Result result = new Result();

        String retMsg = "";
        //解析参数
        String devId= param.getDevId();//设备编号
        String alarmType=param.getAlarmType();//告警类型
        String value=param.getValue();//告警具体值
        Date alarmTime=param.getAlarmTime();//告警时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       logger.info("报警时间:" + sdf.format(new Date()) +"########" + "设备编号:" + devId);
       try {
           CoverBellAlarm coverBellAlarm = new CoverBellAlarm();
           coverBellAlarm.setBellNo(devId);// 井铃编号
           coverBellAlarm.setAlarmNum(IdGen.getInfoCode("AR"));// 报警编号
           coverBellAlarm.setAlarmType(alarmType);// 报警类型
           if (StringUtils.isNotEmpty(value)) {
               coverBellAlarm.setCurrentValue(Double.parseDouble(value));// 当前值
           }
           coverBellAlarm.setAlarmDate(alarmTime);// 报警时间
           CoverBell coverBell = coverBellService.findUniqueByProperty("bell_no", devId);
           if (null != coverBell) {
               coverBellAlarm.setCoverId(coverBell.getCoverId());// 井盖ID
               coverBellAlarm.setCoverNo(coverBell.getCoverNo());// 井盖编号
               coverBellAlarm.setCoverBellId(coverBell.getId());
           }else{
              //井卫数据为空，则自动注册
               DeviceResult deviceResult= deviceService.getDeviceInfo(devId);
               CoverBell bell=new CoverBell();
               bell.setBellNo(devId);//设备编号
               bell.setBellType(deviceResult.getdType());//设备类型（sy,sz,wx）
               bell.setVersion(deviceResult.getVersion());//版本号
               bell.setDefenseStatus(changeDefenseStatus(deviceResult.getFortificationState()));//设防状态
               bell.setWorkStatus(CodeConstant.BELL_WORK_STATUS.ON);// 工作状态
               bell.setBellStatus(CodeConstant.BELL_STATUS.init);// 生命周期
               coverBellService.save(bell);
           }
           coverBellAlarm.setIsGwo(CodeConstant.BOOLEAN.NO);
           coverBellAlarmService.save(coverBellAlarm);
           result.setCode(0);
           result.setData(coverBellAlarm);
           retMsg="报警数据上报成功!";
       }catch (Exception e){
           e.printStackTrace();
           result.setCode(1);
           result.setData(null);
           retMsg="报警数据上报失败!";
       }
        result.setMsg(retMsg);
        return result;
    }

    /**
     *
     * @param fortificationState //设防状态： 0:已设防1：已撤防
     * @return
     */
    public String changeDefenseStatus(Integer fortificationState){
        String defenseStatus="";
        if(fortificationState==0){
            defenseStatus=CodeConstant.DEFENSE_STATUS.FORTIFY;
        }else{
            defenseStatus=CodeConstant.DEFENSE_STATUS.REVOKE;
        }
        return defenseStatus;
    }

}

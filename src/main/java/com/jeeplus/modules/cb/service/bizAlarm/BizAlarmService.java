/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.bizAlarm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.api.pojo.DataSubParam;
import com.jeeplus.modules.api.pojo.DataSubParamInfo;
import com.jeeplus.modules.cb.constant.bizAlarm.BizAlarmConstant;
import com.jeeplus.modules.cb.entity.coverBizAlarm.CoverBizAlarm;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.entity.exceptionReport.ExceptionReport;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.service.coverBizAlarm.CoverBizAlarmService;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.cv.entity.statis.BizAlarmParam;
import com.jeeplus.modules.cv.entity.statis.BizAlarmStatisBo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.bizAlarm.BizAlarm;
import com.jeeplus.modules.cb.mapper.bizAlarm.BizAlarmMapper;

/**
 * 业务报警Service
 *
 * @author Peter
 * @version 2020-10-13
 */
@Service
@Transactional
public class BizAlarmService extends CrudService<BizAlarmMapper, BizAlarm> {

    @Autowired
    private CoverBizAlarmService coverBizAlarmService;

    @Autowired
    private CoverService coverService;
    @Autowired
    private CoverWorkService coverWorkService;
    @Autowired
    private CoverBellService coverBellService;

    @Autowired
    private BizAlarmMapper bizAlarmMapper;

    public BizAlarm get(String id) {
        return super.get(id);
    }

    public List<BizAlarm> findList(BizAlarm bizAlarm) {
        return super.findList(bizAlarm);
    }

    public Page<BizAlarm> findPage(Page<BizAlarm> page, BizAlarm bizAlarm) {
        return super.findPage(page, bizAlarm);
    }

    @Transactional(readOnly = false)
    public void save(BizAlarm bizAlarm) {
        super.save(bizAlarm);
    }

    @Transactional(readOnly = false)
    public void delete(BizAlarm bizAlarm) {
        super.delete(bizAlarm);
    }


    public void processBizAlarm(DataSubParam dataSubParam) throws Exception {
        //处理参数
        DataParam dataParam = processParam(dataSubParam, null);
        //创建业务报警
        BizAlarm bizAlarm = createBizAlarm(dataParam);
        //生成业务报警工单
        coverWorkService.createBizAlarmWork(bizAlarm);
    }

    public Boolean processOfflineBizAlarm(DataSubParamInfo dataSubParamInfo) {
		try {
			//处理参数
			DataParam dataParam = processParam(null, dataSubParamInfo);
			//创建业务报警
			BizAlarm bizAlarm = createBizAlarm(dataParam);
			//生成业务报警工单
			coverWorkService.createBizAlarmWork(bizAlarm);
		} catch (Exception e) {
			logger.error("处理离线业务报警异常！");
			e.printStackTrace();
			return false;
		}

		return true;
    }

    public DataParam processParam(DataSubParam dataSubParam, DataSubParamInfo dataSubParamInfo) throws Exception {
        DataParam dataParam = null;
        if (dataSubParam != null) {
            dataParam = new DataParam();
            BeanUtils.copyProperties(dataSubParam, dataParam);
        } else if (dataSubParamInfo != null) {
            dataParam = new DataParam();
            dataParam.setDevNo(dataSubParamInfo.getDevNo());
            dataParam.setAlarmType(dataSubParamInfo.getCmd());
        }
        if (dataParam != null) {
            CoverBell coverBell = coverBellService.findUniqueByProperty("bell_no", dataParam.getDevNo());
			if (coverBell == null) {
				throw new Exception("查无井卫信息");
			}
			Cover cover = coverService.get(coverBell.getCoverId());
			if (cover == null) {
            	throw new Exception("查无井盖信息");
			}
            dataParam.setCoverBell(coverBell);
            dataParam.setCover(cover);
        }
        return dataParam;
    }

    public BizAlarm createBizAlarm(DataParam param) {
        BizAlarm bizAlarm = null;
        if (StringUtils.isNotBlank(param.getAlarmType())) {
            //当前上传异常报警
            String bizAlarmType = exceptionAlarm(param.getAlarmType());
            if (StringUtils.isNotBlank(bizAlarmType)) {
                param.setAlarmType(bizAlarmType);
                //查询当前井盖状态
                Map<String, Object> map = new HashMap<>();
                map.put("coverId", param.getCoverBell().getCoverId());
                map.put("alarmType", bizAlarmType);
                List<CoverBizAlarm> coverBizAlarms = coverBizAlarmService.queryByParam(map);
                //当前井盖状态正常产生业务报警
                if (CollectionUtils.isEmpty(coverBizAlarms)) {
                    bizAlarm = saveBizAlarm(param);
                    coverBizAlarmService.createCoverBizAlarm(param.getCoverBell().getCoverId(), bizAlarmType);
                }
            }
        }
        return bizAlarm;
    }


    //是否异常报警
    public String exceptionAlarm(String alarmType) {
        switch (alarmType) {
            case "10":
            case "20":
            case "30":
            case "40":
            case "50":
            case "60":
            case "online":
                return null;
            case "offline":
                return BizAlarmConstant.BizAlarmType.OFFLINE;
        }
        //异常业务报警类型
        String bizAlarm = alarmType.substring(0, 1);
        switch (bizAlarm) {
            case "1":
                return BizAlarmConstant.BizAlarmType.OPEN;
            case "2":
                return BizAlarmConstant.BizAlarmType.WATER_LEVEL;
            case "3":
                return BizAlarmConstant.BizAlarmType.VOTAGE;
            case "4":
                return BizAlarmConstant.BizAlarmType.TEMPERATURE;
            case "5":
                return BizAlarmConstant.BizAlarmType.VIBRATE;
            case "6":
                return BizAlarmConstant.BizAlarmType.BROKEN;
        }
        return null;
    }


    //产生业务报警
    public BizAlarm saveBizAlarm(DataParam param) {
        BizAlarm bizAlarm = new BizAlarm();
        bizAlarm.setCoverId(param.getCoverBell().getCoverId());
        bizAlarm.setCoverNo(param.getCoverBell().getCoverNo());
        bizAlarm.setCoverBellId(param.getCoverBell().getId());
        bizAlarm.setAlarmNo(IdGen.getInfoCode("BA"));
        bizAlarm.setAlarmTime(new Date());
        bizAlarm.setAlarmType(param.getAlarmType());
		bizAlarm.setAddress(param.getCover().getAddressDetail());
        this.save(bizAlarm);
        return bizAlarm;
    }


    public List<BizAlarmStatisBo> statisByParam(BizAlarmParam param){
        return bizAlarmMapper.statisByParam(param);
    }


    public BizAlarm createBizAlarm(ExceptionReport exceptionReport) {
        BizAlarm bizAlarm = new BizAlarm();
        CoverWork coverWork = coverWorkService.get(exceptionReport.getCoverWorkId());
        Cover cover = null;
        CoverBell coverBell = null;
        if (coverWork != null) {
            cover =  coverService.get(coverWork.getCover().getId());
            coverBell = coverBellService.findUniqueByProperty("cover_id", coverWork.getCover().getId());
        }

        bizAlarm.setCoverId(cover.getId());
        bizAlarm.setCoverNo(cover.getNo());
        bizAlarm.setCoverBellId(coverBell.getId());
        bizAlarm.setAlarmNo(IdGen.getInfoCode("BA"));
        bizAlarm.setAlarmTime(new Date());
        bizAlarm.setAlarmType(BizAlarmConstant.BizAlarmType.MANUAL);
        bizAlarm.setAddress(exceptionReport.getAddress());
        //this.save(bizAlarm);
        return  bizAlarm;
    }





    public static void main(String[] args) {
        String bizAlarm = "81".substring(0, 1);
        System.out.println(bizAlarm);
    }


}
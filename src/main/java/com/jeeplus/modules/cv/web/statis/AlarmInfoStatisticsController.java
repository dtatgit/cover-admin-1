package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.modules.cb.service.bizAlarm.BizAlarmService;
import com.jeeplus.modules.cv.entity.statis.BizAlarmParam;
import com.jeeplus.modules.cv.entity.statis.BizAlarmStatisBo;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.utils.DictUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 报警信息统计Controller
 * @Author: ycp
 * @Date: 2020/10/14
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/statis/alarmInfoStatistics")
public class AlarmInfoStatisticsController {

    @Autowired
    private BizAlarmService bizAlarmService;


    @ModelAttribute
    public BizAlarmParam bizAlarmParamModel(@RequestParam(required=false) BizAlarmParam bizAlarmParam) {
        if (bizAlarmParam == null) {
            return new BizAlarmParam();
        }
        return bizAlarmParam;
    }

    @RequiresPermissions("cv:statis:alarmInfoStatistics:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/alarmInfoStatisticsList";
    }



    @RequiresPermissions("cv:statis:alarmInfoStatistics:list")
    @RequestMapping(value ="statisticData")
    @ResponseBody
    public AjaxJson statisticData(BizAlarmParam bizAlarmParam) {
        AjaxJson j = new AjaxJson();
        Long no = 1L;
        List<Map<String, Object>> datas = new ArrayList<>();
        //所有业务报警类型
        List<DictValue> dictList = DictUtils.getDictList("biz_alarm_type");
        if (CollectionUtils.isNotEmpty(dictList)) {
            for (DictValue dict : dictList) {
                Map<String, Object> data = new HashMap<>();
                String alarmDesc = dict.getLabel();
                String alarmType = dict.getValue();
                //封装数据
                data.put("id", String.valueOf(no));
                data.put("alarmType", alarmDesc);

                bizAlarmParam.setAlarmType(alarmType);
                List<BizAlarmStatisBo> statisBos = bizAlarmService.statisByParam(bizAlarmParam);
                processData(data, statisBos);
                datas.add(data);
                no++;
            }
        }
        j.setSuccess(true);
        j.setData(datas);
        return j;
    }


    private void processData(Map<String, Object> data, List<BizAlarmStatisBo> statisBos) {
        data.put("untreated","0");
        data.put("processed","0");
        data.put("processing","0");
        if (CollectionUtils.isNotEmpty(statisBos)) {
            for (BizAlarmStatisBo bo : statisBos) {
                if ("init".equals(bo.getLifeCycle())) {
                    data.put("untreated",bo.getCount());
                } else if ("complete".equals(bo.getLifeCycle())) {
                    data.put("processed",bo.getCount());
                } else if ("processing".equals(bo.getLifeCycle())) {
                    data.put("processing",bo.getCount());
                }
            }
        }
    }


}

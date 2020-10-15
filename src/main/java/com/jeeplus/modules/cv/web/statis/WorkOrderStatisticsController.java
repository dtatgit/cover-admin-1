package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.modules.cb.service.bizAlarm.BizAlarmService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.entity.statis.BizAlarmParam;
import com.jeeplus.modules.cv.entity.statis.BizAlarmStatisBo;
import com.jeeplus.modules.cv.entity.statis.CoverWorkParam;
import com.jeeplus.modules.cv.entity.statis.CoverWorkStatisBo;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 工单信息统计Controller
 * @Author: ycp
 * @Date: 2020/10/14
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/statis/workOrderStatistics")
public class WorkOrderStatisticsController {
    @Autowired
    private CoverWorkService coverWorkService;


    @RequiresPermissions("cv:statis:workOrderStatistics:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/workOrderStatisticsList";
    }


    @RequiresPermissions("cv:statis:workOrderStatistics:list")
    @RequestMapping(value ="Data")
    @ResponseBody
    public AjaxJson statisticData(CoverWorkParam param) {
        AjaxJson j = new AjaxJson();
        Long no = 1L;
        List<Map<String, Object>> datas = new ArrayList<>();
        //查询所有本单位及子单位
        List<Office> officeList = UserUtils.getOfficeList();
        param.setOfficeList(officeList);

        //所有业务报警类型
        List<DictValue> dictList = DictUtils.getDictList("work_level");
        if (CollectionUtils.isNotEmpty(dictList)) {
            for (DictValue dict : dictList) {
                Map<String, Object> data = new HashMap<>();

                String workTypeDesc = dict.getLabel();
                String workType = dict.getValue();
                //封装数据
                data.put("id", String.valueOf(no));
                data.put("workOrderType", workTypeDesc);

                param.setWorkType(workType);
                List<CoverWorkStatisBo> statisBos = coverWorkService.coverWorkStatistic(param);
                processData(data, statisBos);
                datas.add(data);
                no++;
            }
        }
        j.setSuccess(true);
        j.setData(datas);
        return j;
    }



    private void processData(Map<String, Object> data, List<CoverWorkStatisBo> statisBos) {
        data.put("normal", 0);
        data.put("urgent", 0);
        data.put("extra", 0);
        if (CollectionUtils.isNotEmpty(statisBos)) {
            for (CoverWorkStatisBo bo : statisBos) {
                if ("normal".equals(bo.getWorkLevel())) {
                    data.put("normal",bo.getCount());
                } else if ("urgent".equals(bo.getWorkLevel())) {
                    data.put("urgent",bo.getCount());
                } else if ("extra".equals(bo.getWorkLevel())) {
                    data.put("extra",bo.getCount());
                }
            }
        }
    }


}

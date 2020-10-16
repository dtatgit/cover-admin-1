package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
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
 * @Description: 工单信息统计Controller
 * @Author: ycp
 * @Date: 2020/10/14
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/statis/workOrderStatistics")
public class WorkOrderStatisticsController extends BaseController {
    @Autowired
    private CoverWorkService coverWorkService;

    @ModelAttribute
    public CoverWorkParam coverWorkModel(@RequestParam(required=false) CoverWorkParam coverWorkParam) {
        if (coverWorkParam == null) {
            return new CoverWorkParam();
        }
        return coverWorkParam;
    }


    @RequiresPermissions("cv:statis:workOrderStatistics:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/workOrderStatisticsList";
    }


    @RequiresPermissions("cv:statis:workOrderStatistics:list")
    @RequestMapping(value ="tableData")
    @ResponseBody
    public AjaxJson tableData(CoverWorkParam param, Model model) {
        AjaxJson j = new AjaxJson();
        Long no = 1L;
        List<Map<String, Object>> datas = new ArrayList<>();
        //查询所有本单位及子单位
        List<Office> officeList = UserUtils.getOfficeList();
        param.setOfficeList(officeList);

        //所有工单类型
        List<DictValue> dictList = DictUtils.getDictList("work_type");
        if (CollectionUtils.isNotEmpty(dictList)) {
            for (DictValue dict : dictList) {
                Map<String, Object> data = new HashMap<>();

                String workTypeDesc = dict.getLabel();
                String workType = dict.getValue();
                //封装数据
                data.put("id", String.valueOf(no));
                data.put("workOrderType", workTypeDesc);

                param.setWorkType(workType);
                List<CoverWorkStatisBo> statisBos = coverWorkService.coverWorkTableStatistic(param);
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

    @RequiresPermissions("cv:statis:workOrderStatistics:list")
    @RequestMapping(value ="lineData")
    @ResponseBody
    public AjaxJson lineData(CoverWorkParam param, Model model) {
        AjaxJson j = new AjaxJson();
        Map<String, Object> result = new HashMap<>();
        List<String> workLevels = new ArrayList<>();
        //查询所有本单位及子单位
        List<Office> officeList = UserUtils.getOfficeList();
        param.setOfficeList(officeList);
        //工单所有类别
        List<DictValue> workTypeList = DictUtils.getDictList("work_type");

        //所有工单紧急程度
        List<DictValue> workLevelList = DictUtils.getDictList("work_level");
        if (CollectionUtils.isNotEmpty(workLevelList)) {
            for (DictValue dict : workLevelList) {
                String workLevelDesc = dict.getLabel();
                String workLevel = dict.getValue();
                //工单紧急程度列表
                workLevels.add(workLevelDesc);

                //查询当前紧急程度工单数据
                param.setWorkLevel(workLevel);
                List<CoverWorkStatisBo> statisBos = coverWorkService.coverWorkLineStatistic(param);
                List<Long> statisDatas = processLineData(workTypeList, statisBos);
                result.put(workLevel, statisDatas);
            }
        }
        result.put("workTypes", processWorkType(workTypeList));
        result.put("workLevels", processWorkLevel(workLevelList));
        j.setSuccess(true);
        j.setData(result);
        return j;
    }

    private List<String> processWorkType(List<DictValue> workTypeList) {
        List<String> workTypes = null;
        if (CollectionUtils.isNotEmpty(workTypeList)) {
            workTypes = new ArrayList<>();
            for (DictValue dict : workTypeList) {
                workTypes.add(dict.getLabel());
            }
        }
        return workTypes;
    }
    private List<String> processWorkLevel(List<DictValue> workLevelList) {
        List<String> workLevels = null;
        if (CollectionUtils.isNotEmpty(workLevelList)) {
            workLevels = new ArrayList<>();
            for (DictValue dict : workLevelList) {
                workLevels.add(dict.getLabel());
            }
        }
        return workLevels;
    }


    private List<Long> processLineData(List<DictValue> workTypeList, List<CoverWorkStatisBo> statisBos){
        List<Long> dataList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workTypeList)) {
            for (DictValue dict : workTypeList) {
                String workType = dict.getValue();
                Long count = getCountByWorkType(workType, statisBos);
                dataList.add(count);
            }
        }
        return dataList;
    }

    private Long getCountByWorkType(String workType, List<CoverWorkStatisBo> statisBos) {
        Long count = 0L;
        if (CollectionUtils.isNotEmpty(statisBos)) {
            for (CoverWorkStatisBo bo : statisBos) {
                if (workType.equals(bo.getWorkType())) {
                    count = bo.getCount();
                    break;
                }
            }
        }
        return count;
    }


}

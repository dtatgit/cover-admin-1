package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.entity.statis.ConstructionStatistics;
import com.jeeplus.modules.cv.entity.statis.CoverWorkParam;
import com.jeeplus.modules.cv.entity.statis.CoverWorkStatisBo;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
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
 * @Description: 施工信息统计Controller
 * @Author: ycp
 * @Date: 2020/10/14
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/statis/constructionStatistics")
public class ConstructionStatisticsController extends BaseController {


    @Autowired
    private OfficeService officeService;
    @Autowired
    private CoverWorkService coverWorkService;


    @ModelAttribute
    public ConstructionStatistics get(@RequestParam(required=false) ConstructionStatistics constructionStatistics) {
        if (constructionStatistics == null) {
            return new ConstructionStatistics();
        }
        return constructionStatistics;
    }

    @RequiresPermissions("cv:statis:constructionStatistics:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/constructionStatisticsList";
    }


    @RequiresPermissions("cv:statis:constructionStatistics:list")
    @RequestMapping(value ="tableData")
    @ResponseBody
    public AjaxJson tableData(ConstructionStatistics param, Model model) {
        AjaxJson j = new AjaxJson();
        Long no = 1L;
        List<Map<String, Object>> datas = new ArrayList<>();
        //查询所有本单位及子单位
        //List<Office> officeList = UserUtils.getOfficeList();
        String officeId = null;
        if (param.getOffice() != null && StringUtils.isNotBlank(param.getOffice().getId())) {
            officeId = param.getOffice().getId();
        } else {
            officeId = UserUtils.getUser().getOffice().getId();
        }
        List<Office> officeList = officeService.getAllChildren(officeId);
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
                List<CoverWorkStatisBo> statisBos = coverWorkService.constructionStatis(param);
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

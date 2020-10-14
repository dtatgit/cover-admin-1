package com.jeeplus.modules.cv.web.statis;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 施工信息统计Controller
 * @Author: ycp
 * @Date: 2020/10/14
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/statis/constructionStatistics")
public class ConstructionStatisticsController {
    @RequiresPermissions("cv:statis:constructionStatistics:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/constructionStatisticsList";
    }
}

package com.jeeplus.modules.cv.web.statis;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 报警信息统计Controller
 * @Author: ycp
 * @Date: 2020/10/14
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/statis/alarmInfoStatistics")
public class AlarmInfoStatisticsController {
    @RequiresPermissions("cv:statis:alarmInfoStatistics:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/alarmInfoStatisticsList";
    }
}

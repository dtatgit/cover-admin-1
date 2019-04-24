package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.cv.entity.statis.CoverCollectStatis;
import com.jeeplus.modules.cv.service.statis.CoverCollectStatisService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/cv/statis/coverSummary")
public class CoverSummaryController extends BaseController {
    @Autowired
    private CoverCollectStatisService coverCollectStatisService;
    /**
     * 窨井盖汇总列表页面
     */
    @RequiresPermissions("cv:statis:coverSummary:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/coverSummaryList";
    }

    /**
     * 窨井盖采集统计列表数据
     */
    @ResponseBody
    @RequiresPermissions("cv:statis:coverSummary:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(CoverCollectStatis coverCollectStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<CoverCollectStatis> page = coverCollectStatisService.findSummaryPage(new Page<CoverCollectStatis>(request, response), coverCollectStatis);
        return getBootstrapData(page);
    }
}

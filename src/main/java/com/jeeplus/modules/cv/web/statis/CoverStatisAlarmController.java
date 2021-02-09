package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.cv.entity.statis.CoverStatis;
import com.jeeplus.modules.cv.service.statis.CoverStatisService;
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
@RequestMapping(value = "${adminPath}/cv/statis/coverStatis/alarm")
public class CoverStatisAlarmController extends BaseController {
    @Autowired
    private CoverStatisService coverStatisService;
    /**
     * 井盖相关统计列表页面
     */
    @RequiresPermissions("cv:statis:coverStatis:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/coverStatisAlarmList";
    }

    /**
     * 井盖相关统计列表数据
     */
    @ResponseBody
    @RequiresPermissions("cv:statis:coverStatis:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(CoverStatis coverStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<CoverStatis> page = coverStatisService.findPage(new Page<CoverStatis>(request, response), coverStatis);
        return getBootstrapData(page);
    }
}

package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.cv.entity.statis.OfficeOwnerStatis;
import com.jeeplus.modules.cv.service.statis.OfficeOwnerStatisService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/cv/statis/workRanking")
public class WorkRankingStatisController  extends BaseController {

    @Autowired
    private OfficeOwnerStatisService officeOwnerStatisService;

    @ModelAttribute
    public OfficeOwnerStatis get(@RequestParam(required=false) String id) {
        OfficeOwnerStatis entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = officeOwnerStatisService.get(id);
        }
        if (entity == null){
            entity = new OfficeOwnerStatis();
        }
        return entity;
    }

    /**
     * 维护部门权属单位统计列表页面
     */
    @RequiresPermissions("cv:statis:workRanking:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/workRankingStatisList";
    }


    /**
     * 工单排名列表数据
     */
    @ResponseBody
    @RequiresPermissions("cv:statis:workRanking:list")
    @RequestMapping(value = "dataOffice")
    public Map<String, Object> dataOffice(OfficeOwnerStatis officeOwnerStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OfficeOwnerStatis> page = officeOwnerStatisService.findPageByOffice(new Page<OfficeOwnerStatis>(request, response), officeOwnerStatis);
        return getBootstrapData(page);
    }

    /**
     * 工单排名列表数据
     */
    @ResponseBody
    @RequiresPermissions("cv:statis:workRanking:list")
    @RequestMapping(value = "dataOwnerDepart")
    public Map<String, Object> dataOwnerDepart(OfficeOwnerStatis officeOwnerStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OfficeOwnerStatis> page = officeOwnerStatisService.findPageByOwnerDepart(new Page<OfficeOwnerStatis>(request, response), officeOwnerStatis);
        return getBootstrapData(page);
    }

}

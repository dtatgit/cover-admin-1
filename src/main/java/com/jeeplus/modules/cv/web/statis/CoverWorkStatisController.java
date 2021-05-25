package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.cv.entity.statis.CoverCollectStatis;
import com.jeeplus.modules.cv.entity.statis.CoverWorkStatisVo;
import com.jeeplus.modules.cv.service.statis.CoverStatisService;
import com.jeeplus.modules.cv.service.statis.CoverWorkStatisService;
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
@RequestMapping(value = "${adminPath}/cv/statis/coverWorkStatis")
public class CoverWorkStatisController extends BaseController {

    @Autowired
    private CoverWorkStatisService CoverWorkStatisService;


    @ModelAttribute
    public CoverWorkStatisVo get(@RequestParam(required=false) String id) {
        CoverWorkStatisVo entity = null;
        if (StringUtils.isNotBlank(id)){
            entity = CoverWorkStatisService.get(id);
        }
        if (entity == null){
            entity = new CoverWorkStatisVo();
        }
        return entity;
    }


    /**
     *
     */
    @RequiresPermissions("cv:statis:coverWorkStatis:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/coverWorkStatisList";
    }


    /**
     * 窨井盖采集统计列表数据
     */
    @ResponseBody
    @RequiresPermissions("cv:statis:coverWorkStatis:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(CoverWorkStatisVo coverWorkStatisVo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<CoverWorkStatisVo> page = CoverWorkStatisService.findPage(new Page<CoverWorkStatisVo>(request, response), coverWorkStatisVo);
        return getBootstrapData(page);
    }






}

package com.jeeplus.modules.cb.web.equinfo;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 井卫设备信息Controller
 * @author crj
 * @version 2019-11-12
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/equinfo/coverBellExt")
public class CoverBellExtController extends CoverBellController{

    /**
     * 井卫设备信息列表页面
     */
    @RequiresPermissions("cb:equinfo:coverBellExt:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cb/equinfo/coverBellExtList";
    }

    /**
     * 井卫设备信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("cb:equinfo:coverBellExt:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(CoverBell coverBell, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<CoverBell> page = coverBellService.findPage(new Page<CoverBell>(request, response), coverBell);
        //渲染井盖数据进行前端展示
        List<CoverBell> bellList=  page.getList();
        if(null!=bellList&&bellList.size()>0){
            for(CoverBell bell:bellList){
                if(StringUtils.isNotEmpty(bell.getCoverId())){
                    bell.setCover(coverService.get(bell.getCoverId()));
                }
            }
        }
        return getBootstrapData(page);
    }

}

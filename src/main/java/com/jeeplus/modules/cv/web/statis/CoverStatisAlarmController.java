package com.jeeplus.modules.cv.web.statis;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.cv.entity.statis.ConstructionStatistics;
import com.jeeplus.modules.cv.entity.statis.CoverStatis;
import com.jeeplus.modules.cv.entity.statis.CoverWorkStatisBo;
import com.jeeplus.modules.cv.service.statis.CoverStatisService;
import com.jeeplus.modules.cv.vo.CoverStatisVO;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/cv/statis/coverStatis/alarm")
public class CoverStatisAlarmController extends BaseController {
    @Autowired
    private CoverStatisService coverStatisService;

    @ModelAttribute
    public CoverStatis get(@RequestParam(required=false) CoverStatis coverStatis) {
        if (coverStatis == null) {
            return new CoverStatis();
        }
        return coverStatis;
    }

    /**
     * 井盖相关统计列表页面
     */
    @RequiresPermissions("cv:statis:coverStatis:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/cv/statis/coverStatisAlarmList";
    }

    @RequiresPermissions("cv:statis:coverStatis:list")
    @RequestMapping(value ="tableData")
    @ResponseBody
    public AjaxJson tableData(CoverStatis coverStatis, Model model) {
        AjaxJson j = new AjaxJson();
        List<Map<String, Object>> datas = new ArrayList<>();
        List<CoverStatisVO> dataList = coverStatisService.queryStatisData(coverStatis);

        if (CollectionUtils.isNotEmpty(dataList)) {
            for (CoverStatisVO vo : dataList) {
                Map<String, Object> data = new HashMap<>();
                //封装数据
                data.put("district", vo.getDistrict());//区域
                data.put("coverNum", vo.getCoverNum());// 井盖数
                data.put("installEqu", vo.getInstallEqu());// 已安装设备数
                data.put("onlineNum", vo.getOnlineNum());// 当前在线数
                data.put("offlineNum", vo.getOfflineNum());// 当前离线数
                data.put("coverAlarmNum", vo.getCoverAlarmNum());// 报警井盖数
                data.put("alarmTotalNum", vo.getAlarmTotalNum());// 报警总数
                data.put("addWorkNum", vo.getAddWorkNum());// 工单总数（当天新增）
                data.put("completeWorkNum", vo.getCompleteWorkNum());// 已完成工单总数（当天）
                data.put("proWorkNum", vo.getProWorkNum());// 未完成工单总数（累计）
                data.put("statisTime", vo.getStatisTime());// 统计时间
                data.put("workNumTotal", vo.getWorkNumTotal());// 工单总数（累计总共）
                data.put("completeWorkNumTotal", vo.getCompleteWorkNumTotal());/// 已完成工单总数（累计总共）
                datas.add(data);
            }
        }else{
            Map<String, Object> data = new HashMap<>();
            datas.add(data);
        }
        j.setSuccess(true);
        j.setData(datas);
        return j;
    }

}

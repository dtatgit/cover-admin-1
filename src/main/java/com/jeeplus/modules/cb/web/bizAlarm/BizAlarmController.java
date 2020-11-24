/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.bizAlarm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.cb.constant.bizAlarm.BizAlarmConstant;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.cb.entity.bizAlarm.BizAlarm;
import com.jeeplus.modules.cb.service.bizAlarm.BizAlarmService;

/**
 * 业务报警Controller
 *
 * @author Peter
 * @version 2020-10-13
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/alarm/bizAlarm/")
public class BizAlarmController extends BaseController {

    @Autowired
    private BizAlarmService bizAlarmService;

    @Autowired
    private CoverService coverService;

    @Autowired
    private CoverBellService coverBellService;

    @Autowired
    private CoverWorkService coverWorkService;

    @ModelAttribute
    public BizAlarm get(@RequestParam(required = false) String id) {
        BizAlarm entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bizAlarmService.get(id);
        }
        if (entity == null) {
            entity = new BizAlarm();
        }
        return entity;
    }

    /**
     * 业务报警列表页面
     */
    /*@RequiresPermissions("alarm:bizAlarm:list")*/
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bizAlarm/bizAlarmList";
    }

    /**
     * 业务报警列表数据
     */
    @ResponseBody
    /*@RequiresPermissions("alarm:bizAlarm:list")*/
    @RequestMapping(value = "data")
    public Map<String, Object> data(BizAlarm bizAlarm, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BizAlarm> page = bizAlarmService.findPage(new Page<BizAlarm>(request, response), bizAlarm);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑业务报警表单页面
     */
    @RequiresPermissions(value = {"alarm:bizAlarm:view", "alarm:bizAlarm:add", "alarm:bizAlarm:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BizAlarm bizAlarm, Model model) {
        model.addAttribute("bizAlarm", bizAlarm);
        if (StringUtils.isBlank(bizAlarm.getId())) {//如果ID是空为添加
            model.addAttribute("isAdd", true);
        }
        return "modules/bizAlarm/bizAlarmForm";
    }

    /**
     * 查看业务报警表单页面
     */
    @RequiresPermissions(value = {"alarm:bizAlarm:view"}, logical = Logical.OR)
    @RequestMapping(value = "view")
    public String view(BizAlarm bizAlarm, Model model) {
        model.addAttribute("bizAlarm", bizAlarm);
        Cover cover = null;
        CoverBell coverBell = null;
        if (bizAlarm != null) {
            cover = coverService.get(bizAlarm.getCoverId());
            coverBell = coverBellService.get(bizAlarm.getCoverBellId());
        }
        model.addAttribute("cover", cover);
        model.addAttribute("coverBell", coverBell);
        return "modules/bizAlarm/bizAlarmView";
    }


    /**
     * 保存业务报警
     */
    @RequiresPermissions(value = {"alarm:bizAlarm:add", "alarm:bizAlarm:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(BizAlarm bizAlarm, Model model, RedirectAttributes redirectAttributes) throws Exception {
        if (!beanValidator(model, bizAlarm)) {
            return form(bizAlarm, model);
        }
        //新增或编辑表单保存
        bizAlarmService.save(bizAlarm);//保存
        addMessage(redirectAttributes, "保存业务报警成功");
        return "redirect:" + Global.getAdminPath() + "/cb/alarm/bizAlarm/?repage";
    }

    /**
     * 删除业务报警
     */
    @ResponseBody
    @RequiresPermissions("alarm:bizAlarm:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(BizAlarm bizAlarm, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        bizAlarmService.delete(bizAlarm);
        j.setMsg("删除业务报警成功");
        return j;
    }

    /**
     * 批量删除业务报警
     */
    @ResponseBody
    @RequiresPermissions("alarm:bizAlarm:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            bizAlarmService.delete(bizAlarmService.get(id));
        }
        j.setMsg("删除业务报警成功");
        return j;
    }

    /**
     * 批量生成业务报警的工单
     */
    @ResponseBody
    @RequiresPermissions("alarm:bizAlarm:createWorks")
    @RequestMapping(value = "createWorks")
    public AjaxJson createWorks(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        String errorMsg = "";

        for (String id : idArray) {
            BizAlarm bizAlarm = bizAlarmService.get(id);
            //不生成新工单
            if (!isCreateBizAlarmBill(bizAlarm)) {
				errorMsg = errorMsg + bizAlarm.getAlarmNo() + "警报已生成过工单.";
				continue;
			}
            try {
                coverWorkService.createBizAlarmWork(bizAlarm);
            } catch (Exception e) {
                logger.error("批量创建业务报警工单异常, 警报编号：" + bizAlarm.getAlarmNo() + "异常信息：" + e.getMessage());
                errorMsg = errorMsg + bizAlarm.getAlarmNo() + "警报生成业务警报工单异常.";
                continue;
            }
        }
        j.setMsg("处理业务报警工单完成 " + errorMsg);
        return j;
    }


	/**
	 * 校验是否生成新业务报警工单
	 * @param bizAlarm
	 * @return
	 */
	public boolean isCreateBizAlarmBill(BizAlarm bizAlarm) {
    	//未关联工单
		if (StringUtils.isBlank(bizAlarm.getCoverWorkId())) {
			return true;
		//已关联工单
		} else {
			CoverWork coverWork = coverWorkService.get(bizAlarm.getCoverWorkId());
			if (coverWork != null) {
				//工单已处理完,报警状态是未处理
				if (CodeConstant.lifecycle.complete.equals(coverWork.getLifeCycle())
						&& BizAlarmConstant.BizAlarmDealStatus.NOT_DEAL.equals(bizAlarm.getDealStatus())) {
					return true;
				}
			}
		}
        return false;
    }


    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("alarm:bizAlarm:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BizAlarm bizAlarm, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "业务报警" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BizAlarm> page = bizAlarmService.findPage(new Page<BizAlarm>(request, response, -1), bizAlarm);
            new ExportExcel("业务报警", BizAlarm.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出业务报警记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("alarm:bizAlarm:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BizAlarm> list = ei.getDataList(BizAlarm.class);
            for (BizAlarm bizAlarm : list) {
                try {
                    bizAlarmService.save(bizAlarm);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条业务报警记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条业务报警记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入业务报警失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bizalarm/bizAlarm/?repage";
    }

    /**
     * 下载导入业务报警数据模板
     */
    @RequiresPermissions("alarm:bizAlarm:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "业务报警数据导入模板.xlsx";
            List<BizAlarm> list = Lists.newArrayList();
            new ExportExcel("业务报警数据", BizAlarm.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/bizalarm/bizAlarm/?repage";
    }

}
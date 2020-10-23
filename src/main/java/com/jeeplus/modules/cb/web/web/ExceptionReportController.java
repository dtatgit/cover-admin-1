/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.web;

import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.cb.entity.bizAlarm.BizAlarm;
import com.jeeplus.modules.cb.entity.exceptionReport.ExceptionReport;
import com.jeeplus.modules.cb.service.bizAlarm.BizAlarmService;
import com.jeeplus.modules.cb.service.service.ExceptionReportService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.sys.utils.UserUtils;
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

/**
 * 异常上报Controller
 * @author Peter
 * @version 2020-10-19
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/report/exceptionReport")
public class ExceptionReportController extends BaseController {

	@Autowired
	private ExceptionReportService exceptionReportService;

	@Autowired
	private BizAlarmService bizAlarmService;

	@Autowired
	private CoverWorkService coverWorkService;
	
	@ModelAttribute
	public ExceptionReport get(@RequestParam(required=false) String id) {
		ExceptionReport entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = exceptionReportService.get(id);
		}
		if (entity == null){
			entity = new ExceptionReport();
		}
		return entity;
	}
	
	/**
	 * 异常上报列表页面
	 */
	@RequiresPermissions("report:exceptionReport:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/exceptionReport/exceptionReportList";
	}
	
		/**
	 * 异常上报列表数据
	 */
	@ResponseBody
	@RequiresPermissions("report:exceptionReport:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ExceptionReport exceptionReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ExceptionReport> page = exceptionReportService.findPage(new Page<ExceptionReport>(request, response), exceptionReport); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑异常上报表单页面
	 */
	@RequiresPermissions(value={"report:exceptionReport:view","report:exceptionReport:add","report:exceptionReport:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ExceptionReport exceptionReport, Model model) {
		model.addAttribute("exceptionReport", exceptionReport);
		if(StringUtils.isBlank(exceptionReport.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/exceptionReport/exceptionReportForm";
	}

	/**
	 * 保存异常上报
	 */
	@RequiresPermissions(value={"report:exceptionReport:add","report:exceptionReport:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ExceptionReport exceptionReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, exceptionReport)){
			return form(exceptionReport, model);
		}
		if (exceptionReport != null) {
			exceptionReport.setCreateDate(new Date());
			exceptionReport.setCreateBy(UserUtils.getUser());
		}
		//新增或编辑表单保存
		exceptionReportService.save(exceptionReport);//保存
		addMessage(redirectAttributes, "保存异常上报成功");
		return "redirect:"+Global.getAdminPath()+"/cb/report/exceptionReport/?repage";
	}

	/**
	 * 查看，增加，编辑异常上报表单页面
	 */
	@RequiresPermissions(value={"report:exceptionReport:check"},logical=Logical.OR)
	@RequestMapping(value = "check")
	public String check(ExceptionReport exceptionReport, Model model) {
		if (StringUtils.isNotBlank(exceptionReport.getId())) {
			exceptionReport = exceptionReportService.get(exceptionReport.getId());
		}
		model.addAttribute("exceptionReport", exceptionReport);
		if(StringUtils.isBlank(exceptionReport.getId())){//如果ID是空为添加
			model.addAttribute("isCheck", true);
		}
		return "modules/exceptionReport/exceptionReportCheck";
	}


	/**
	 * 保存异常上报
	 */
	@RequiresPermissions(value={"report:exceptionReport:add","report:exceptionReport:edit"},logical=Logical.OR)
	@RequestMapping(value = "checkReport")
	public String checkReport(ExceptionReport exceptionReport, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, exceptionReport)){
			return form(exceptionReport, model);
		}
		if (exceptionReport != null) {
			exceptionReport.setCheckDate(new Date());
			exceptionReport.setCheckBy(UserUtils.getUser().getId());
		}
		//新增或编辑表单保存
		exceptionReportService.save(exceptionReport);//保存
		//审核通过
		if ("1".equals(exceptionReport.getCheckStatus())) {
			//业务报警参数
			BizAlarm bizAlarm = bizAlarmService.createBizAlarm(exceptionReport);
			//创建业务报警工单
			coverWorkService.createBizAlarmWork(bizAlarm);
		}
		addMessage(redirectAttributes, "保存异常上报成功");
		return "redirect:"+Global.getAdminPath()+"/cb/report/exceptionReport/?repage";
	}

	/**
	 * 删除异常上报
	 */
	@ResponseBody
	@RequiresPermissions("report:exceptionReport:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ExceptionReport exceptionReport, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		exceptionReportService.delete(exceptionReport);
		j.setMsg("删除异常上报成功");
		return j;
	}

	/**
	 * 批量删除异常上报
	 */
	@ResponseBody
	@RequiresPermissions("report:exceptionReport:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			exceptionReportService.delete(exceptionReportService.get(id));
		}
		j.setMsg("删除异常上报成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("report:exceptionReport:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ExceptionReport exceptionReport, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "异常上报"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ExceptionReport> page = exceptionReportService.findPage(new Page<ExceptionReport>(request, response, -1), exceptionReport);
    		new ExportExcel("异常上报", ExceptionReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出异常上报记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("report:exceptionReport:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ExceptionReport> list = ei.getDataList(ExceptionReport.class);
			for (ExceptionReport exceptionReport : list){
				try{
					exceptionReportService.save(exceptionReport);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条异常上报记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条异常上报记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入异常上报失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exceptionreport/exceptionReport/?repage";
    }
	
	/**
	 * 下载导入异常上报数据模板
	 */
	@RequiresPermissions("report:exceptionReport:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "异常上报数据导入模板.xlsx";
    		List<ExceptionReport> list = Lists.newArrayList(); 
    		new ExportExcel("异常上报数据", ExceptionReport.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/exceptionreport/exceptionReport/?repage";
    }

}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.web.task;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.jeeplus.modules.cv.entity.task.CoverTaskProcess;
import com.jeeplus.modules.cv.service.task.CoverTaskProcessService;

/**
 * 任务处理明细Controller
 * @author crj
 * @version 2019-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/task/coverTaskProcess")
public class CoverTaskProcessController extends BaseController {

	@Autowired
	private CoverTaskProcessService coverTaskProcessService;
	
	@ModelAttribute
	public CoverTaskProcess get(@RequestParam(required=false) String id) {
		CoverTaskProcess entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverTaskProcessService.get(id);
		}
		if (entity == null){
			entity = new CoverTaskProcess();
		}
		return entity;
	}
	
	/**
	 * 任务处理明细列表页面
	 */
	@RequiresPermissions("cv:task:coverTaskProcess:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/task/coverTaskProcessList";
	}
	
		/**
	 * 任务处理明细列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTaskProcess:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverTaskProcess coverTaskProcess, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverTaskProcess> page = coverTaskProcessService.findPage(new Page<CoverTaskProcess>(request, response), coverTaskProcess); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑任务处理明细表单页面
	 */
	@RequiresPermissions(value={"cv:task:coverTaskProcess:view","cv:task:coverTaskProcess:add","cv:task:coverTaskProcess:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverTaskProcess coverTaskProcess, Model model) {
		model.addAttribute("coverTaskProcess", coverTaskProcess);
		return "modules/cv/task/coverTaskProcessForm";
	}

	/**
	 * 保存任务处理明细
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:task:coverTaskProcess:add","cv:task:coverTaskProcess:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverTaskProcess coverTaskProcess, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverTaskProcess)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverTaskProcessService.save(coverTaskProcess);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存任务处理明细成功");
		return j;
	}
	
	/**
	 * 删除任务处理明细
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTaskProcess:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverTaskProcess coverTaskProcess, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverTaskProcessService.delete(coverTaskProcess);
		j.setMsg("删除任务处理明细成功");
		return j;
	}
	
	/**
	 * 批量删除任务处理明细
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTaskProcess:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverTaskProcessService.delete(coverTaskProcessService.get(id));
		}
		j.setMsg("删除任务处理明细成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTaskProcess:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverTaskProcess coverTaskProcess, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "任务处理明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverTaskProcess> page = coverTaskProcessService.findPage(new Page<CoverTaskProcess>(request, response, -1), coverTaskProcess);
    		new ExportExcel("任务处理明细", CoverTaskProcess.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出任务处理明细记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:task:coverTaskProcess:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverTaskProcess> list = ei.getDataList(CoverTaskProcess.class);
			for (CoverTaskProcess coverTaskProcess : list){
				try{
					coverTaskProcessService.save(coverTaskProcess);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条任务处理明细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条任务处理明细记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入任务处理明细失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/task/coverTaskProcess/?repage";
    }
	
	/**
	 * 下载导入任务处理明细数据模板
	 */
	@RequiresPermissions("cv:task:coverTaskProcess:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "任务处理明细数据导入模板.xlsx";
    		List<CoverTaskProcess> list = Lists.newArrayList(); 
    		new ExportExcel("任务处理明细数据", CoverTaskProcess.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/task/coverTaskProcess/?repage";
    }

}
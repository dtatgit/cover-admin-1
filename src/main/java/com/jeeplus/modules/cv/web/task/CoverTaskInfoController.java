/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.web.task;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.cv.constant.CodeConstant;
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
import com.jeeplus.modules.cv.entity.task.CoverTaskInfo;
import com.jeeplus.modules.cv.service.task.CoverTaskInfoService;

/**
 * 井盖数据处理任务Controller
 * @author crj
 * @version 2019-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/task/coverTaskInfo")
public class CoverTaskInfoController extends BaseController {

	@Autowired
	private CoverTaskInfoService coverTaskInfoService;
	
	@ModelAttribute
	public CoverTaskInfo get(@RequestParam(required=false) String id) {
		CoverTaskInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverTaskInfoService.get(id);
		}
		if (entity == null){
			entity = new CoverTaskInfo();
		}
		return entity;
	}
	
	/**
	 * 井盖数据处理任务列表页面
	 */
	@RequiresPermissions("cv:task:coverTaskInfo:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/task/coverTaskInfoList";
	}
	
		/**
	 * 井盖数据处理任务列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTaskInfo:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverTaskInfo coverTaskInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverTaskInfo> page = coverTaskInfoService.findPage(new Page<CoverTaskInfo>(request, response), coverTaskInfo); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖数据处理任务表单页面
	 */
	@RequiresPermissions(value={"cv:task:coverTaskInfo:view","cv:task:coverTaskInfo:add","cv:task:coverTaskInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverTaskInfo coverTaskInfo, Model model) {
		String id=coverTaskInfo.getId();
		if(StringUtils.isEmpty(id)){
			coverTaskInfo.setTaskNo(IdGen.getInfoCode("TASK"));
			coverTaskInfo.setTaskStatus(CodeConstant.TASK_STATUS.ASSIGN);
		}

		model.addAttribute("coverTaskInfo", coverTaskInfo);
		return "modules/cv/task/coverTaskInfoForm";
	}

	/**
	 * 保存井盖数据处理任务
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:task:coverTaskInfo:add","cv:task:coverTaskInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverTaskInfo coverTaskInfo, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverTaskInfo)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverTaskInfoService.save(coverTaskInfo);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井盖数据处理任务成功");
		return j;
	}
	
	/**
	 * 删除井盖数据处理任务
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTaskInfo:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverTaskInfo coverTaskInfo, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverTaskInfoService.delete(coverTaskInfo);
		j.setMsg("删除井盖数据处理任务成功");
		return j;
	}
	
	/**
	 * 批量删除井盖数据处理任务
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTaskInfo:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverTaskInfoService.delete(coverTaskInfoService.get(id));
		}
		j.setMsg("删除井盖数据处理任务成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTaskInfo:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverTaskInfo coverTaskInfo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井盖数据处理任务"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverTaskInfo> page = coverTaskInfoService.findPage(new Page<CoverTaskInfo>(request, response, -1), coverTaskInfo);
    		new ExportExcel("井盖数据处理任务", CoverTaskInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井盖数据处理任务记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:task:coverTaskInfo:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverTaskInfo> list = ei.getDataList(CoverTaskInfo.class);
			for (CoverTaskInfo coverTaskInfo : list){
				try{
					coverTaskInfoService.save(coverTaskInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井盖数据处理任务记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井盖数据处理任务记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井盖数据处理任务失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/task/coverTaskInfo/?repage";
    }
	
	/**
	 * 下载导入井盖数据处理任务数据模板
	 */
	@RequiresPermissions("cv:task:coverTaskInfo:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井盖数据处理任务数据导入模板.xlsx";
    		List<CoverTaskInfo> list = Lists.newArrayList(); 
    		new ExportExcel("井盖数据处理任务数据", CoverTaskInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/task/coverTaskInfo/?repage";
    }

}
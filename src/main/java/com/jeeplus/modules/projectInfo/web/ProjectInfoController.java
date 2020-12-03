/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.projectInfo.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.projectInfo.service.ProjectInfoService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
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
import com.jeeplus.modules.projectInfo.entity.ProjectInfo;
/**
 * 项目管理Controller
 * @author Peter
 * @version 2020-12-02
 */
@Controller
@RequestMapping(value = "${adminPath}/project/projectInfo")
public class ProjectInfoController extends BaseController {

	@Autowired
	private ProjectInfoService projectInfoService;

	@Autowired
	private OfficeService officeService;


	
	@ModelAttribute
	public ProjectInfo get(@RequestParam(required=false) String id) {
		ProjectInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = projectInfoService.get(id);
		}
		if (entity == null){
			entity = new ProjectInfo();
		}
		return entity;
	}
	
	/**
	 * 项目管理列表页面
	 */
	@RequiresPermissions("project:projectInfo:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/projectInfo/projectInfoList";
	}
	
		/**
	 * 项目管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("project:projectInfo:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ProjectInfo projectInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProjectInfo> page = projectInfoService.findPage(new Page<ProjectInfo>(request, response), projectInfo); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑项目管理表单页面
	 */
	@RequiresPermissions(value={"project:projectInfo:view","project:projectInfo:add","project:projectInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ProjectInfo projectInfo, Model model) {
		model.addAttribute("projectInfo", projectInfo);
		if(StringUtils.isBlank(projectInfo.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/projectInfo/projectInfoForm";
	}

	/**
	 * 查看，增加，编辑项目管理表单页面
	 */
	@RequiresPermissions(value={"project:projectInfo:view"},logical=Logical.OR)
	@RequestMapping(value = "view")
	public String view(ProjectInfo projectInfo, Model model) {
		if (projectInfo != null) {
			projectInfo = projectInfoService.get(projectInfo.getId());
		}
		model.addAttribute("projectInfo", projectInfo);
		return "modules/projectInfo/projectInfoView";
	}

	/**
	 * 保存项目管理
	 */
	@RequiresPermissions(value={"project:projectInfo:add","project:projectInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	@ResponseBody
	public AjaxJson save(ProjectInfo projectInfo, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, projectInfo)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新建或编辑项目
		projectInfoService.saveProject(projectInfo);
		addMessage(redirectAttributes, "保存项目管理成功");
		j.setSuccess(true);
		j.setMsg("保存工单流程操作结果定义成功");
		return j;
	}
	
	/**
	 * 删除项目管理
	 */
	@ResponseBody
	@RequiresPermissions("project:projectInfo:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ProjectInfo projectInfo, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		projectInfoService.delete(projectInfo);
		j.setMsg("删除项目管理成功");
		return j;
	}
	
	/**
	 * 批量删除项目管理
	 */
	@ResponseBody
	@RequiresPermissions("project:projectInfo:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			projectInfoService.delete(projectInfoService.get(id));
		}
		j.setMsg("删除项目管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("project:projectInfo:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ProjectInfo projectInfo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "项目管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ProjectInfo> page = projectInfoService.findPage(new Page<ProjectInfo>(request, response, -1), projectInfo);
    		new ExportExcel("项目管理", ProjectInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出项目管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("project:projectInfo:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ProjectInfo> list = ei.getDataList(ProjectInfo.class);
			for (ProjectInfo projectInfo : list){
				try{
					projectInfoService.save(projectInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条项目管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条项目管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入项目管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/project/projectInfo/?repage";
    }
	
	/**
	 * 下载导入项目管理数据模板
	 */
	@RequiresPermissions("project:projectInfo:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "项目管理数据导入模板.xlsx";
    		List<ProjectInfo> list = Lists.newArrayList(); 
    		new ExportExcel("项目管理数据", ProjectInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/project/projectInfo/?repage";
    }

}
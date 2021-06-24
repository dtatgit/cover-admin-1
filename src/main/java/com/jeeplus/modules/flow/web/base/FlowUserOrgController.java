/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.web.base;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.projectInfo.entity.ProjectInfo;
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
import com.jeeplus.modules.flow.entity.base.FlowUserOrg;
import com.jeeplus.modules.flow.service.base.FlowUserOrgService;

/**
 * 用户组织关系配置Controller
 * @author crj
 * @version 2019-11-21
 */
@Controller
@RequestMapping(value = "${adminPath}/flow/base/flowUserOrg")
public class FlowUserOrgController extends BaseController {

	@Autowired
	private FlowUserOrgService flowUserOrgService;
	
	@ModelAttribute
	public FlowUserOrg get(@RequestParam(required=false) String id) {
		FlowUserOrg entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = flowUserOrgService.get(id);
		}
		if (entity == null){
			entity = new FlowUserOrg();
		}
		return entity;
	}
	
	/**
	 * 用户组织关系配置列表页面
	 */
	@RequiresPermissions("flow:base:flowUserOrg:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/flow/base/flowUserOrgList";
	}
	
		/**
	 * 用户组织关系配置列表数据
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowUserOrg:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(FlowUserOrg flowUserOrg, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FlowUserOrg> page = flowUserOrgService.findPage(new Page<FlowUserOrg>(request, response), flowUserOrg); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑用户组织关系配置表单页面
	 */
	@RequiresPermissions(value={"flow:base:flowUserOrg:view","flow:base:flowUserOrg:add","flow:base:flowUserOrg:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FlowUserOrg flowUserOrg, Model model) {
		model.addAttribute("flowUserOrg", flowUserOrg);
		return "modules/flow/base/flowUserOrgForm";
	}

	/**
	 * 保存用户组织关系配置
	 */
	@ResponseBody
	@RequiresPermissions(value={"flow:base:flowUserOrg:add","flow:base:flowUserOrg:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(FlowUserOrg flowUserOrg, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, flowUserOrg)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}

		ProjectInfo projectInfo=flowUserOrg.getProjectInfo();
		if(null!=projectInfo){
			flowUserOrg.setProjectId(projectInfo.getId());
			flowUserOrg.setProjectName(projectInfo.getProjectName());
		}
		flowUserOrgService.save(flowUserOrg);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存用户组织关系配置成功");
		return j;
	}
	
	/**
	 * 删除用户组织关系配置
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowUserOrg:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(FlowUserOrg flowUserOrg, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		flowUserOrgService.delete(flowUserOrg);
		j.setMsg("删除用户组织关系配置成功");
		return j;
	}
	
	/**
	 * 批量删除用户组织关系配置
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowUserOrg:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			flowUserOrgService.delete(flowUserOrgService.get(id));
		}
		j.setMsg("删除用户组织关系配置成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowUserOrg:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(FlowUserOrg flowUserOrg, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "用户组织关系配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FlowUserOrg> page = flowUserOrgService.findPage(new Page<FlowUserOrg>(request, response, -1), flowUserOrg);
    		new ExportExcel("用户组织关系配置", FlowUserOrg.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出用户组织关系配置记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("flow:base:flowUserOrg:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FlowUserOrg> list = ei.getDataList(FlowUserOrg.class);
			for (FlowUserOrg flowUserOrg : list){
				try{
					flowUserOrgService.save(flowUserOrg);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户组织关系配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户组织关系配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户组织关系配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/base/flowUserOrg/?repage";
    }
	
	/**
	 * 下载导入用户组织关系配置数据模板
	 */
	@RequiresPermissions("flow:base:flowUserOrg:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户组织关系配置数据导入模板.xlsx";
    		List<FlowUserOrg> list = Lists.newArrayList(); 
    		new ExportExcel("用户组织关系配置数据", FlowUserOrg.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/base/flowUserOrg/?repage";
    }

}
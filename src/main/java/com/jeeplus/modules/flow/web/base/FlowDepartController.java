/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.web.base;

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
import com.jeeplus.modules.flow.entity.base.FlowDepart;
import com.jeeplus.modules.flow.service.base.FlowDepartService;

/**
 * 部门流程配置Controller
 * @author crj
 * @version 2019-11-21
 */
@Controller
@RequestMapping(value = "${adminPath}/flow/base/flowDepart")
public class FlowDepartController extends BaseController {

	@Autowired
	private FlowDepartService flowDepartService;
	
	@ModelAttribute
	public FlowDepart get(@RequestParam(required=false) String id) {
		FlowDepart entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = flowDepartService.get(id);
		}
		if (entity == null){
			entity = new FlowDepart();
		}
		return entity;
	}
	
	/**
	 * 部门流程配置列表页面
	 */
	@RequiresPermissions("flow:base:flowDepart:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/flow/base/flowDepartList";
	}
	
		/**
	 * 部门流程配置列表数据
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowDepart:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(FlowDepart flowDepart, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FlowDepart> page = flowDepartService.findPage(new Page<FlowDepart>(request, response), flowDepart); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑部门流程配置表单页面
	 */
	@RequiresPermissions(value={"flow:base:flowDepart:view","flow:base:flowDepart:add","flow:base:flowDepart:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FlowDepart flowDepart, Model model) {
		model.addAttribute("flowDepart", flowDepart);
		return "modules/flow/base/flowDepartForm";
	}

	/**
	 * 保存部门流程配置
	 */
	@ResponseBody
	@RequiresPermissions(value={"flow:base:flowDepart:add","flow:base:flowDepart:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(FlowDepart flowDepart, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, flowDepart)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		flowDepartService.save(flowDepart);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存部门流程配置成功");
		return j;
	}
	
	/**
	 * 删除部门流程配置
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowDepart:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(FlowDepart flowDepart, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		flowDepartService.delete(flowDepart);
		j.setMsg("删除部门流程配置成功");
		return j;
	}
	
	/**
	 * 批量删除部门流程配置
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowDepart:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			flowDepartService.delete(flowDepartService.get(id));
		}
		j.setMsg("删除部门流程配置成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowDepart:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(FlowDepart flowDepart, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "部门流程配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FlowDepart> page = flowDepartService.findPage(new Page<FlowDepart>(request, response, -1), flowDepart);
    		new ExportExcel("部门流程配置", FlowDepart.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出部门流程配置记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("flow:base:flowDepart:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FlowDepart> list = ei.getDataList(FlowDepart.class);
			for (FlowDepart flowDepart : list){
				try{
					flowDepartService.save(flowDepart);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条部门流程配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条部门流程配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入部门流程配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/base/flowDepart/?repage";
    }
	
	/**
	 * 下载导入部门流程配置数据模板
	 */
	@RequiresPermissions("flow:base:flowDepart:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "部门流程配置数据导入模板.xlsx";
    		List<FlowDepart> list = Lists.newArrayList(); 
    		new ExportExcel("部门流程配置数据", FlowDepart.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/base/flowDepart/?repage";
    }

}
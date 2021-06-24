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
import com.jeeplus.modules.flow.entity.base.FlowState;
import com.jeeplus.modules.flow.service.base.FlowStateService;

/**
 * 工单流程状态Controller
 * @author crj
 * @version 2019-11-21
 */
@Controller
@RequestMapping(value = "${adminPath}/flow/base/flowState")
public class FlowStateController extends BaseController {

	@Autowired
	private FlowStateService flowStateService;
	
	@ModelAttribute
	public FlowState get(@RequestParam(required=false) String id) {
		FlowState entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = flowStateService.get(id);
		}
		if (entity == null){
			entity = new FlowState();
		}
		return entity;
	}
	
	/**
	 * 工单流程状态列表页面
	 */
	@RequiresPermissions("flow:base:flowState:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/flow/base/flowStateList";
	}
	
		/**
	 * 工单流程状态列表数据
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowState:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(FlowState flowState, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FlowState> page = flowStateService.findPage(new Page<FlowState>(request, response), flowState); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工单流程状态表单页面
	 */
	@RequiresPermissions(value={"flow:base:flowState:view","flow:base:flowState:add","flow:base:flowState:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FlowState flowState, Model model) {
		model.addAttribute("flowState", flowState);
		return "modules/flow/base/flowStateForm";
	}

	/**
	 * 保存工单流程状态
	 */
	@ResponseBody
	@RequiresPermissions(value={"flow:base:flowState:add","flow:base:flowState:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(FlowState flowState, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, flowState)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		ProjectInfo projectInfo=flowState.getProjectInfo();
		if(null!=projectInfo){
			flowState.setProjectId(projectInfo.getId());
			flowState.setProjectName(projectInfo.getProjectName());
		}
		flowStateService.save(flowState);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存工单流程状态成功");
		return j;
	}
	
	/**
	 * 删除工单流程状态
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowState:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(FlowState flowState, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		flowStateService.delete(flowState);
		j.setMsg("删除工单流程状态成功");
		return j;
	}
	
	/**
	 * 批量删除工单流程状态
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowState:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			flowStateService.delete(flowStateService.get(id));
		}
		j.setMsg("删除工单流程状态成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowState:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(FlowState flowState, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工单流程状态"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FlowState> page = flowStateService.findPage(new Page<FlowState>(request, response, -1), flowState);
    		new ExportExcel("工单流程状态", FlowState.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工单流程状态记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("flow:base:flowState:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FlowState> list = ei.getDataList(FlowState.class);
			for (FlowState flowState : list){
				try{
					flowStateService.save(flowState);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工单流程状态记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工单流程状态记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工单流程状态失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/base/flowState/?repage";
    }
	
	/**
	 * 下载导入工单流程状态数据模板
	 */
	@RequiresPermissions("flow:base:flowState:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工单流程状态数据导入模板.xlsx";
    		List<FlowState> list = Lists.newArrayList(); 
    		new ExportExcel("工单流程状态数据", FlowState.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/base/flowState/?repage";
    }

}
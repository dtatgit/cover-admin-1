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
import com.jeeplus.modules.flow.entity.base.FlowProc;
import com.jeeplus.modules.flow.service.base.FlowProcService;

/**
 * 工单流程定义Controller
 * @author crj
 * @version 2019-11-21
 */
@Controller
@RequestMapping(value = "${adminPath}/flow/base/flowProc")
public class FlowProcController extends BaseController {

	@Autowired
	private FlowProcService flowProcService;
	
	@ModelAttribute
	public FlowProc get(@RequestParam(required=false) String id) {
		FlowProc entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = flowProcService.get(id);
		}
		if (entity == null){
			entity = new FlowProc();
		}
		return entity;
	}
	
	/**
	 * 工单流程定义列表页面
	 */
	@RequiresPermissions("flow:base:flowProc:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/flow/base/flowProcList";
	}
	
		/**
	 * 工单流程定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowProc:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(FlowProc flowProc, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FlowProc> page = flowProcService.findPage(new Page<FlowProc>(request, response), flowProc); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工单流程定义表单页面
	 */
	@RequiresPermissions(value={"flow:base:flowProc:view","flow:base:flowProc:add","flow:base:flowProc:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FlowProc flowProc, Model model) {
		model.addAttribute("flowProc", flowProc);
		return "modules/flow/base/flowProcForm";
	}

	/**
	 * 保存工单流程定义
	 */
	@ResponseBody
	@RequiresPermissions(value={"flow:base:flowProc:add","flow:base:flowProc:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(FlowProc flowProc, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, flowProc)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		flowProcService.save(flowProc);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存工单流程定义成功");
		return j;
	}
	
	/**
	 * 删除工单流程定义
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowProc:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(FlowProc flowProc, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		flowProcService.delete(flowProc);
		j.setMsg("删除工单流程定义成功");
		return j;
	}
	
	/**
	 * 批量删除工单流程定义
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowProc:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			flowProcService.delete(flowProcService.get(id));
		}
		j.setMsg("删除工单流程定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("flow:base:flowProc:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(FlowProc flowProc, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工单流程定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FlowProc> page = flowProcService.findPage(new Page<FlowProc>(request, response, -1), flowProc);
    		new ExportExcel("工单流程定义", FlowProc.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工单流程定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("flow:base:flowProc:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FlowProc> list = ei.getDataList(FlowProc.class);
			for (FlowProc flowProc : list){
				try{
					flowProcService.save(flowProc);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工单流程定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工单流程定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工单流程定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/base/flowProc/?repage";
    }
	
	/**
	 * 下载导入工单流程定义数据模板
	 */
	@RequiresPermissions("flow:base:flowProc:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工单流程定义数据导入模板.xlsx";
    		List<FlowProc> list = Lists.newArrayList(); 
    		new ExportExcel("工单流程定义数据", FlowProc.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/base/flowProc/?repage";
    }

}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.web.opt;

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
import com.jeeplus.modules.flow.entity.opt.FlowOptResult;
import com.jeeplus.modules.flow.service.opt.FlowOptResultService;

/**
 * 工单流程操作结果定义Controller
 * @author crj
 * @version 2019-11-21
 */
@Controller
@RequestMapping(value = "${adminPath}/flow/opt/flowOptResult")
public class FlowOptResultController extends BaseController {

	@Autowired
	private FlowOptResultService flowOptResultService;
	
	@ModelAttribute
	public FlowOptResult get(@RequestParam(required=false) String id) {
		FlowOptResult entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = flowOptResultService.get(id);
		}
		if (entity == null){
			entity = new FlowOptResult();
		}
		return entity;
	}
	
	/**
	 * 工单流程操作结果定义列表页面
	 */
	@RequiresPermissions("flow:opt:flowOptResult:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/flow/opt/flowOptResultList";
	}
	
		/**
	 * 工单流程操作结果定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowOptResult:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(FlowOptResult flowOptResult, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FlowOptResult> page = flowOptResultService.findPage(new Page<FlowOptResult>(request, response), flowOptResult); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工单流程操作结果定义表单页面
	 */
	@RequiresPermissions(value={"flow:opt:flowOptResult:view","flow:opt:flowOptResult:add","flow:opt:flowOptResult:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FlowOptResult flowOptResult, Model model) {
		model.addAttribute("flowOptResult", flowOptResult);
		return "modules/flow/opt/flowOptResultForm";
	}

	/**
	 * 保存工单流程操作结果定义
	 */
	@ResponseBody
	@RequiresPermissions(value={"flow:opt:flowOptResult:add","flow:opt:flowOptResult:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(FlowOptResult flowOptResult, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, flowOptResult)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		flowOptResultService.save(flowOptResult);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存工单流程操作结果定义成功");
		return j;
	}
	
	/**
	 * 删除工单流程操作结果定义
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowOptResult:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(FlowOptResult flowOptResult, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		flowOptResultService.delete(flowOptResult);
		j.setMsg("删除工单流程操作结果定义成功");
		return j;
	}
	
	/**
	 * 批量删除工单流程操作结果定义
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowOptResult:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			flowOptResultService.delete(flowOptResultService.get(id));
		}
		j.setMsg("删除工单流程操作结果定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowOptResult:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(FlowOptResult flowOptResult, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工单流程操作结果定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FlowOptResult> page = flowOptResultService.findPage(new Page<FlowOptResult>(request, response, -1), flowOptResult);
    		new ExportExcel("工单流程操作结果定义", FlowOptResult.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工单流程操作结果定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("flow:opt:flowOptResult:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FlowOptResult> list = ei.getDataList(FlowOptResult.class);
			for (FlowOptResult flowOptResult : list){
				try{
					flowOptResultService.save(flowOptResult);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工单流程操作结果定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工单流程操作结果定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工单流程操作结果定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/opt/flowOptResult/?repage";
    }
	
	/**
	 * 下载导入工单流程操作结果定义数据模板
	 */
	@RequiresPermissions("flow:opt:flowOptResult:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工单流程操作结果定义数据导入模板.xlsx";
    		List<FlowOptResult> list = Lists.newArrayList(); 
    		new ExportExcel("工单流程操作结果定义数据", FlowOptResult.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/opt/flowOptResult/?repage";
    }

}
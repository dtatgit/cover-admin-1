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
import com.jeeplus.modules.flow.entity.opt.FlowOpt;
import com.jeeplus.modules.flow.service.opt.FlowOptService;

/**
 * 工单流程操作定义Controller
 * @author crj
 * @version 2019-11-21
 */
@Controller
@RequestMapping(value = "${adminPath}/flow/opt/flowOpt")
public class FlowOptController extends BaseController {

	@Autowired
	private FlowOptService flowOptService;
	
	@ModelAttribute
	public FlowOpt get(@RequestParam(required=false) String id) {
		FlowOpt entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = flowOptService.get(id);
		}
		if (entity == null){
			entity = new FlowOpt();
		}
		return entity;
	}
	
	/**
	 * 工单流程操作定义列表页面
	 */
	@RequiresPermissions("flow:opt:flowOpt:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/flow/opt/flowOptList";
	}
	
		/**
	 * 工单流程操作定义列表数据
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowOpt:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(FlowOpt flowOpt, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FlowOpt> page = flowOptService.findPage(new Page<FlowOpt>(request, response), flowOpt); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工单流程操作定义表单页面
	 */
	@RequiresPermissions(value={"flow:opt:flowOpt:view","flow:opt:flowOpt:add","flow:opt:flowOpt:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FlowOpt flowOpt, Model model) {
		model.addAttribute("flowOpt", flowOpt);
		return "modules/flow/opt/flowOptForm";
	}

	/**
	 * 保存工单流程操作定义
	 */
	@ResponseBody
	@RequiresPermissions(value={"flow:opt:flowOpt:add","flow:opt:flowOpt:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(FlowOpt flowOpt, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, flowOpt)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		flowOptService.save(flowOpt);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存工单流程操作定义成功");
		return j;
	}
	
	/**
	 * 删除工单流程操作定义
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowOpt:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(FlowOpt flowOpt, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		flowOptService.delete(flowOpt);
		j.setMsg("删除工单流程操作定义成功");
		return j;
	}
	
	/**
	 * 批量删除工单流程操作定义
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowOpt:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			flowOptService.delete(flowOptService.get(id));
		}
		j.setMsg("删除工单流程操作定义成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowOpt:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(FlowOpt flowOpt, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工单流程操作定义"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FlowOpt> page = flowOptService.findPage(new Page<FlowOpt>(request, response, -1), flowOpt);
    		new ExportExcel("工单流程操作定义", FlowOpt.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工单流程操作定义记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("flow:opt:flowOpt:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FlowOpt> list = ei.getDataList(FlowOpt.class);
			for (FlowOpt flowOpt : list){
				try{
					flowOptService.save(flowOpt);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工单流程操作定义记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工单流程操作定义记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工单流程操作定义失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/opt/flowOpt/?repage";
    }
	
	/**
	 * 下载导入工单流程操作定义数据模板
	 */
	@RequiresPermissions("flow:opt:flowOpt:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工单流程操作定义数据导入模板.xlsx";
    		List<FlowOpt> list = Lists.newArrayList(); 
    		new ExportExcel("工单流程操作定义数据", FlowOpt.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/opt/flowOpt/?repage";
    }

}
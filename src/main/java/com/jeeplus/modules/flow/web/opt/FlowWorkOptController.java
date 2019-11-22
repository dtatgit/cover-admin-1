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
import com.jeeplus.modules.flow.entity.opt.FlowWorkOpt;
import com.jeeplus.modules.flow.service.opt.FlowWorkOptService;

/**
 * 工单操作记录Controller
 * @author crj
 * @version 2019-11-21
 */
@Controller
@RequestMapping(value = "${adminPath}/flow/opt/flowWorkOpt")
public class FlowWorkOptController extends BaseController {

	@Autowired
	private FlowWorkOptService flowWorkOptService;
	
	@ModelAttribute
	public FlowWorkOpt get(@RequestParam(required=false) String id) {
		FlowWorkOpt entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = flowWorkOptService.get(id);
		}
		if (entity == null){
			entity = new FlowWorkOpt();
		}
		return entity;
	}
	
	/**
	 * 工单操作记录列表页面
	 */
	@RequiresPermissions("flow:opt:flowWorkOpt:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/flow/opt/flowWorkOptList";
	}
	
		/**
	 * 工单操作记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowWorkOpt:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(FlowWorkOpt flowWorkOpt, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FlowWorkOpt> page = flowWorkOptService.findPage(new Page<FlowWorkOpt>(request, response), flowWorkOpt); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工单操作记录表单页面
	 */
	@RequiresPermissions(value={"flow:opt:flowWorkOpt:view","flow:opt:flowWorkOpt:add","flow:opt:flowWorkOpt:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FlowWorkOpt flowWorkOpt, Model model) {
		model.addAttribute("flowWorkOpt", flowWorkOpt);
		return "modules/flow/opt/flowWorkOptForm";
	}

	/**
	 * 保存工单操作记录
	 */
	@ResponseBody
	@RequiresPermissions(value={"flow:opt:flowWorkOpt:add","flow:opt:flowWorkOpt:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(FlowWorkOpt flowWorkOpt, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, flowWorkOpt)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		flowWorkOptService.save(flowWorkOpt);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存工单操作记录成功");
		return j;
	}
	
	/**
	 * 删除工单操作记录
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowWorkOpt:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(FlowWorkOpt flowWorkOpt, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		flowWorkOptService.delete(flowWorkOpt);
		j.setMsg("删除工单操作记录成功");
		return j;
	}
	
	/**
	 * 批量删除工单操作记录
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowWorkOpt:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			flowWorkOptService.delete(flowWorkOptService.get(id));
		}
		j.setMsg("删除工单操作记录成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("flow:opt:flowWorkOpt:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(FlowWorkOpt flowWorkOpt, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工单操作记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FlowWorkOpt> page = flowWorkOptService.findPage(new Page<FlowWorkOpt>(request, response, -1), flowWorkOpt);
    		new ExportExcel("工单操作记录", FlowWorkOpt.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工单操作记录记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("flow:opt:flowWorkOpt:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FlowWorkOpt> list = ei.getDataList(FlowWorkOpt.class);
			for (FlowWorkOpt flowWorkOpt : list){
				try{
					flowWorkOptService.save(flowWorkOpt);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工单操作记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工单操作记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工单操作记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/opt/flowWorkOpt/?repage";
    }
	
	/**
	 * 下载导入工单操作记录数据模板
	 */
	@RequiresPermissions("flow:opt:flowWorkOpt:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工单操作记录数据导入模板.xlsx";
    		List<FlowWorkOpt> list = Lists.newArrayList(); 
    		new ExportExcel("工单操作记录数据", FlowWorkOpt.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/flow/opt/flowWorkOpt/?repage";
    }

}
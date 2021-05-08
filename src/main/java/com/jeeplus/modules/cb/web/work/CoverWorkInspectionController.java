/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.work;

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
import com.jeeplus.modules.cb.entity.work.CoverWorkInspection;
import com.jeeplus.modules.cb.service.work.CoverWorkInspectionService;

/**
 * 工单巡查Controller
 * @author crj
 * @version 2021-02-08
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/work/coverWorkInspection")
public class CoverWorkInspectionController extends BaseController {

	@Autowired
	private CoverWorkInspectionService coverWorkInspectionService;
	
	@ModelAttribute
	public CoverWorkInspection get(@RequestParam(required=false) String id) {
		CoverWorkInspection entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverWorkInspectionService.get(id);
		}
		if (entity == null){
			entity = new CoverWorkInspection();
		}
		return entity;
	}
	
	/**
	 * 工单巡查列表页面
	 */
	@RequiresPermissions("cb:work:coverWorkInspection:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/work/coverWorkInspectionList";
	}
	
		/**
	 * 工单巡查列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkInspection:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverWorkInspection coverWorkInspection, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverWorkInspection> page = coverWorkInspectionService.findPage(new Page<CoverWorkInspection>(request, response), coverWorkInspection); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工单巡查表单页面
	 */
	@RequiresPermissions(value={"cb:work:coverWorkInspection:view","cb:work:coverWorkInspection:add","cb:work:coverWorkInspection:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverWorkInspection coverWorkInspection, Model model) {
		model.addAttribute("coverWorkInspection", coverWorkInspection);
		if(StringUtils.isBlank(coverWorkInspection.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/cb/work/coverWorkInspectionForm";
	}

	/**
	 * 保存工单巡查
	 */
	@RequiresPermissions(value={"cb:work:coverWorkInspection:add","cb:work:coverWorkInspection:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CoverWorkInspection coverWorkInspection, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, coverWorkInspection)){
			return form(coverWorkInspection, model);
		}
		//新增或编辑表单保存
		coverWorkInspectionService.save(coverWorkInspection);//保存
		addMessage(redirectAttributes, "保存工单巡查成功");
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkInspection/?repage";
	}
	
	/**
	 * 删除工单巡查
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkInspection:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverWorkInspection coverWorkInspection, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverWorkInspectionService.delete(coverWorkInspection);
		j.setMsg("删除工单巡查成功");
		return j;
	}
	
	/**
	 * 批量删除工单巡查
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkInspection:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverWorkInspectionService.delete(coverWorkInspectionService.get(id));
		}
		j.setMsg("删除工单巡查成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkInspection:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverWorkInspection coverWorkInspection, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工单巡查"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverWorkInspection> page = coverWorkInspectionService.findPage(new Page<CoverWorkInspection>(request, response, -1), coverWorkInspection);
    		new ExportExcel("工单巡查", CoverWorkInspection.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工单巡查记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cb:work:coverWorkInspection:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverWorkInspection> list = ei.getDataList(CoverWorkInspection.class);
			for (CoverWorkInspection coverWorkInspection : list){
				try{
					coverWorkInspectionService.save(coverWorkInspection);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工单巡查记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工单巡查记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工单巡查失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkInspection/?repage";
    }
	
	/**
	 * 下载导入工单巡查数据模板
	 */
	@RequiresPermissions("cb:work:coverWorkInspection:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工单巡查数据导入模板.xlsx";
    		List<CoverWorkInspection> list = Lists.newArrayList(); 
    		new ExportExcel("工单巡查数据", CoverWorkInspection.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkInspection/?repage";
    }

}
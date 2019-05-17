/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.web.task;

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
import com.jeeplus.modules.cv.entity.task.CoverTableField;
import com.jeeplus.modules.cv.service.task.CoverTableFieldService;

/**
 * 井盖任务数据权限Controller
 * @author crj
 * @version 2019-05-17
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/task/coverTableField")
public class CoverTableFieldController extends BaseController {

	@Autowired
	private CoverTableFieldService coverTableFieldService;
	
	@ModelAttribute
	public CoverTableField get(@RequestParam(required=false) String id) {
		CoverTableField entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverTableFieldService.get(id);
		}
		if (entity == null){
			entity = new CoverTableField();
		}
		return entity;
	}
	
	/**
	 * 井盖任务数据权限列表页面
	 */
	@RequiresPermissions("cv:task:coverTableField:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/task/coverTableFieldList";
	}
	
		/**
	 * 井盖任务数据权限列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTableField:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverTableField coverTableField, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverTableField> page = coverTableFieldService.findPage(new Page<CoverTableField>(request, response), coverTableField); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖任务数据权限表单页面
	 */
	@RequiresPermissions(value={"cv:task:coverTableField:view","cv:task:coverTableField:add","cv:task:coverTableField:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverTableField coverTableField, Model model) {
		model.addAttribute("coverTableField", coverTableField);
		return "modules/cv/task/coverTableFieldForm";
	}

	/**
	 * 保存井盖任务数据权限
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:task:coverTableField:add","cv:task:coverTableField:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverTableField coverTableField, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverTableField)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverTableFieldService.save(coverTableField);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井盖任务数据权限成功");
		return j;
	}
	
	/**
	 * 删除井盖任务数据权限
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTableField:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverTableField coverTableField, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverTableFieldService.delete(coverTableField);
		j.setMsg("删除井盖任务数据权限成功");
		return j;
	}
	
	/**
	 * 批量删除井盖任务数据权限
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTableField:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverTableFieldService.delete(coverTableFieldService.get(id));
		}
		j.setMsg("删除井盖任务数据权限成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:task:coverTableField:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverTableField coverTableField, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井盖任务数据权限"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverTableField> page = coverTableFieldService.findPage(new Page<CoverTableField>(request, response, -1), coverTableField);
    		new ExportExcel("井盖任务数据权限", CoverTableField.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井盖任务数据权限记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:task:coverTableField:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverTableField> list = ei.getDataList(CoverTableField.class);
			for (CoverTableField coverTableField : list){
				try{
					coverTableFieldService.save(coverTableField);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井盖任务数据权限记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井盖任务数据权限记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井盖任务数据权限失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/task/coverTableField/?repage";
    }
	
	/**
	 * 下载导入井盖任务数据权限数据模板
	 */
	@RequiresPermissions("cv:task:coverTableField:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井盖任务数据权限数据导入模板.xlsx";
    		List<CoverTableField> list = Lists.newArrayList(); 
    		new ExportExcel("井盖任务数据权限数据", CoverTableField.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/task/coverTableField/?repage";
    }

}
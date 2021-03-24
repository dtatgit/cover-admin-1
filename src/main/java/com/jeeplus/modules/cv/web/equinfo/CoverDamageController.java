/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.web.equinfo;

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
import com.jeeplus.modules.cv.entity.equinfo.CoverDamage;
import com.jeeplus.modules.cv.service.equinfo.CoverDamageService;

/**
 * 井盖病害Controller
 * @author crj
 * @version 2019-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/equinfo/coverDamage")
public class CoverDamageController extends BaseController {

	@Autowired
	private CoverDamageService coverDamageService;
	
	@ModelAttribute
	public CoverDamage get(@RequestParam(required=false) String id) {
		CoverDamage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverDamageService.get(id);
		}
		if (entity == null){
			entity = new CoverDamage();
		}
		return entity;
	}
	
	/**
	 * 井盖病害列表页面
	 */
	@RequiresPermissions("cv:equinfo:coverDamage:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/equinfo/coverDamageList";
	}
	
		/**
	 * 井盖病害列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverDamage:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverDamage coverDamage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverDamage> page = coverDamageService.findPage(new Page<CoverDamage>(request, response), coverDamage); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖病害表单页面
	 */
	@RequiresPermissions(value={"cv:equinfo:coverDamage:view","cv:equinfo:coverDamage:add","cv:equinfo:coverDamage:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverDamage coverDamage, Model model) {
		model.addAttribute("coverDamage", coverDamage);
		if(StringUtils.isBlank(coverDamage.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/cv/equinfo/coverDamageForm";
	}

	/**
	 * 保存井盖病害
	 */
	@RequiresPermissions(value={"cv:equinfo:coverDamage:add","cv:equinfo:coverDamage:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CoverDamage coverDamage, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, coverDamage)){
			return form(coverDamage, model);
		}
		//新增或编辑表单保存
		coverDamageService.save(coverDamage);//保存
		addMessage(redirectAttributes, "保存井盖病害成功");
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverDamage/?repage";
	}
	
	/**
	 * 删除井盖病害
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverDamage:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverDamage coverDamage, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverDamageService.delete(coverDamage);
		j.setMsg("删除井盖病害成功");
		return j;
	}
	
	/**
	 * 批量删除井盖病害
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverDamage:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverDamageService.delete(coverDamageService.get(id));
		}
		j.setMsg("删除井盖病害成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverDamage:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverDamage coverDamage, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井盖病害"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverDamage> page = coverDamageService.findPage(new Page<CoverDamage>(request, response, -1), coverDamage);
    		new ExportExcel("井盖病害", CoverDamage.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井盖病害记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:equinfo:coverDamage:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverDamage> list = ei.getDataList(CoverDamage.class);
			for (CoverDamage coverDamage : list){
				try{
					coverDamageService.save(coverDamage);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井盖病害记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井盖病害记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井盖病害失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverDamage/?repage";
    }
	
	/**
	 * 下载导入井盖病害数据模板
	 */
	@RequiresPermissions("cv:equinfo:coverDamage:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井盖病害数据导入模板.xlsx";
    		List<CoverDamage> list = Lists.newArrayList(); 
    		new ExportExcel("井盖病害数据", CoverDamage.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverDamage/?repage";
    }

}
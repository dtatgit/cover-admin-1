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
import com.jeeplus.modules.cv.entity.equinfo.CoverOfficeOwner;
import com.jeeplus.modules.cv.service.equinfo.CoverOfficeOwnerService;

/**
 * 井盖维护单位配置Controller
 * @author crj
 * @version 2019-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/equinfo/coverOfficeOwner")
public class CoverOfficeOwnerController extends BaseController {

	@Autowired
	private CoverOfficeOwnerService coverOfficeOwnerService;

	@ModelAttribute
	public CoverOfficeOwner get(@RequestParam(required=false) String id) {
		CoverOfficeOwner entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverOfficeOwnerService.get(id);
		}
		if (entity == null){
			entity = new CoverOfficeOwner();
		}
		return entity;
	}

	/**
	 * 井盖维护单位配置列表页面
	 */
	@RequiresPermissions("cv:equinfo:coverOfficeOwner:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/equinfo/coverOfficeOwnerList";
	}

		/**
	 * 井盖维护单位配置列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverOfficeOwner:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverOfficeOwner coverOfficeOwner, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverOfficeOwner> page = coverOfficeOwnerService.findPage(new Page<CoverOfficeOwner>(request, response), coverOfficeOwner);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖维护单位配置表单页面
	 */
	@RequiresPermissions(value={"cv:equinfo:coverOfficeOwner:view","cv:equinfo:coverOfficeOwner:add","cv:equinfo:coverOfficeOwner:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverOfficeOwner coverOfficeOwner, Model model) {
		model.addAttribute("coverOfficeOwner", coverOfficeOwner);
		return "modules/cv/equinfo/coverOfficeOwnerForm";
	}

	/**
	 * 保存井盖维护单位配置
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:equinfo:coverOfficeOwner:add","cv:equinfo:coverOfficeOwner:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverOfficeOwner coverOfficeOwner, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverOfficeOwner)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverOfficeOwnerService.save(coverOfficeOwner);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井盖维护单位配置成功");
		return j;
	}

	/**
	 * 删除井盖维护单位配置
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverOfficeOwner:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverOfficeOwner coverOfficeOwner, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverOfficeOwnerService.delete(coverOfficeOwner);
		j.setMsg("删除井盖维护单位配置成功");
		return j;
	}

	/**
	 * 批量删除井盖维护单位配置
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverOfficeOwner:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverOfficeOwnerService.delete(coverOfficeOwnerService.get(id));
		}
		j.setMsg("删除井盖维护单位配置成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverOfficeOwner:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverOfficeOwner coverOfficeOwner, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井盖维护单位配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverOfficeOwner> page = coverOfficeOwnerService.findPage(new Page<CoverOfficeOwner>(request, response, -1), coverOfficeOwner);
    		new ExportExcel("井盖维护单位配置", CoverOfficeOwner.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井盖维护单位配置记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:equinfo:coverOfficeOwner:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverOfficeOwner> list = ei.getDataList(CoverOfficeOwner.class);
			for (CoverOfficeOwner coverOfficeOwner : list){
				try{
					coverOfficeOwnerService.save(coverOfficeOwner);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井盖维护单位配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井盖维护单位配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井盖维护单位配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverOfficeOwner/?repage";
    }

	/**
	 * 下载导入井盖维护单位配置数据模板
	 */
	@RequiresPermissions("cv:equinfo:coverOfficeOwner:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井盖维护单位配置数据导入模板.xlsx";
    		List<CoverOfficeOwner> list = Lists.newArrayList();
    		new ExportExcel("井盖维护单位配置数据", CoverOfficeOwner.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverOfficeOwner/?repage";
    }


	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverOfficeOwner:synData")
	@RequestMapping(value = "synData")
	public AjaxJson synData() {
		AjaxJson j = new AjaxJson();
		boolean flag=coverOfficeOwnerService.office0wnerHandle();
		if(flag){
			j.setSuccess(true);
			j.setMsg("数据同步成功！");
		}else{
			j.setSuccess(false);
			j.setMsg("数据同步失败！");
		}

		return j;
	}

}
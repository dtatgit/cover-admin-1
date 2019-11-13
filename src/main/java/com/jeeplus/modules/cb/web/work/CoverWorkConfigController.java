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
import com.jeeplus.modules.cb.entity.work.CoverWorkConfig;
import com.jeeplus.modules.cb.service.work.CoverWorkConfigService;

/**
 * 工单配置Controller
 * @author crj
 * @version 2019-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/work/coverWorkConfig")
public class CoverWorkConfigController extends BaseController {

	@Autowired
	private CoverWorkConfigService coverWorkConfigService;
	
	@ModelAttribute
	public CoverWorkConfig get(@RequestParam(required=false) String id) {
		CoverWorkConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverWorkConfigService.get(id);
		}
		if (entity == null){
			entity = new CoverWorkConfig();
		}
		return entity;
	}
	
	/**
	 * 工单配置列表页面
	 */
	@RequiresPermissions("cb:work:coverWorkConfig:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/work/coverWorkConfigList";
	}
	
		/**
	 * 工单配置列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkConfig:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverWorkConfig coverWorkConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverWorkConfig> page = coverWorkConfigService.findPage(new Page<CoverWorkConfig>(request, response), coverWorkConfig); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工单配置表单页面
	 */
	@RequiresPermissions(value={"cb:work:coverWorkConfig:view","cb:work:coverWorkConfig:add","cb:work:coverWorkConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverWorkConfig coverWorkConfig, Model model) {
		model.addAttribute("coverWorkConfig", coverWorkConfig);
		return "modules/cb/work/coverWorkConfigForm";
	}

	/**
	 * 保存工单配置
	 */
	@ResponseBody
	@RequiresPermissions(value={"cb:work:coverWorkConfig:add","cb:work:coverWorkConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverWorkConfig coverWorkConfig, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverWorkConfig)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverWorkConfigService.save(coverWorkConfig);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存工单配置成功");
		return j;
	}
	
	/**
	 * 删除工单配置
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkConfig:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverWorkConfig coverWorkConfig, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverWorkConfigService.delete(coverWorkConfig);
		j.setMsg("删除工单配置成功");
		return j;
	}
	
	/**
	 * 批量删除工单配置
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkConfig:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverWorkConfigService.delete(coverWorkConfigService.get(id));
		}
		j.setMsg("删除工单配置成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkConfig:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverWorkConfig coverWorkConfig, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工单配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverWorkConfig> page = coverWorkConfigService.findPage(new Page<CoverWorkConfig>(request, response, -1), coverWorkConfig);
    		new ExportExcel("工单配置", CoverWorkConfig.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工单配置记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cb:work:coverWorkConfig:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverWorkConfig> list = ei.getDataList(CoverWorkConfig.class);
			for (CoverWorkConfig coverWorkConfig : list){
				try{
					coverWorkConfigService.save(coverWorkConfig);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工单配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工单配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工单配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkConfig/?repage";
    }
	
	/**
	 * 下载导入工单配置数据模板
	 */
	@RequiresPermissions("cb:work:coverWorkConfig:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工单配置数据导入模板.xlsx";
    		List<CoverWorkConfig> list = Lists.newArrayList(); 
    		new ExportExcel("工单配置数据", CoverWorkConfig.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkConfig/?repage";
    }

}
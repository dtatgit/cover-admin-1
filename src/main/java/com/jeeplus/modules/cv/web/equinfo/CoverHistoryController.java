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
import com.jeeplus.modules.cv.entity.equinfo.CoverHistory;
import com.jeeplus.modules.cv.service.equinfo.CoverHistoryService;

/**
 * 井盖历史记录Controller
 * @author crj
 * @version 2019-05-15
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/equinfo/coverHistory")
public class CoverHistoryController extends BaseController {

	@Autowired
	private CoverHistoryService coverHistoryService;
	
	@ModelAttribute
	public CoverHistory get(@RequestParam(required=false) String id) {
		CoverHistory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverHistoryService.get(id);
		}
		if (entity == null){
			entity = new CoverHistory();
		}
		return entity;
	}
	
	/**
	 * 井盖历史记录列表页面
	 */
	@RequiresPermissions("cv:equinfo:coverHistory:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/equinfo/coverHistoryList";
	}
	
		/**
	 * 井盖历史记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverHistory:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverHistory coverHistory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverHistory> page = coverHistoryService.findPage(new Page<CoverHistory>(request, response), coverHistory); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖历史记录表单页面
	 */
	@RequiresPermissions(value={"cv:equinfo:coverHistory:view","cv:equinfo:coverHistory:add","cv:equinfo:coverHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverHistory coverHistory, Model model) {
		model.addAttribute("coverHistory", coverHistory);
		return "modules/cv/equinfo/coverHistoryForm";
	}

	/**
	 * 保存井盖历史记录
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:equinfo:coverHistory:add","cv:equinfo:coverHistory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverHistory coverHistory, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverHistory)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverHistoryService.save(coverHistory);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井盖历史记录成功");
		return j;
	}
	
	/**
	 * 删除井盖历史记录
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverHistory:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverHistory coverHistory, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverHistoryService.delete(coverHistory);
		j.setMsg("删除井盖历史记录成功");
		return j;
	}
	
	/**
	 * 批量删除井盖历史记录
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverHistory:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverHistoryService.delete(coverHistoryService.get(id));
		}
		j.setMsg("删除井盖历史记录成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverHistory:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverHistory coverHistory, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井盖历史记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverHistory> page = coverHistoryService.findPage(new Page<CoverHistory>(request, response, -1), coverHistory);
    		new ExportExcel("井盖历史记录", CoverHistory.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井盖历史记录记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:equinfo:coverHistory:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverHistory> list = ei.getDataList(CoverHistory.class);
			for (CoverHistory coverHistory : list){
				try{
					coverHistoryService.save(coverHistory);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井盖历史记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井盖历史记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井盖历史记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverHistory/?repage";
    }
	
	/**
	 * 下载导入井盖历史记录数据模板
	 */
	@RequiresPermissions("cv:equinfo:coverHistory:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井盖历史记录数据导入模板.xlsx";
    		List<CoverHistory> list = Lists.newArrayList(); 
    		new ExportExcel("井盖历史记录数据", CoverHistory.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverHistory/?repage";
    }

}
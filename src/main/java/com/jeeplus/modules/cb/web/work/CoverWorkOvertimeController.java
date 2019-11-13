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
import com.jeeplus.modules.cb.entity.work.CoverWorkOvertime;
import com.jeeplus.modules.cb.service.work.CoverWorkOvertimeService;

/**
 * 超时工单Controller
 * @author crj
 * @version 2019-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/work/coverWorkOvertime")
public class CoverWorkOvertimeController extends BaseController {

	@Autowired
	private CoverWorkOvertimeService coverWorkOvertimeService;
	
	@ModelAttribute
	public CoverWorkOvertime get(@RequestParam(required=false) String id) {
		CoverWorkOvertime entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverWorkOvertimeService.get(id);
		}
		if (entity == null){
			entity = new CoverWorkOvertime();
		}
		return entity;
	}
	
	/**
	 * 超时工单列表页面
	 */
	@RequiresPermissions("cb:work:coverWorkOvertime:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/work/coverWorkOvertimeList";
	}
	
		/**
	 * 超时工单列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOvertime:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverWorkOvertime coverWorkOvertime, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverWorkOvertime> page = coverWorkOvertimeService.findPage(new Page<CoverWorkOvertime>(request, response), coverWorkOvertime); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑超时工单表单页面
	 */
	@RequiresPermissions(value={"cb:work:coverWorkOvertime:view","cb:work:coverWorkOvertime:add","cb:work:coverWorkOvertime:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverWorkOvertime coverWorkOvertime, Model model) {
		model.addAttribute("coverWorkOvertime", coverWorkOvertime);
		return "modules/cb/work/coverWorkOvertimeForm";
	}

	/**
	 * 保存超时工单
	 */
	@ResponseBody
	@RequiresPermissions(value={"cb:work:coverWorkOvertime:add","cb:work:coverWorkOvertime:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverWorkOvertime coverWorkOvertime, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverWorkOvertime)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverWorkOvertimeService.save(coverWorkOvertime);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存超时工单成功");
		return j;
	}
	
	/**
	 * 删除超时工单
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOvertime:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverWorkOvertime coverWorkOvertime, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverWorkOvertimeService.delete(coverWorkOvertime);
		j.setMsg("删除超时工单成功");
		return j;
	}
	
	/**
	 * 批量删除超时工单
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOvertime:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverWorkOvertimeService.delete(coverWorkOvertimeService.get(id));
		}
		j.setMsg("删除超时工单成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOvertime:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverWorkOvertime coverWorkOvertime, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "超时工单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverWorkOvertime> page = coverWorkOvertimeService.findPage(new Page<CoverWorkOvertime>(request, response, -1), coverWorkOvertime);
    		new ExportExcel("超时工单", CoverWorkOvertime.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出超时工单记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cb:work:coverWorkOvertime:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverWorkOvertime> list = ei.getDataList(CoverWorkOvertime.class);
			for (CoverWorkOvertime coverWorkOvertime : list){
				try{
					coverWorkOvertimeService.save(coverWorkOvertime);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条超时工单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条超时工单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入超时工单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkOvertime/?repage";
    }
	
	/**
	 * 下载导入超时工单数据模板
	 */
	@RequiresPermissions("cb:work:coverWorkOvertime:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "超时工单数据导入模板.xlsx";
    		List<CoverWorkOvertime> list = Lists.newArrayList(); 
    		new ExportExcel("超时工单数据", CoverWorkOvertime.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkOvertime/?repage";
    }

}
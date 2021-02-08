/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.web.statis;

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
import com.jeeplus.modules.cv.entity.statis.CoverStatis;
import com.jeeplus.modules.cv.service.statis.CoverStatisService;

/**
 * 井盖相关统计Controller
 * @author crj
 * @version 2021-02-08
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/statis/coverStatis")
public class CoverStatisController extends BaseController {

	@Autowired
	private CoverStatisService coverStatisService;
	
	@ModelAttribute
	public CoverStatis get(@RequestParam(required=false) String id) {
		CoverStatis entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverStatisService.get(id);
		}
		if (entity == null){
			entity = new CoverStatis();
		}
		return entity;
	}
	
	/**
	 * 井盖相关统计列表页面
	 */
	@RequiresPermissions("cv:statis:coverStatis:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/statis/coverStatisList";
	}
	
		/**
	 * 井盖相关统计列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:coverStatis:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverStatis coverStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverStatis> page = coverStatisService.findPage(new Page<CoverStatis>(request, response), coverStatis); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖相关统计表单页面
	 */
	@RequiresPermissions(value={"cv:statis:coverStatis:view","cv:statis:coverStatis:add","cv:statis:coverStatis:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverStatis coverStatis, Model model) {
		model.addAttribute("coverStatis", coverStatis);
		return "modules/cv/statis/coverStatisForm";
	}

	/**
	 * 保存井盖相关统计
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:statis:coverStatis:add","cv:statis:coverStatis:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverStatis coverStatis, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverStatis)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverStatisService.save(coverStatis);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井盖相关统计成功");
		return j;
	}
	
	/**
	 * 删除井盖相关统计
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:coverStatis:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverStatis coverStatis, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverStatisService.delete(coverStatis);
		j.setMsg("删除井盖相关统计成功");
		return j;
	}
	
	/**
	 * 批量删除井盖相关统计
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:coverStatis:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverStatisService.delete(coverStatisService.get(id));
		}
		j.setMsg("删除井盖相关统计成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:coverStatis:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverStatis coverStatis, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井盖相关统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverStatis> page = coverStatisService.findPage(new Page<CoverStatis>(request, response, -1), coverStatis);
    		new ExportExcel("井盖相关统计", CoverStatis.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井盖相关统计记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:statis:coverStatis:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverStatis> list = ei.getDataList(CoverStatis.class);
			for (CoverStatis coverStatis : list){
				try{
					coverStatisService.save(coverStatis);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井盖相关统计记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井盖相关统计记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井盖相关统计失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/statis/coverStatis/?repage";
    }
	
	/**
	 * 下载导入井盖相关统计数据模板
	 */
	@RequiresPermissions("cv:statis:coverStatis:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井盖相关统计数据导入模板.xlsx";
    		List<CoverStatis> list = Lists.newArrayList(); 
    		new ExportExcel("井盖相关统计数据", CoverStatis.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/statis/coverStatis/?repage";
    }

}
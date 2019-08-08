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
import com.jeeplus.modules.cb.entity.work.CoverWorkOperationDetail;
import com.jeeplus.modules.cb.service.work.CoverWorkOperationDetailService;

/**
 * 工单操作记录明细Controller
 * @author crj
 * @version 2019-08-07
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/work/coverWorkOperationDetail")
public class CoverWorkOperationDetailController extends BaseController {

	@Autowired
	private CoverWorkOperationDetailService coverWorkOperationDetailService;
	
	@ModelAttribute
	public CoverWorkOperationDetail get(@RequestParam(required=false) String id) {
		CoverWorkOperationDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverWorkOperationDetailService.get(id);
		}
		if (entity == null){
			entity = new CoverWorkOperationDetail();
		}
		return entity;
	}
	
	/**
	 * 工单操作记录明细列表页面
	 */
	@RequiresPermissions("cb:work:coverWorkOperationDetail:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/work/coverWorkOperationDetailList";
	}
	
		/**
	 * 工单操作记录明细列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOperationDetail:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverWorkOperationDetail coverWorkOperationDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverWorkOperationDetail> page = coverWorkOperationDetailService.findPage(new Page<CoverWorkOperationDetail>(request, response), coverWorkOperationDetail); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工单操作记录明细表单页面
	 */
	@RequiresPermissions(value={"cb:work:coverWorkOperationDetail:view","cb:work:coverWorkOperationDetail:add","cb:work:coverWorkOperationDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverWorkOperationDetail coverWorkOperationDetail, Model model) {
		model.addAttribute("coverWorkOperationDetail", coverWorkOperationDetail);
		return "modules/cb/work/coverWorkOperationDetailForm";
	}

	/**
	 * 保存工单操作记录明细
	 */
	@ResponseBody
	@RequiresPermissions(value={"cb:work:coverWorkOperationDetail:add","cb:work:coverWorkOperationDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverWorkOperationDetail coverWorkOperationDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverWorkOperationDetail)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverWorkOperationDetailService.save(coverWorkOperationDetail);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存工单操作记录明细成功");
		return j;
	}
	
	/**
	 * 删除工单操作记录明细
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOperationDetail:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverWorkOperationDetail coverWorkOperationDetail, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverWorkOperationDetailService.delete(coverWorkOperationDetail);
		j.setMsg("删除工单操作记录明细成功");
		return j;
	}
	
	/**
	 * 批量删除工单操作记录明细
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOperationDetail:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverWorkOperationDetailService.delete(coverWorkOperationDetailService.get(id));
		}
		j.setMsg("删除工单操作记录明细成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOperationDetail:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverWorkOperationDetail coverWorkOperationDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工单操作记录明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverWorkOperationDetail> page = coverWorkOperationDetailService.findPage(new Page<CoverWorkOperationDetail>(request, response, -1), coverWorkOperationDetail);
    		new ExportExcel("工单操作记录明细", CoverWorkOperationDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工单操作记录明细记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cb:work:coverWorkOperationDetail:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverWorkOperationDetail> list = ei.getDataList(CoverWorkOperationDetail.class);
			for (CoverWorkOperationDetail coverWorkOperationDetail : list){
				try{
					coverWorkOperationDetailService.save(coverWorkOperationDetail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工单操作记录明细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工单操作记录明细记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工单操作记录明细失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkOperationDetail/?repage";
    }
	
	/**
	 * 下载导入工单操作记录明细数据模板
	 */
	@RequiresPermissions("cb:work:coverWorkOperationDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工单操作记录明细数据导入模板.xlsx";
    		List<CoverWorkOperationDetail> list = Lists.newArrayList(); 
    		new ExportExcel("工单操作记录明细数据", CoverWorkOperationDetail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkOperationDetail/?repage";
    }

}
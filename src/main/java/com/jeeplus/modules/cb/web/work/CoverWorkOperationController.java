/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.work;

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
import com.jeeplus.modules.cb.entity.work.CoverWorkOperation;
import com.jeeplus.modules.cb.service.work.CoverWorkOperationService;

/**
 * 工单操作记录Controller
 * @author crj
 * @version 2019-06-26
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/work/coverWorkOperation")
public class CoverWorkOperationController extends BaseController {

	@Autowired
	private CoverWorkOperationService coverWorkOperationService;
	
	@ModelAttribute
	public CoverWorkOperation get(@RequestParam(required=false) String id) {
		CoverWorkOperation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverWorkOperationService.get(id);
		}
		if (entity == null){
			entity = new CoverWorkOperation();
		}
		return entity;
	}
	
	/**
	 * 工单操作记录列表页面
	 */
	@RequiresPermissions("cb:work:coverWorkOperation:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/work/coverWorkOperationList";
	}
	
		/**
	 * 工单操作记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOperation:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverWorkOperation coverWorkOperation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverWorkOperation> page = coverWorkOperationService.findPage(new Page<CoverWorkOperation>(request, response), coverWorkOperation); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工单操作记录表单页面
	 */
	@RequiresPermissions(value={"cb:work:coverWorkOperation:view","cb:work:coverWorkOperation:add","cb:work:coverWorkOperation:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverWorkOperation coverWorkOperation, Model model) {
		model.addAttribute("coverWorkOperation", coverWorkOperation);
		return "modules/cb/work/coverWorkOperationForm";
	}

	/**
	 * 保存工单操作记录
	 */
	@ResponseBody
	@RequiresPermissions(value={"cb:work:coverWorkOperation:add","cb:work:coverWorkOperation:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverWorkOperation coverWorkOperation, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverWorkOperation)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverWorkOperationService.save(coverWorkOperation);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存工单操作记录成功");
		return j;
	}
	
	/**
	 * 删除工单操作记录
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOperation:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverWorkOperation coverWorkOperation, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverWorkOperationService.delete(coverWorkOperation);
		j.setMsg("删除工单操作记录成功");
		return j;
	}
	
	/**
	 * 批量删除工单操作记录
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOperation:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverWorkOperationService.delete(coverWorkOperationService.get(id));
		}
		j.setMsg("删除工单操作记录成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWorkOperation:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverWorkOperation coverWorkOperation, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工单操作记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverWorkOperation> page = coverWorkOperationService.findPage(new Page<CoverWorkOperation>(request, response, -1), coverWorkOperation);
    		new ExportExcel("工单操作记录", CoverWorkOperation.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	@RequiresPermissions("cb:work:coverWorkOperation:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverWorkOperation> list = ei.getDataList(CoverWorkOperation.class);
			for (CoverWorkOperation coverWorkOperation : list){
				try{
					coverWorkOperationService.save(coverWorkOperation);
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
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkOperation/?repage";
    }
	
	/**
	 * 下载导入工单操作记录数据模板
	 */
	@RequiresPermissions("cb:work:coverWorkOperation:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工单操作记录数据导入模板.xlsx";
    		List<CoverWorkOperation> list = Lists.newArrayList(); 
    		new ExportExcel("工单操作记录数据", CoverWorkOperation.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWorkOperation/?repage";
    }

}
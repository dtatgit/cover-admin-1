/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.equinfo;

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
import com.jeeplus.modules.cb.entity.equinfo.CoverBellOperation;
import com.jeeplus.modules.cb.service.equinfo.CoverBellOperationService;

/**
 * 井铃操作记录Controller
 * @author crj
 * @version 2019-06-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/equinfo/coverBellOperation")
public class CoverBellOperationController extends BaseController {

	@Autowired
	private CoverBellOperationService coverBellOperationService;
	
	@ModelAttribute
	public CoverBellOperation get(@RequestParam(required=false) String id) {
		CoverBellOperation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverBellOperationService.get(id);
		}
		if (entity == null){
			entity = new CoverBellOperation();
		}
		return entity;
	}
	
	/**
	 * 井铃操作记录列表页面
	 */
	//@RequiresPermissions("cb:equinfo:coverBellOperation:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/equinfo/coverBellOperationList";
	}
	
		/**
	 * 井铃操作记录列表数据
	 */
	@ResponseBody
	//@RequiresPermissions("cb:equinfo:coverBellOperation:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverBellOperation coverBellOperation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverBellOperation> page = coverBellOperationService.findPage(new Page<CoverBellOperation>(request, response), coverBellOperation); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井铃操作记录表单页面
	 */
	@RequiresPermissions(value={"cb:equinfo:coverBellOperation:view","cb:equinfo:coverBellOperation:add","cb:equinfo:coverBellOperation:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverBellOperation coverBellOperation, Model model) {
		model.addAttribute("coverBellOperation", coverBellOperation);
		return "modules/cb/equinfo/coverBellOperationForm";
	}

	/**
	 * 保存井铃操作记录
	 */
	@ResponseBody
	@RequiresPermissions(value={"cb:equinfo:coverBellOperation:add","cb:equinfo:coverBellOperation:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverBellOperation coverBellOperation, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverBellOperation)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverBellOperationService.save(coverBellOperation);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井铃操作记录成功");
		return j;
	}
	
	/**
	 * 删除井铃操作记录
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBellOperation:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverBellOperation coverBellOperation, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverBellOperationService.delete(coverBellOperation);
		j.setMsg("删除井铃操作记录成功");
		return j;
	}
	
	/**
	 * 批量删除井铃操作记录
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBellOperation:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverBellOperationService.delete(coverBellOperationService.get(id));
		}
		j.setMsg("删除井铃操作记录成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBellOperation:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverBellOperation coverBellOperation, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井铃操作记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverBellOperation> page = coverBellOperationService.findPage(new Page<CoverBellOperation>(request, response, -1), coverBellOperation);
    		new ExportExcel("井铃操作记录", CoverBellOperation.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井铃操作记录记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cb:equinfo:coverBellOperation:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverBellOperation> list = ei.getDataList(CoverBellOperation.class);
			for (CoverBellOperation coverBellOperation : list){
				try{
					coverBellOperationService.save(coverBellOperation);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井铃操作记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井铃操作记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井铃操作记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/equinfo/coverBellOperation/?repage";
    }
	
	/**
	 * 下载导入井铃操作记录数据模板
	 */
	@RequiresPermissions("cb:equinfo:coverBellOperation:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井铃操作记录数据导入模板.xlsx";
    		List<CoverBellOperation> list = Lists.newArrayList(); 
    		new ExportExcel("井铃操作记录数据", CoverBellOperation.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/equinfo/coverBellOperation/?repage";
    }

}
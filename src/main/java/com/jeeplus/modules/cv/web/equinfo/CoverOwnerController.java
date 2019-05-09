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
import com.jeeplus.modules.cv.entity.equinfo.CoverOwner;
import com.jeeplus.modules.cv.service.equinfo.CoverOwnerService;

/**
 * 井盖权属单位Controller
 * @author crj
 * @version 2019-05-09
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/equinfo/coverOwner")
public class CoverOwnerController extends BaseController {

	@Autowired
	private CoverOwnerService coverOwnerService;
	
	@ModelAttribute
	public CoverOwner get(@RequestParam(required=false) String id) {
		CoverOwner entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverOwnerService.get(id);
		}
		if (entity == null){
			entity = new CoverOwner();
		}
		return entity;
	}
	
	/**
	 * 井盖权属单位列表页面
	 */
	@RequiresPermissions("cv:equinfo:coverOwner:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/equinfo/coverOwnerList";
	}
	
		/**
	 * 井盖权属单位列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverOwner:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverOwner coverOwner, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverOwner> page = coverOwnerService.findPage(new Page<CoverOwner>(request, response), coverOwner); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖权属单位表单页面
	 */
	@RequiresPermissions(value={"cv:equinfo:coverOwner:view","cv:equinfo:coverOwner:add","cv:equinfo:coverOwner:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverOwner coverOwner, Model model) {
		model.addAttribute("coverOwner", coverOwner);
		return "modules/cv/equinfo/coverOwnerForm";
	}

	/**
	 * 保存井盖权属单位
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:equinfo:coverOwner:add","cv:equinfo:coverOwner:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverOwner coverOwner, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverOwner)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverOwnerService.save(coverOwner);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井盖权属单位成功");
		return j;
	}
	
	/**
	 * 删除井盖权属单位
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverOwner:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverOwner coverOwner, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverOwnerService.delete(coverOwner);
		j.setMsg("删除井盖权属单位成功");
		return j;
	}
	
	/**
	 * 批量删除井盖权属单位
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverOwner:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverOwnerService.delete(coverOwnerService.get(id));
		}
		j.setMsg("删除井盖权属单位成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverOwner:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverOwner coverOwner, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井盖权属单位"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverOwner> page = coverOwnerService.findPage(new Page<CoverOwner>(request, response, -1), coverOwner);
    		new ExportExcel("井盖权属单位", CoverOwner.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井盖权属单位记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:equinfo:coverOwner:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverOwner> list = ei.getDataList(CoverOwner.class);
			for (CoverOwner coverOwner : list){
				try{
					coverOwnerService.save(coverOwner);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井盖权属单位记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井盖权属单位记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井盖权属单位失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverOwner/?repage";
    }
	
	/**
	 * 下载导入井盖权属单位数据模板
	 */
	@RequiresPermissions("cv:equinfo:coverOwner:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井盖权属单位数据导入模板.xlsx";
    		List<CoverOwner> list = Lists.newArrayList(); 
    		new ExportExcel("井盖权属单位数据", CoverOwner.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverOwner/?repage";
    }

}
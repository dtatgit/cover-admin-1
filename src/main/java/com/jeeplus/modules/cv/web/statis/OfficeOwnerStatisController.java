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
import com.jeeplus.modules.cv.entity.statis.OfficeOwnerStatis;
import com.jeeplus.modules.cv.service.statis.OfficeOwnerStatisService;

/**
 * 维护部门权属单位统计Controller
 * @author crj
 * @version 2021-03-22
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/statis/officeOwnerStatis")
public class OfficeOwnerStatisController extends BaseController {

	@Autowired
	private OfficeOwnerStatisService officeOwnerStatisService;
	
	@ModelAttribute
	public OfficeOwnerStatis get(@RequestParam(required=false) String id) {
		OfficeOwnerStatis entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = officeOwnerStatisService.get(id);
		}
		if (entity == null){
			entity = new OfficeOwnerStatis();
		}
		return entity;
	}
	
	/**
	 * 维护部门权属单位统计列表页面
	 */
	@RequiresPermissions("cv:statis:officeOwnerStatis:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/statis/officeOwnerStatisList";
	}
	
		/**
	 * 维护部门权属单位统计列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:officeOwnerStatis:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(OfficeOwnerStatis officeOwnerStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OfficeOwnerStatis> page = officeOwnerStatisService.findPage(new Page<OfficeOwnerStatis>(request, response), officeOwnerStatis); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑维护部门权属单位统计表单页面
	 */
	@RequiresPermissions(value={"cv:statis:officeOwnerStatis:view","cv:statis:officeOwnerStatis:add","cv:statis:officeOwnerStatis:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OfficeOwnerStatis officeOwnerStatis, Model model) {
		model.addAttribute("officeOwnerStatis", officeOwnerStatis);
		return "modules/cv/statis/officeOwnerStatisForm";
	}

	/**
	 * 保存维护部门权属单位统计
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:statis:officeOwnerStatis:add","cv:statis:officeOwnerStatis:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(OfficeOwnerStatis officeOwnerStatis, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, officeOwnerStatis)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		officeOwnerStatisService.save(officeOwnerStatis);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存维护部门权属单位统计成功");
		return j;
	}
	
	/**
	 * 删除维护部门权属单位统计
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:officeOwnerStatis:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(OfficeOwnerStatis officeOwnerStatis, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		officeOwnerStatisService.delete(officeOwnerStatis);
		j.setMsg("删除维护部门权属单位统计成功");
		return j;
	}
	
	/**
	 * 批量删除维护部门权属单位统计
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:officeOwnerStatis:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			officeOwnerStatisService.delete(officeOwnerStatisService.get(id));
		}
		j.setMsg("删除维护部门权属单位统计成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:officeOwnerStatis:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(OfficeOwnerStatis officeOwnerStatis, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "维护部门权属单位统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<OfficeOwnerStatis> page = officeOwnerStatisService.findPage(new Page<OfficeOwnerStatis>(request, response, -1), officeOwnerStatis);
    		new ExportExcel("维护部门权属单位统计", OfficeOwnerStatis.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出维护部门权属单位统计记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:statis:officeOwnerStatis:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OfficeOwnerStatis> list = ei.getDataList(OfficeOwnerStatis.class);
			for (OfficeOwnerStatis officeOwnerStatis : list){
				try{
					officeOwnerStatisService.save(officeOwnerStatis);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条维护部门权属单位统计记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条维护部门权属单位统计记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入维护部门权属单位统计失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/statis/officeOwnerStatis/?repage";
    }
	
	/**
	 * 下载导入维护部门权属单位统计数据模板
	 */
	@RequiresPermissions("cv:statis:officeOwnerStatis:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "维护部门权属单位统计数据导入模板.xlsx";
    		List<OfficeOwnerStatis> list = Lists.newArrayList(); 
    		new ExportExcel("维护部门权属单位统计数据", OfficeOwnerStatis.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/statis/officeOwnerStatis/?repage";
    }


	/**
	 * 工单排名列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:officeOwnerStatis:list")
	@RequestMapping(value = "dataOffice")
	public Map<String, Object> dataOffice(OfficeOwnerStatis officeOwnerStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OfficeOwnerStatis> page = officeOwnerStatisService.findPageByOffice(new Page<OfficeOwnerStatis>(request, response), officeOwnerStatis);
		return getBootstrapData(page);
	}

	/**
	 * 工单排名列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:officeOwnerStatis:list")
	@RequestMapping(value = "dataOwnerDepart")
	public Map<String, Object> dataOwnerDepart(OfficeOwnerStatis officeOwnerStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OfficeOwnerStatis> page = officeOwnerStatisService.findPageByOwnerDepart(new Page<OfficeOwnerStatis>(request, response), officeOwnerStatis);
		return getBootstrapData(page);
	}

}
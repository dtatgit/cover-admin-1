/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.web.equinfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.service.AreaService;
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
import com.jeeplus.modules.cv.entity.equinfo.CoverAudit;
import com.jeeplus.modules.cv.service.equinfo.CoverAuditService;

/**
 * 井盖审核信息Controller
 * @author crj
 * @version 2019-04-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/equinfo/coverAudit")
public class CoverAuditController extends BaseController {

	@Autowired
	private CoverAuditService coverAuditService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private CoverService coverService;
	
	@ModelAttribute
	public CoverAudit get(@RequestParam(required=false) String id) {
		CoverAudit entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverAuditService.get(id);
		}
		if (entity == null){
			entity = new CoverAudit();
		}
		return entity;
	}
	
	/**
	 * 井盖审核信息列表页面
	 */
	@RequiresPermissions("cv:equinfo:coverAudit:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/equinfo/coverAuditList";
	}
	
		/**
	 * 井盖审核信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverAudit:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverAudit coverAudit, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverAudit> page = coverAuditService.findPage(new Page<CoverAudit>(request, response), coverAudit); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖审核信息表单页面
	 */
	@RequiresPermissions(value={"cv:equinfo:coverAudit:view","cv:equinfo:coverAudit:add","cv:equinfo:coverAudit:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverAudit coverAudit, Model model) {
		model.addAttribute("coverAudit", coverAudit);
		return "modules/cv/equinfo/coverAuditForm";
	}

	/**
	 * 保存井盖审核信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:equinfo:coverAudit:add","cv:equinfo:coverAudit:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverAudit coverAudit, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverAudit)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverAuditService.save(coverAudit);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井盖审核信息成功");
		return j;
	}
	
	/**
	 * 删除井盖审核信息
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverAudit:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverAudit coverAudit, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverAuditService.delete(coverAudit);
		j.setMsg("删除井盖审核信息成功");
		return j;
	}
	
	/**
	 * 批量删除井盖审核信息
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverAudit:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverAuditService.delete(coverAuditService.get(id));
		}
		j.setMsg("删除井盖审核信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverAudit:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverAudit coverAudit, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井盖审核信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverAudit> page = coverAuditService.findPage(new Page<CoverAudit>(request, response, -1), coverAudit);
    		new ExportExcel("井盖审核信息", CoverAudit.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井盖审核信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:equinfo:coverAudit:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverAudit> list = ei.getDataList(CoverAudit.class);
			for (CoverAudit coverAudit : list){
				try{
					coverAuditService.save(coverAudit);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井盖审核信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井盖审核信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井盖审核信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverAudit/?repage";
    }
	
	/**
	 * 下载导入井盖审核信息数据模板
	 */
	@RequiresPermissions("cv:equinfo:coverAudit:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井盖审核信息数据导入模板.xlsx";
    		List<CoverAudit> list = Lists.newArrayList(); 
    		new ExportExcel("井盖审核信息数据", CoverAudit.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverAudit/?repage";
    }

	/**
	 * 获取待审核井盖查询页面
	 */
	@RequiresPermissions("cv:equinfo:coverAudit:obtainCover")
	@RequestMapping(value = "obtainCoverPage")
	public String obtainCoverPage(CoverAudit coverAudit, Model model) {
		model.addAttribute("coverAudit", coverAudit);
		return "modules/cv/equinfo/obtainCoverPage";
	}

	/**
	 * 获取待审核
	 * @param coverAudit
	 * @param model
	 * @return
	 */
/*
	@RequiresPermissions("cv:equinfo:coverAudit:obtainCover")
	@RequestMapping(value = "obtainCover")
	public String obtainCover(CoverAudit coverAudit, Model model) {
		model.addAttribute("coverAudit", coverAudit);
		return "modules/cv/equinfo/obtainCoverPage";
	}
*/

	/**
	 * 获取待审核
	 * @param
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverAudit:obtainCover")
	@RequestMapping(value = "obtainCover")
	public AjaxJson obtainCover(CoverAudit coverAudit, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String areaId=coverAudit.getArea().getId();
		Area coverArea=areaService.get(areaId);
		boolean flag=coverAuditService.obtainCover(coverArea);
		System.out.println("*****************"+coverAudit.getArea().getId());
		if(flag){
			j.setSuccess(true);
			j.setMsg("获取任务成功！");
		}else{
			j.setSuccess(false);
			j.setMsg("获取任务失败，请重新获取！");
		}

		return j;
	}

	/**
	 * 获取审核页面
	 */
	@RequiresPermissions("cv:equinfo:coverAudit:audit")
	@RequestMapping(value = "auditPage")
	public String auditPage(CoverAudit coverAudit, Model model) {
		Cover cover=coverService.get(coverAudit.getCover().getId());
		coverAudit.setCover(cover);
		model.addAttribute("coverAudit", coverAudit);

		return "modules/cv/equinfo/coverAuditPage";
	}
	/**
	 * 保存用户审核信息
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverAudit:audit")
	@RequestMapping(value = "saveAudit")
	public AjaxJson saveAudit(CoverAudit coverAudit, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverAudit)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		System.out.println("***********************"+coverAudit.getAuditStatus());
		System.out.println("***********************"+coverAudit.getAuditResult());
		//cgRefundInfoService.saveAudit(cgRefundInfo);//新建或者编辑保存
		boolean flag=coverAuditService.auditCover(coverAudit);
		if(flag){
			j.setSuccess(true);
			j.setMsg("保存审核信息成功！");
		}else{
			j.setSuccess(false);
			j.setMsg("保存审核信息失败！");
		}

		return j;
	}


}
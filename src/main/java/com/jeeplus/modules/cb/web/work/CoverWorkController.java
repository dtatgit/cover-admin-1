/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.work;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
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
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.service.work.CoverWorkService;

/**
 * 工单信息Controller
 * @author crj
 * @version 2019-06-26
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/work/coverWork")
public class CoverWorkController extends BaseController {

	@Autowired
	private CoverWorkService coverWorkService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public CoverWork get(@RequestParam(required=false) String id) {
		CoverWork entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverWorkService.get(id);
		}
		if (entity == null){
			entity = new CoverWork();
		}
		return entity;
	}
	
	/**
	 * 工单信息列表页面
	 */
	@RequiresPermissions("cb:work:coverWork:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/work/coverWorkList";
	}
	
		/**
	 * 工单信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWork:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverWork coverWork, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverWork> page = coverWorkService.findPage(new Page<CoverWork>(request, response), coverWork);
		List<CoverWork> coverWorkList = page.getList();
		if(null!=coverWorkList&&coverWorkList.size()>0){
		for(CoverWork work:coverWorkList){
			work.setCreateBy(systemService.getUser(work.getCreateBy().getId()));
			Office office = officeService.get(work.getCreateDepart());
			work.setCreateDepart(office.getName());
		}
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工单信息表单页面
	 */
	@RequiresPermissions(value={"cb:work:coverWork:view","cb:work:coverWork:add","cb:work:coverWork:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverWork coverWork, Model model) {
		String id=coverWork.getId();
		if(StringUtils.isEmpty(id)){
			coverWork.setWorkNum(IdGen.getInfoCode("CW"));
		}
		model.addAttribute("coverWork", coverWork);
		return "modules/cb/work/coverWorkForm";
	}

	/**
	 * 保存工单信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"cb:work:coverWork:add","cb:work:coverWork:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverWork coverWork, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverWork)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		Cover cover=coverWork.getCover();
		if(null!=cover){
			coverWork.setCoverNo(cover.getNo());
		}
		String workStatus=coverWork.getWorkStatus();// 工单状态
		if(StringUtils.isEmpty(workStatus)){
			coverWork.setWorkStatus(CodeConstant.WORK_STATUS.WAIT_RECEIVE);
		}
		coverWorkService.save(coverWork);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存工单信息成功");
		return j;
	}
	
	/**
	 * 删除工单信息
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWork:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverWork coverWork, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverWorkService.delete(coverWork);
		j.setMsg("删除工单信息成功");
		return j;
	}
	
	/**
	 * 批量删除工单信息
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWork:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverWorkService.delete(coverWorkService.get(id));
		}
		j.setMsg("删除工单信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWork:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverWork coverWork, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工单信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverWork> page = coverWorkService.findPage(new Page<CoverWork>(request, response, -1), coverWork);
    		new ExportExcel("工单信息", CoverWork.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工单信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cb:work:coverWork:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverWork> list = ei.getDataList(CoverWork.class);
			for (CoverWork coverWork : list){
				try{
					coverWorkService.save(coverWork);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工单信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条工单信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入工单信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWork/?repage";
    }
	
	/**
	 * 下载导入工单信息数据模板
	 */
	@RequiresPermissions("cb:work:coverWork:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "工单信息数据导入模板.xlsx";
    		List<CoverWork> list = Lists.newArrayList(); 
    		new ExportExcel("工单信息数据", CoverWork.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWork/?repage";
    }

}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

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
import com.jeeplus.modules.sys.entity.SysUpload;
import com.jeeplus.modules.sys.service.SysUploadService;

/**
 * 文件管理Controller
 * @author ffy
 * @version 2021-07-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysUpload")
public class SysUploadController extends BaseController {

	@Autowired
	private SysUploadService sysUploadService;
	
	@ModelAttribute
	public SysUpload get(@RequestParam(required=false) String id) {
		SysUpload entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysUploadService.get(id);
		}
		if (entity == null){
			entity = new SysUpload();
		}
		return entity;
	}
	
	/**
	 * 文件列表页面
	 */
	@RequiresPermissions("sys:sysUpload:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/sys/sysUploadList";
	}
	
		/**
	 * 文件列表数据
	 */
	@ResponseBody
	@RequiresPermissions("sys:sysUpload:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SysUpload sysUpload, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysUpload> page = sysUploadService.findPage(new Page<SysUpload>(request, response), sysUpload); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑文件表单页面
	 */
	@RequiresPermissions(value={"sys:sysUpload:view","sys:sysUpload:add","sys:sysUpload:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SysUpload sysUpload, Model model) {
		model.addAttribute("sysUpload", sysUpload);
		return "modules/sys/sysUploadForm";
	}

	/**
	 * 保存文件
	 */
	@ResponseBody
	@RequiresPermissions(value={"sys:sysUpload:add","sys:sysUpload:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SysUpload sysUpload, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, sysUpload)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		sysUploadService.save(sysUpload);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存文件成功");
		return j;
	}
	
	/**
	 * 删除文件
	 */
	@ResponseBody
	@RequiresPermissions("sys:sysUpload:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SysUpload sysUpload, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		sysUploadService.delete(sysUpload);
		j.setMsg("删除文件成功");
		return j;
	}
	
	/**
	 * 批量删除文件
	 */
	@ResponseBody
	@RequiresPermissions("sys:sysUpload:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysUploadService.delete(sysUploadService.get(id));
		}
		j.setMsg("删除文件成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("sys:sysUpload:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(SysUpload sysUpload, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "文件"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SysUpload> page = sysUploadService.findPage(new Page<SysUpload>(request, response, -1), sysUpload);
    		new ExportExcel("文件", SysUpload.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出文件记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:sysUpload:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SysUpload> list = ei.getDataList(SysUpload.class);
			for (SysUpload sysUpload : list){
				try{
					sysUploadService.save(sysUpload);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条文件记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条文件记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入文件失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/sysUpload/?repage";
    }
	
	/**
	 * 下载导入文件数据模板
	 */
	@RequiresPermissions("sys:sysUpload:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "文件数据导入模板.xlsx";
    		List<SysUpload> list = Lists.newArrayList(); 
    		new ExportExcel("文件数据", SysUpload.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/sysUpload/?repage";
    }

}
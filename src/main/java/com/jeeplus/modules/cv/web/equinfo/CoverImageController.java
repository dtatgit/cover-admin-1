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
import com.jeeplus.modules.cv.entity.equinfo.CoverImage;
import com.jeeplus.modules.cv.service.equinfo.CoverImageService;

/**
 * 井盖图片信息Controller
 * @author crj
 * @version 2019-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/equinfo/coverImage")
public class CoverImageController extends BaseController {

	@Autowired
	private CoverImageService coverImageService;
	
	@ModelAttribute
	public CoverImage get(@RequestParam(required=false) String id) {
		CoverImage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverImageService.get(id);
		}
		if (entity == null){
			entity = new CoverImage();
		}
		return entity;
	}
	
	/**
	 * 井盖图片列表页面
	 */
	@RequiresPermissions("cv:equinfo:coverImage:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/equinfo/coverImageList";
	}
	
		/**
	 * 井盖图片列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverImage:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverImage coverImage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverImage> page = coverImageService.findPage(new Page<CoverImage>(request, response), coverImage); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖图片表单页面
	 */
	@RequiresPermissions(value={"cv:equinfo:coverImage:view","cv:equinfo:coverImage:add","cv:equinfo:coverImage:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverImage coverImage, Model model) {
		model.addAttribute("coverImage", coverImage);
		if(StringUtils.isBlank(coverImage.getId())){//如果ID是空为添加
			model.addAttribute("isAdd", true);
		}
		return "modules/cv/equinfo/coverImageForm";
	}

	/**
	 * 保存井盖图片
	 */
	@RequiresPermissions(value={"cv:equinfo:coverImage:add","cv:equinfo:coverImage:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CoverImage coverImage, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, coverImage)){
			return form(coverImage, model);
		}
		//新增或编辑表单保存
		coverImageService.save(coverImage);//保存
		addMessage(redirectAttributes, "保存井盖图片成功");
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverImage/?repage";
	}
	
	/**
	 * 删除井盖图片
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverImage:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverImage coverImage, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverImageService.delete(coverImage);
		j.setMsg("删除井盖图片成功");
		return j;
	}
	
	/**
	 * 批量删除井盖图片
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverImage:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverImageService.delete(coverImageService.get(id));
		}
		j.setMsg("删除井盖图片成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:coverImage:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverImage coverImage, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井盖图片"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverImage> page = coverImageService.findPage(new Page<CoverImage>(request, response, -1), coverImage);
    		new ExportExcel("井盖图片", CoverImage.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井盖图片记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:equinfo:coverImage:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverImage> list = ei.getDataList(CoverImage.class);
			for (CoverImage coverImage : list){
				try{
					coverImageService.save(coverImage);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井盖图片记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井盖图片记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井盖图片失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverImage/?repage";
    }
	
	/**
	 * 下载导入井盖图片数据模板
	 */
	@RequiresPermissions("cv:equinfo:coverImage:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井盖图片数据导入模板.xlsx";
    		List<CoverImage> list = Lists.newArrayList(); 
    		new ExportExcel("井盖图片数据", CoverImage.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/coverImage/?repage";
    }

}
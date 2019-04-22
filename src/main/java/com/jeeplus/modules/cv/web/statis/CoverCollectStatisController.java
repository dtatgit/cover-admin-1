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
import com.jeeplus.modules.cv.entity.statis.CoverCollectStatis;
import com.jeeplus.modules.cv.service.statis.CoverCollectStatisService;

/**
 * 窨井盖采集统计Controller
 * @author crj
 * @version 2019-04-22
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/statis/coverCollectStatis")
public class CoverCollectStatisController extends BaseController {

	@Autowired
	private CoverCollectStatisService coverCollectStatisService;
	
	@ModelAttribute
	public CoverCollectStatis get(@RequestParam(required=false) String id) {
		CoverCollectStatis entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverCollectStatisService.get(id);
		}
		if (entity == null){
			entity = new CoverCollectStatis();
		}
		return entity;
	}
	
	/**
	 * 窨井盖采集统计列表页面
	 */
	@RequiresPermissions("cv:statis:coverCollectStatis:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/statis/coverCollectStatisList";
	}
	
		/**
	 * 窨井盖采集统计列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:coverCollectStatis:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverCollectStatis coverCollectStatis, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverCollectStatis> page = coverCollectStatisService.findPage(new Page<CoverCollectStatis>(request, response), coverCollectStatis); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑窨井盖采集统计表单页面
	 */
	@RequiresPermissions(value={"cv:statis:coverCollectStatis:view","cv:statis:coverCollectStatis:add","cv:statis:coverCollectStatis:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverCollectStatis coverCollectStatis, Model model) {
		model.addAttribute("coverCollectStatis", coverCollectStatis);
		return "modules/cv/statis/coverCollectStatisForm";
	}

	/**
	 * 保存窨井盖采集统计
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:statis:coverCollectStatis:add","cv:statis:coverCollectStatis:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverCollectStatis coverCollectStatis, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverCollectStatis)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverCollectStatisService.save(coverCollectStatis);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存窨井盖采集统计成功");
		return j;
	}
	
	/**
	 * 删除窨井盖采集统计
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:coverCollectStatis:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverCollectStatis coverCollectStatis, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverCollectStatisService.delete(coverCollectStatis);
		j.setMsg("删除窨井盖采集统计成功");
		return j;
	}
	
	/**
	 * 批量删除窨井盖采集统计
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:coverCollectStatis:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverCollectStatisService.delete(coverCollectStatisService.get(id));
		}
		j.setMsg("删除窨井盖采集统计成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:statis:coverCollectStatis:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverCollectStatis coverCollectStatis, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "窨井盖采集统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverCollectStatis> page = coverCollectStatisService.findPage(new Page<CoverCollectStatis>(request, response, -1), coverCollectStatis);
    		new ExportExcel("窨井盖采集统计", CoverCollectStatis.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出窨井盖采集统计记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:statis:coverCollectStatis:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverCollectStatis> list = ei.getDataList(CoverCollectStatis.class);
			for (CoverCollectStatis coverCollectStatis : list){
				try{
					coverCollectStatisService.save(coverCollectStatis);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条窨井盖采集统计记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条窨井盖采集统计记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入窨井盖采集统计失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/statis/coverCollectStatis/?repage";
    }
	
	/**
	 * 下载导入窨井盖采集统计数据模板
	 */
	@RequiresPermissions("cv:statis:coverCollectStatis:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "窨井盖采集统计数据导入模板.xlsx";
    		List<CoverCollectStatis> list = Lists.newArrayList(); 
    		new ExportExcel("窨井盖采集统计数据", CoverCollectStatis.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/statis/coverCollectStatis/?repage";
    }

}
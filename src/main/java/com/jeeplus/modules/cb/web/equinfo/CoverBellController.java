/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.equinfo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.cb.entity.alarm.CoverBellAlarm;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;

/**
 * 井铃设备信息Controller
 * @author crj
 * @version 2019-06-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/equinfo/coverBell")
public class CoverBellController extends BaseController {

	@Autowired
	private CoverBellService coverBellService;

	
	@ModelAttribute
	public CoverBell get(@RequestParam(required=false) String id) {
		CoverBell entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverBellService.get(id);
		}
		if (entity == null){
			entity = new CoverBell();
		}
		return entity;
	}
	
	/**
	 * 井铃设备信息列表页面
	 */
	@RequiresPermissions("cb:equinfo:coverBell:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/equinfo/coverBellList";
	}
	
		/**
	 * 井铃设备信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverBell coverBell, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverBell> page = coverBellService.findPage(new Page<CoverBell>(request, response), coverBell); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井铃设备信息表单页面
	 */
	@RequiresPermissions(value={"cb:equinfo:coverBell:view","cb:equinfo:coverBell:add","cb:equinfo:coverBell:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverBell coverBell, Model model) {
		model.addAttribute("coverBell", coverBell);
		return "modules/cb/equinfo/coverBellForm";
	}

	/**
	 * 保存井铃设备信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"cb:equinfo:coverBell:add","cb:equinfo:coverBell:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverBell coverBell, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverBell)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//获取当前用户部门
		User user = UserUtils.getUser();
		if (org.apache.commons.lang3.StringUtils.isNotBlank(user.getId())){
			coverBell.setCreateOffice(user.getOffice().getId());
		}
		coverBellService.save(coverBell);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井铃设备信息成功");
		return j;
	}
	
	/**
	 * 删除井铃设备信息
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverBell coverBell, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverBellService.delete(coverBell);
		j.setMsg("删除井铃设备信息成功");
		return j;
	}
	
	/**
	 * 批量删除井铃设备信息
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverBellService.delete(coverBellService.get(id));
		}
		j.setMsg("删除井铃设备信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverBell coverBell, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井铃设备信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverBell> page = coverBellService.findPage(new Page<CoverBell>(request, response, -1), coverBell);
    		new ExportExcel("井铃设备信息", CoverBell.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井铃设备信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cb:equinfo:coverBell:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverBell> list = ei.getDataList(CoverBell.class);
			for (CoverBell coverBell : list){
				try{
					coverBellService.save(coverBell);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井铃设备信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井铃设备信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井铃设备信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/equinfo/coverBell/?repage";
    }
	
	/**
	 * 下载导入井铃设备信息数据模板
	 */
	@RequiresPermissions("cb:equinfo:coverBell:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井铃设备信息数据导入模板.xlsx";
    		List<CoverBell> list = Lists.newArrayList(); 
    		new ExportExcel("井铃设备信息数据", CoverBell.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/equinfo/coverBell/?repage";
    }
    //查看井铃报警记录
	@RequiresPermissions("cb:equinfo:coverBell:alarmlist")
	@RequestMapping(value = "alarmlist")
	public String alarmlist(CoverBell coverBell, Model model) {
		model.addAttribute("coverBell", coverBell);
		return "modules/cb/alarm/showAlarmList";
	}

	//查看井铃操作记录(coverBellOperation)
	@RequiresPermissions("cb:equinfo:coverBell:operationList")
	@RequestMapping(value = "operationList")
	public String operationList(CoverBell coverBell, Model model) {
		model.addAttribute("coverBell", coverBell);
		return "modules/cb/equinfo/showOperationList";
	}

	//查看井铃状态数据(coverBellState)
	@RequiresPermissions("cb:equinfo:coverBell:bellStateList")
	@RequestMapping(value = "bellStateList")
	public String bellStateList(CoverBell coverBell, Model model) {
		model.addAttribute("coverBell", coverBell);
		return "modules/cb/equinfo/showBellStateList";
	}

	/**
	 * 批量设防 add by 2019-07-29
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:defense")
	@RequestMapping(value = "fortify")
	public AjaxJson fortify(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String success="";
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Result result =coverBellService.setDefense(coverBellService.get(id), CodeConstant.DEFENSE_STATUS.FORTIFY);
			if(null!=result){
				success=result.getSuccess();
			}

		}
		if(StringUtils.isNotEmpty(success)&&success.equals("true")){
			j.setSuccess(true);
			j.setMsg("批量设防成功!");
		}else{
			j.setSuccess(false);
			j.setMsg("批量设防失败!");
		}

		return j;
	}

	/**
	 * 批量撤防 add by 2019-07-29
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:defense")
	@RequestMapping(value = "revoke")
	public AjaxJson revoke(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String success="";
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Result result =coverBellService.setDefense(coverBellService.get(id), CodeConstant.DEFENSE_STATUS.REVOKE);
			if(null!=result){
				success=result.getSuccess();
			}
		}
		if(StringUtils.isNotEmpty(success)&&success.equals("true")){
			j.setSuccess(true);
			j.setMsg("批量撤防成功!");
		}else{
			j.setSuccess(false);
			j.setMsg("批量撤防失败!");
		}

		return j;
	}

}
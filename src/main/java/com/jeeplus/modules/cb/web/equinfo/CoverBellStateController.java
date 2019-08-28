/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.equinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.api.pojo.DeviceStateResult;
import com.jeeplus.modules.api.service.DeviceService;
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
import com.jeeplus.modules.cb.entity.equinfo.CoverBellState;
import com.jeeplus.modules.cb.service.equinfo.CoverBellStateService;

/**
 * 井铃状态上报Controller
 * @author crj
 * @version 2019-06-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/equinfo/coverBellState")
public class CoverBellStateController extends BaseController {

	@Autowired
	private CoverBellStateService coverBellStateService;
	@Autowired
	private DeviceService deviceService;

	
	@ModelAttribute
	public CoverBellState get(@RequestParam(required=false) String id) {
		CoverBellState entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverBellStateService.get(id);
		}
		if (entity == null){
			entity = new CoverBellState();
		}
		return entity;
	}
	
	/**
	 * 井铃状态上报列表页面
	 */
	//@RequiresPermissions("cb:equinfo:coverBellState:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/equinfo/coverBellStateList";
	}
	
		/**
	 * 井铃状态上报列表数据
	 */
	@ResponseBody
	//@RequiresPermissions("cb:equinfo:coverBellState:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverBellState coverBellState, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverBellState> page = coverBellStateService.findPage(new Page<CoverBellState>(request, response), coverBellState); 
		return getBootstrapData(page);
	}

	/**
	 * 井卫状态上报列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "dataThird")
	public Map<String, Object> dataThird(DeviceStateResult deviceStateResult, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DeviceStateResult> page = new Page<DeviceStateResult>(request, response);
		Map map=new HashMap();
		map.put("devId",deviceStateResult.getDevId());
		map.put("pageNo",page.getPageNo());
		map.put("pageSize",page.getPageSize());
		page.setList(deviceService.getDeviceStateList(map));
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井铃状态上报表单页面
	 */
	@RequiresPermissions(value={"cb:equinfo:coverBellState:view","cb:equinfo:coverBellState:add","cb:equinfo:coverBellState:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverBellState coverBellState, Model model) {
		model.addAttribute("coverBellState", coverBellState);
		return "modules/cb/equinfo/coverBellStateForm";
	}

	/**
	 * 保存井铃状态上报
	 */
	@ResponseBody
	@RequiresPermissions(value={"cb:equinfo:coverBellState:add","cb:equinfo:coverBellState:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverBellState coverBellState, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverBellState)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverBellStateService.save(coverBellState);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井铃状态上报成功");
		return j;
	}
	
	/**
	 * 删除井铃状态上报
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBellState:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverBellState coverBellState, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverBellStateService.delete(coverBellState);
		j.setMsg("删除井铃状态上报成功");
		return j;
	}
	
	/**
	 * 批量删除井铃状态上报
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBellState:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverBellStateService.delete(coverBellStateService.get(id));
		}
		j.setMsg("删除井铃状态上报成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBellState:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverBellState coverBellState, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井铃状态上报"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverBellState> page = coverBellStateService.findPage(new Page<CoverBellState>(request, response, -1), coverBellState);
    		new ExportExcel("井铃状态上报", CoverBellState.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井铃状态上报记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cb:equinfo:coverBellState:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverBellState> list = ei.getDataList(CoverBellState.class);
			for (CoverBellState coverBellState : list){
				try{
					coverBellStateService.save(coverBellState);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井铃状态上报记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井铃状态上报记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井铃状态上报失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/equinfo/coverBellState/?repage";
    }
	
	/**
	 * 下载导入井铃状态上报数据模板
	 */
	@RequiresPermissions("cb:equinfo:coverBellState:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井铃状态上报数据导入模板.xlsx";
    		List<CoverBellState> list = Lists.newArrayList(); 
    		new ExportExcel("井铃状态上报数据", CoverBellState.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/equinfo/coverBellState/?repage";
    }

}
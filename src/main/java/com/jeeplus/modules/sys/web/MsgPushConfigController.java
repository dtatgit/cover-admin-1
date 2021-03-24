/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
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
import com.jeeplus.modules.sys.entity.MsgPushConfig;
import com.jeeplus.modules.sys.service.MsgPushConfigService;

/**
 * 消息推送配置Controller
 * @author crj
 * @version 2021-02-08
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/msgPushConfig")
public class MsgPushConfigController extends BaseController {

	@Autowired
	private MsgPushConfigService msgPushConfigService;
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public MsgPushConfig get(@RequestParam(required=false) String id) {
		MsgPushConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = msgPushConfigService.get(id);
		}
		if (entity == null){
			entity = new MsgPushConfig();
		}
		return entity;
	}
	
	/**
	 * 消息推送配置列表页面
	 */
	@RequiresPermissions("sys:msgPushConfig:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/sys/msg/msgPushConfigList";
	}
	
		/**
	 * 消息推送配置列表数据
	 */
	@ResponseBody
	@RequiresPermissions("sys:msgPushConfig:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(MsgPushConfig msgPushConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MsgPushConfig> page = msgPushConfigService.findPage(new Page<MsgPushConfig>(request, response), msgPushConfig); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑消息推送配置表单页面
	 */
	@RequiresPermissions(value={"sys:msgPushConfig:view","sys:msgPushConfig:add","sys:msgPushConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MsgPushConfig msgPushConfig, Model model) {
		model.addAttribute("msgPushConfig", msgPushConfig);
		return "modules/sys/msg/msgPushConfigForm";
	}

	/**
	 * 保存消息推送配置
	 */
	@ResponseBody
	@RequiresPermissions(value={"sys:msgPushConfig:add","sys:msgPushConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(MsgPushConfig msgPushConfig, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, msgPushConfig)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		User user = UserUtils.getUser();
		msgPushConfig.setCreateUserName(user.getName());
		//add by crj 新增通知部门id
		User noticePerson=systemService.getUser(msgPushConfig.getNoticePerson().getId());
		msgPushConfig.setNoticeOfficeId(noticePerson.getOffice().getId());
		msgPushConfigService.save(msgPushConfig);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存消息推送配置成功");
		return j;
	}
	
	/**
	 * 删除消息推送配置
	 */
	@ResponseBody
	@RequiresPermissions("sys:msgPushConfig:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(MsgPushConfig msgPushConfig, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		msgPushConfigService.delete(msgPushConfig);
		j.setMsg("删除消息推送配置成功");
		return j;
	}
	
	/**
	 * 批量删除消息推送配置
	 */
	@ResponseBody
	@RequiresPermissions("sys:msgPushConfig:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			msgPushConfigService.delete(msgPushConfigService.get(id));
		}
		j.setMsg("删除消息推送配置成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("sys:msgPushConfig:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(MsgPushConfig msgPushConfig, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "消息推送配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MsgPushConfig> page = msgPushConfigService.findPage(new Page<MsgPushConfig>(request, response, -1), msgPushConfig);
    		new ExportExcel("消息推送配置", MsgPushConfig.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出消息推送配置记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sys:msgPushConfig:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MsgPushConfig> list = ei.getDataList(MsgPushConfig.class);
			for (MsgPushConfig msgPushConfig : list){
				try{
					msgPushConfigService.save(msgPushConfig);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条消息推送配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条消息推送配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入消息推送配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/msgPushConfig/?repage";
    }
	
	/**
	 * 下载导入消息推送配置数据模板
	 */
	@RequiresPermissions("sys:msgPushConfig:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "消息推送配置数据导入模板.xlsx";
    		List<MsgPushConfig> list = Lists.newArrayList(); 
    		new ExportExcel("消息推送配置数据", MsgPushConfig.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sys/msgPushConfig/?repage";
    }

}
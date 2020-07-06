/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.web;

import com.alibaba.fastjson.JSONObject;
import com.antu.common.utils.JsonUtils;
import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import com.jeeplus.modules.device.entity.ServerUrl;
import com.jeeplus.modules.device.service.ServerUrlService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务器管理Controller
 * @author ffy
 * @version 2020-06-19
 */
@Controller
@RequestMapping(value = "${adminPath}/device/serverUrl")
public class ServerUrlController extends BaseController {

	private static final String DEVICEURL = Global.getConfig("coverBell.server.url"); //硬件平台api地址

	@Autowired
	private ServerUrlService serverUrlService;
	
	@ModelAttribute
	public ServerUrl get(@RequestParam(required=false) String id) {
		ServerUrl entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = serverUrlService.get(id);
		}
		if (entity == null){
			entity = new ServerUrl();
		}
		return entity;
	}
	
	/**
	 * 服务器列表页面
	 */
	@RequiresPermissions("device:serverUrl:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/device/serverUrlList";
	}
	
		/**
	 * 服务器列表数据
	 */
	@ResponseBody
	@RequiresPermissions("device:serverUrl:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ServerUrl serverUrl, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
//		Page<ServerUrl> page = serverUrlService.findPage(new Page<ServerUrl>(request, response), serverUrl);
//		return getBootstrapData(page);


		Map<String, Object> mapTemp = new HashMap<String, Object>();
		mapTemp.put("rows", null);
		mapTemp.put("total", 0);


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", serverUrl.getName());
		map.put("pageNo",request.getParameter("pageNo"));
		map.put("pageSize",request.getParameter("pageSize"));

		String deviceUrl = DEVICEURL + "/device/serverUrlListByPage";

		String str = HttpClientUtil.doPost(deviceUrl,map);
		logger.info("str--------------------->:"+str);

		if(StringUtils.isNotBlank(str)){
			Map<String, Object> resultMap = JsonUtils.parseMap(str);
			Boolean s = (Boolean) resultMap.get("success");
			if(s){
				Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");

				mapTemp.put("rows", dataMap.get("list"));
				mapTemp.put("total", dataMap.get("total"));
			}
		}

		return mapTemp;
	}

	/**
	 * 查看，增加，编辑服务器表单页面
	 */
	@RequiresPermissions(value={"device:serverUrl:view","device:serverUrl:add","device:serverUrl:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ServerUrl serverUrl, Model model) throws IOException {

		if(StringUtils.isNotBlank(serverUrl.getId())){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", serverUrl.getId());

			String deviceUrl = DEVICEURL + "/device/getServerUrl";
			String str = HttpClientUtil.doPost(deviceUrl,map);
			logger.info("str--------------------->:"+str);

			if(StringUtils.isNotBlank(str)){
				JSONObject jsonObject =JSONObject.parseObject(str);
				Boolean s = jsonObject.getBoolean("success");
				if(s){
					String tt = jsonObject.getString("data");
					//logger.info("tt:"+tt);
					serverUrl = JsonUtils.parse(tt,ServerUrl.class);
				}
			}
		}
		model.addAttribute("serverUrl", serverUrl);
		return "modules/device/serverUrlForm";
	}

	/**
	 * 保存服务器
	 */
	@ResponseBody
	@RequiresPermissions(value={"device:serverUrl:add","device:serverUrl:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ServerUrl serverUrl, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, serverUrl)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//serverUrlService.save(serverUrl);//新建或者编辑保存


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", serverUrl.getName());
		map.put("url",serverUrl.getUrl());

		String deviceUrl = DEVICEURL +"/device/saveServerUrl";
		if(StringUtils.isNotBlank(serverUrl.getId())){
			deviceUrl = DEVICEURL+ "/device/updateServerUrl";

			map.put("id",serverUrl.getId());
		}

		String str = HttpClientUtil.doPost(deviceUrl,map);
		logger.info("str--------------------->:"+str);

		j.setSuccess(true);
		j.setMsg("保存服务器成功");
		return j;
	}
	
	/**
	 * 删除服务器
	 */
	@ResponseBody
	@RequiresPermissions("device:serverUrl:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ServerUrl serverUrl, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		//serverUrlService.delete(serverUrl);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", serverUrl.getId());

		String deviceUrl = DEVICEURL + "/device/deleteServerUrl";

		String str = HttpClientUtil.doPost(deviceUrl,map);
		logger.info("str--------------------->:"+str);

		j.setMsg("删除服务器成功");
		return j;
	}
	
	/**
	 * 批量删除服务器
	 */
	@ResponseBody
	@RequiresPermissions("device:serverUrl:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);

			String deviceUrl = DEVICEURL + "/device/deleteServerUrl";

			String str = HttpClientUtil.doPost(deviceUrl,map);
			logger.info("str--------------------->:"+str);

			//serverUrlService.delete(serverUrlService.get(id));
		}
		j.setMsg("删除服务器成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("device:serverUrl:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(ServerUrl serverUrl, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "服务器"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ServerUrl> page = serverUrlService.findPage(new Page<ServerUrl>(request, response, -1), serverUrl);
    		new ExportExcel("服务器", ServerUrl.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出服务器记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("device:serverUrl:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ServerUrl> list = ei.getDataList(ServerUrl.class);
			for (ServerUrl serverUrl : list){
				try{
					serverUrlService.save(serverUrl);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条服务器记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条服务器记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入服务器失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/serverUrl/?repage";
    }
	
	/**
	 * 下载导入服务器数据模板
	 */
	@RequiresPermissions("device:serverUrl:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "服务器数据导入模板.xlsx";
    		List<ServerUrl> list = Lists.newArrayList(); 
    		new ExportExcel("服务器数据", ServerUrl.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/serverUrl/?repage";
    }

}
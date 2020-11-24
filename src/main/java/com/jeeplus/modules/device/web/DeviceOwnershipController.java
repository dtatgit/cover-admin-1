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
import com.jeeplus.modules.api.AppResult;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import com.jeeplus.modules.device.entity.DeviceOwnership;
import com.jeeplus.modules.device.entity.ServerUrl;
import com.jeeplus.modules.device.service.DeviceOwnershipService;
import com.jeeplus.modules.device.service.ServerUrlService;
import com.jeeplus.modules.device.vo.DeviceOwnershipVo;
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
import java.util.stream.Collectors;

/**
 * 设备归属管理Controller
 * @author ffy
 * @version 2020-06-19
 */
@Controller
@RequestMapping(value = "${adminPath}/device/deviceOwnership")
public class DeviceOwnershipController extends BaseController {

	private static final String DEVICEURL = Global.getConfig("coverBell.server.url"); //硬件平台api地址

	@Autowired
	private DeviceOwnershipService deviceOwnershipService;
	@Autowired
	private ServerUrlService serverUrlService;
	
	@ModelAttribute
	public DeviceOwnership get(@RequestParam(required=false) String id) {
		DeviceOwnership entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = deviceOwnershipService.get(id);
		}
		if (entity == null){
			entity = new DeviceOwnership();
		}
		return entity;
	}
	
	/**
	 * 设备归属列表页面
	 */
	@RequiresPermissions("device:deviceOwnership:list")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) throws IOException {

		List<ServerUrl> list = Lists.newArrayList();

		String deviceUrl = DEVICEURL + "/device/serverUrlListAll";
		String str = HttpClientUtil.doPost(deviceUrl,new HashMap());
		if(StringUtils.isNotBlank(str)){
			JSONObject jsonObject =JSONObject.parseObject(str);
			Boolean s = jsonObject.getBoolean("success");
			if(s){
				String tt = jsonObject.getString("data");
				list = JsonUtils.parseList(tt,ServerUrl.class);
			}
		}
		model.addAttribute("serverList",list);




		//测试
		List<String> listt = Lists.newArrayList();// Arrays.asList("BT191105000073","BT1911050000731","BT191105000073r","BT200608000103","BT191024000060");
		listt.add("BT191105000073");
		listt.add("BT1911050000731");
		listt.add("BT191105000073r");
		listt.add("BT200608000103");
		listt.add("BT191024000060");

		Map<String,Object> map = new HashMap<>();
		map.put("list",listt);
		deviceUrl = DEVICEURL + "/device/filterAlarmDevice";
		String tt = HttpClientUtil.doPost(deviceUrl,map);
		System.out.println("tt======================>:"+tt);




		return "modules/device/deviceOwnershipList";
	}
	
		/**
	 * 设备归属列表数据
	 */
	@ResponseBody
	@RequiresPermissions("device:deviceOwnership:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DeviceOwnership deviceOwnership, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

		Map<String, Object> mapTemp = new HashMap<String, Object>();
		mapTemp.put("rows", null);
		mapTemp.put("total", 0);


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("devId", deviceOwnership.getDevId());
		map.put("dtype", deviceOwnership.getDtype());
		map.put("serverUrlId", deviceOwnership.getServerUrlId());
		map.put("pageNo",request.getParameter("pageNo"));
		map.put("pageSize",request.getParameter("pageSize"));

		String deviceUrl = DEVICEURL + "/device/deviceOwnershipListByPage";

		String str = HttpClientUtil.doPost(deviceUrl,map);
		logger.info("str--------------------->:"+str);

		if(StringUtils.isNotBlank(str)){
			JSONObject jsonObject =JSONObject.parseObject(str);
			Boolean s = jsonObject.getBoolean("success");
			if(s){
				String tt = jsonObject.getString("data");
				logger.info("tt:"+tt);
				Map<String, Object> dataMap = JsonUtils.parseMap(tt);

				mapTemp.put("rows", dataMap.get("list"));
				mapTemp.put("total", dataMap.get("total"));
			}
		}

//		if(StringUtils.isNotBlank(str)){
//			Map<String, Object> resultMap = JsonUtils.parseMap(str);
//			Boolean s = (Boolean) resultMap.get("success");
//			if(s){
//				Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
//
//				mapTemp.put("rows", dataMap.get("list"));
//				mapTemp.put("total", dataMap.get("total"));
//			}
//		}

//		Page<DeviceOwnership> page = deviceOwnershipService.findPage(new Page<DeviceOwnership>(request, response), deviceOwnership);
//		return getBootstrapData(page);

		return mapTemp;
	}

	/**
	 * 查看，增加，编辑设备归属表单页面
	 */
	@RequiresPermissions(value={"device:deviceOwnership:view","device:deviceOwnership:add","device:deviceOwnership:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DeviceOwnership deviceOwnership, Model model) throws IOException {

		List<ServerUrl> list = Lists.newArrayList();

		String deviceUrl = DEVICEURL + "/device/serverUrlListAll";
		String str = HttpClientUtil.doPost(deviceUrl,new HashMap());
		if(StringUtils.isNotBlank(str)){
			JSONObject jsonObject =JSONObject.parseObject(str);
			Boolean s = jsonObject.getBoolean("success");
			if(s){
				String tt = jsonObject.getString("data");
				//logger.info("tt:"+tt);
				list = JsonUtils.parseList(tt,ServerUrl.class);
			}
		}

		if(StringUtils.isNotBlank(deviceOwnership.getId())){
			//修改
			deviceUrl = DEVICEURL + "/device/getDeviceOwnership";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", deviceOwnership.getId());
			String str1 = HttpClientUtil.doPost(deviceUrl,map);
			logger.info("str1:"+str1);
			JSONObject jsonObject =JSONObject.parseObject(str1);
			Boolean s = jsonObject.getBoolean("success");
			if(s){
				String tt = jsonObject.getString("data");
				deviceOwnership = JsonUtils.parse(tt,DeviceOwnership.class);
			}
		}


		model.addAttribute("deviceOwnership", deviceOwnership);
		model.addAttribute("serverList",list);
		return "modules/device/deviceOwnershipForm";
	}

	@RequestMapping(value = "importForm")
	public String importForm(Model model) throws IOException {

		List<ServerUrl> list = Lists.newArrayList();

		String deviceUrl = DEVICEURL + "/device/serverUrlListAll";
		String str = HttpClientUtil.doPost(deviceUrl,new HashMap());
		if(StringUtils.isNotBlank(str)){
			JSONObject jsonObject =JSONObject.parseObject(str);
			Boolean s = jsonObject.getBoolean("success");
			if(s){
				String tt = jsonObject.getString("data");
				//logger.info("tt:"+tt);
				list = JsonUtils.parseList(tt,ServerUrl.class);
			}
		}
		model.addAttribute("serverList",list);
		model.addAttribute("interfaceUrl",DEVICEURL+"/device/importFile");
		return "modules/device/deviceOwnershipImportForm";
	}

	/**
	 * 保存设备归属
	 */
	@ResponseBody
	@RequiresPermissions(value={"device:deviceOwnership:add","device:deviceOwnership:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(DeviceOwnership deviceOwnership, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, deviceOwnership)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//deviceOwnershipService.save(deviceOwnership);//新建或者编辑保存

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("devId", deviceOwnership.getDevId());
		map.put("dtype", deviceOwnership.getDtype());
		map.put("serverUrlId", deviceOwnership.getServerUrlId());

		String deviceUrl = DEVICEURL + "/device/addDeviceOwnership";

		if(StringUtils.isNotBlank(deviceOwnership.getId())){

			deviceUrl = DEVICEURL + "/device/updateDeviceOwnership";
			map.put("id",deviceOwnership.getId());
		}
		String str = HttpClientUtil.doPost(deviceUrl,map);
		logger.info("str--------------------->:"+str);

		if(StringUtils.isNotBlank(str)){
			JSONObject jsonObject =JSONObject.parseObject(str);
			Boolean s = jsonObject.getBoolean("success");
			if(s){
				j.setMsg("保存设备归属成功");
			}else {
				j.setSuccess(false);
				j.setMsg("保存设备归属失败");
			}
		}else{
			j.setSuccess(false);
			j.setMsg("接口调用异常");
		}

		return j;
	}
	
	/**
	 * 删除设备归属
	 */
	@ResponseBody
	@RequiresPermissions("device:deviceOwnership:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(DeviceOwnership deviceOwnership, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		//deviceOwnershipService.delete(deviceOwnership);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", deviceOwnership.getId());

		String deviceUrl = DEVICEURL + "/device/deleteDeviceOwnership";

		String str = HttpClientUtil.doPost(deviceUrl,map);
		logger.info("str--------------------->:"+str);

		if(StringUtils.isNotBlank(str)){
			JSONObject jsonObject =JSONObject.parseObject(str);
			Boolean s = jsonObject.getBoolean("success");
			if(s){
				j.setMsg("删除成功");
			}else {
				j.setSuccess(false);
				j.setMsg("删除失败");
			}
		}else{
			j.setSuccess(false);
			j.setMsg("接口调用异常");
		}

		return j;
	}
	
	/**
	 * 批量删除设备归属
	 */
	@ResponseBody
	@RequiresPermissions("device:deviceOwnership:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			//deviceOwnershipService.delete(deviceOwnershipService.get(id));

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);

			String deviceUrl = DEVICEURL + "/device/deleteDeviceOwnership";

			String str = HttpClientUtil.doPost(deviceUrl,map);
			logger.info("str--------------------->:"+str);
		}
		j.setMsg("删除设备归属成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("device:deviceOwnership:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(DeviceOwnership deviceOwnership, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "设备归属"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DeviceOwnership> page = deviceOwnershipService.findPage(new Page<DeviceOwnership>(request, response, -1), deviceOwnership);
    		new ExportExcel("设备归属", DeviceOwnership.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出设备归属记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	@ResponseBody
	@RequestMapping(value = "/importFile")
	public AppResult importFile(@RequestParam("file") MultipartFile file,
								@RequestParam(value = "dtype", required = true) String dtype,
								@RequestParam(value = "serverUrlId", required = true) String serverUrlId,
								HttpServletRequest request, HttpServletResponse response) throws IOException {

		AppResult j = new AppResult();

		StringBuilder sb = new StringBuilder();
		if (!file.isEmpty()) {

			String fileName = file.getOriginalFilename();
			if(!fileName.endsWith("xls") && !fileName.endsWith("xlsx")){
				j.setMsg("请上传excel文件");
				j.setSuccess(false);
				return j;
			}

			try {

				ImportExcel ei = new ImportExcel(file, 1, 0);
				List<DeviceOwnership> list = ei.getDataList(DeviceOwnership.class);
				if(list!=null && list.size()>0){
					List<String> collect = list.stream().map(item -> {
						return item.getDevId();
					}).collect(Collectors.toList());

					DeviceOwnershipVo vo = new DeviceOwnershipVo();
					vo.setDevIds(collect);
					vo.setDtype(dtype);
					vo.setServerUrlId(serverUrlId);

					String deviceUrl = DEVICEURL + "/device/importFile2";

					String str = HttpClientUtil.post2(deviceUrl,vo);
					logger.info("str--------------------->:"+str);

					if(StringUtils.isNotBlank(str)){
						JSONObject jsonObject =JSONObject.parseObject(str);
						Boolean s = jsonObject.getBoolean("success");
						if(s){
							j.setMsg("批量导入成功");
						}else {
							j.setSuccess(false);
							j.setMsg("批量导入异常");
						}
					}else{
						j.setSuccess(false);
						j.setMsg("接口调用异常");
					}

				}else{
					j.setMsg("编号为空");
					j.setSuccess(false);
					return j;
				}

			} catch (Exception e) {
				j.setMsg("出现异常，请联系开发商。"+e.getMessage());
				j.setSuccess(false);
				return j;
			}

		}
		return j;
	}


	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("device:deviceOwnership:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DeviceOwnership> list = ei.getDataList(DeviceOwnership.class);
			for (DeviceOwnership deviceOwnership : list){
				try{
					deviceOwnershipService.save(deviceOwnership);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条设备归属记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条设备归属记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入设备归属失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/deviceOwnership/?repage";
    }
	
	/**
	 * 下载导入设备归属数据模板
	 */
	@RequiresPermissions("device:deviceOwnership:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "设备归属数据导入模板.xlsx";
    		List<DeviceOwnership> list = Lists.newArrayList(); 
    		new ExportExcel("设备归属数据", DeviceOwnership.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/device/deviceOwnership/?repage";
    }

}
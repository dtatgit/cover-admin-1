/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.equinfo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.api.pojo.AlarmDevice;
import com.jeeplus.modules.api.pojo.DeviceParameterResult;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.service.DeviceParameterService;
import com.jeeplus.modules.api.service.DeviceService;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.service.equinfo.CoverBellOperationService;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 井铃设备信息Controller
 * @author crj
 * @version 2019-06-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/equinfo/coverBell")
public class CoverBellController extends BaseController {

	@Autowired
	 CoverBellService coverBellService;
	@Autowired
	private DeviceParameterService deviceParameterService;
	@Autowired
	private CoverBellOperationService coverBellOperationService;

	@Autowired
	 CoverService coverService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private CoverWorkService coverWorkService;

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
	 * 查看，增加，编辑井铃设备信息表单页面
	 */
	@RequiresPermissions(value={"cb:equinfo:coverBell:view","cb:equinfo:coverBell:add","cb:equinfo:coverBell:edit"},logical=Logical.OR)
	@RequestMapping(value = "view")
	public String view(CoverBell coverBell, Model model) {
		CoverBell bell=coverBellService.get(coverBell.getId());
		if(StringUtils.isNotEmpty(bell.getCoverId())){
			bell.setCover(coverService.get(bell.getCoverId()));
		}
		model.addAttribute("coverBell", coverBell);
		return "modules/cb/equinfo/coverBellDetail";
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
		StringBuilder  sb = new StringBuilder(); //记录操作失败的编号
		for(String id : idArray){
			CoverBell bell=coverBellService.get(id);
			Result result =coverBellService.setDefense(bell, CodeConstant.DEFENSE_STATUS.FORTIFY);
			if(null!=result){
				success=result.getSuccess();
			}
			if(StringUtils.isNotEmpty(success)&&success.equals("true")){
				coverBellOperationService.genRecord(CodeConstant.operation_type.fortify,bell.getBellNo() );
			}else{
				sb.append(bell.getBellNo()).append(":").append(result.getMsg()).append("<br/>");
			}

		}
		//System.out.println("sb:"+sb.toString());
		if(StringUtils.isNotEmpty(success)&&success.equals("true")){
			j.setSuccess(true);
			j.setMsg("批量设防成功!");
		}else{
			j.setSuccess(false);
			if(StringUtils.isNotBlank(sb.toString())){
				j.setMsg(sb.toString());
			}else{
				j.setMsg("批量设防失败!");
			}
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
		StringBuilder  sb = new StringBuilder(); //记录操作失败的编号
		for(String id : idArray){
			CoverBell bell=coverBellService.get(id);
			Result result =coverBellService.setDefense(bell, CodeConstant.DEFENSE_STATUS.REVOKE);
			if(null!=result){
				success=result.getSuccess();
			}
			if(StringUtils.isNotEmpty(success)&&success.equals("true")){
				coverBellOperationService.genRecord(CodeConstant.operation_type.revoke,bell.getBellNo() );
			}else{
				sb.append(bell.getBellNo()).append(":").append(result.getMsg()).append("<br/>");
			}
		}
		if(StringUtils.isNotEmpty(success)&&success.equals("true")){
			j.setSuccess(true);
			j.setMsg("批量撤防成功!");

		}else{
			j.setSuccess(false);
			if(StringUtils.isNotBlank(sb.toString())){
				j.setMsg(sb.toString());
			}else{
				j.setMsg("批量撤防失败!");
			}
		}

		return j;
	}

	/**
	 *批量报废操作
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:scrap")
	@RequestMapping(value = "scrap")
	public AjaxJson scrap(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String success="";
		String idArray[] =ids.split(",");
		for(String id : idArray){
			CoverBell bell=coverBellService.get(id);
			//调用设备报废接口
			Result result =coverBellService.setScrap(bell, CodeConstant.DEFENSE_STATUS.REVOKE);
		if(null!=result){
				success=result.getSuccess();
			}
			logger.info("************井卫报废中*****************");
			if(StringUtils.isNotEmpty(success)&&success.equals("true")){
				coverBellOperationService.genRecord(CodeConstant.operation_type.scrap,bell.getBellNo() );
				//add by 2019-10-24井卫报废之后需要维护井盖的安装
				Cover cover=null;
				if(StringUtils.isNotEmpty(bell.getCoverId())){
					cover=coverService.get(bell.getCoverId());
				}
				if(null!=cover){
					//add by 2019-10-24废弃后，井盖中安装工单状态为未安装
					cover.setIsGwo(CodeConstant.cover_gwo.not_install);
					coverService.save(cover);
				}

			}
		}
		if(StringUtils.isNotEmpty(success)&&success.equals("true")){
			j.setSuccess(true);
			j.setMsg("批量报废成功!");

		}else{
			j.setSuccess(false);
			j.setMsg("批量报废失败!");
		}

		return j;
	}

	/**
	 * 跳转到参数设置页面
	 * @param deviceId
	 * @param model
	 * @return
	 */
	@RequiresPermissions("cb:equinfo:coverBell:toSetParam")
	@RequestMapping(value = "toSetParam")
	public String toSetParam(String deviceId, Model model, HttpServletRequest request) throws InterruptedException {
		CoverBell coverBell=coverBellService.get(deviceId);
		DeviceParameterResult deviceParameterResult=deviceParameterService.getDeviceParameter(coverBell.getBellNo());
		if(deviceParameterResult==null){
			deviceParameterResult = new DeviceParameterResult(coverBell.getBellNo(),-1,-1);
		}
//		if(null!=deviceParameterResult){
//			model.addAttribute("deviceParameterResult", deviceParameterResult);
//			return "modules/cb/equinfo/coverBellParameterResult";
//		}
//		return "error/400";
		model.addAttribute("deviceParameterResult", deviceParameterResult);
		return "modules/cb/equinfo/coverBellParameterResult";
	}
	
	/**
	 * 修改设备参数
	 * @param deviceParameterResult
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:toSetParam")
	@RequestMapping(value = "setParam")
	public AjaxJson setParam(DeviceParameterResult deviceParameterResult){
		AjaxJson j = new AjaxJson();
		Result result = deviceParameterService.setDeviceParameter(deviceParameterResult);
		String msg="";
		if(result.getSuccess().equals("true")){
			j.setSuccess(true);
			msg="设置参数信息成功！";
		}else{
			j.setSuccess(false);
			msg= result.getMsg();
		}


		j.setMsg(msg);
		return j;
	}

	/**
	 *批量解绑操作
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:untying")
	@RequestMapping(value = "untying")
	public AjaxJson untying(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		boolean success=false;
		String idArray[] =ids.split(",");
		for(String id : idArray){
			CoverBell bell=coverBellService.get(id);
			boolean flag=coverBellService.untying(bell);
			if(flag){
				success=true;
			}
		}
		if(success){
			j.setSuccess(true);
			j.setMsg("批量解绑成功!");

		}else{
			j.setSuccess(false);
			j.setMsg("批量解绑失败!");
		}

		return j;
	}

	/**
	 * 根据硬件接口获取报警井卫打点
	 * 返回报警数据到地图上
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "mapAlarmdata")
	public AjaxJson mapAlarmdata(HttpServletRequest request, HttpServletResponse response, Model model) {

		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		AjaxJson j = new AjaxJson();

		List<AlarmDevice> alarmDeviceList = deviceService.getAlarmDeviceList();
		if(null!=alarmDeviceList&&alarmDeviceList.size()>0){
			for(AlarmDevice device:alarmDeviceList){
				Map<String,Object> resp = new HashMap<String,Object>();
				Cover cv=null;
				System.out.println("*************:"+device.getDevNo());
				String devNo=device.getDevNo();//井卫编号
				CoverBell coverBell=coverBellService.findUniqueByProperty("bell_no", devNo);
				if(null!=coverBell&&StringUtils.isNotEmpty(coverBell.getCoverId())){
					cv=coverService.get(coverBell.getCoverId());
				}

				if(null!=cv){
					resp.put("coverId", cv.getId());
					//resp.put("alarmId", bellAlarm.getId());
					//resp.put("alarmNum", bellAlarm.getAlarmNum());
/*					String alarmTypeName= DictUtils.getDictLabel(bellAlarm.getAlarmType(),"alarm_type", "--");
					resp.put("alarmTypeName", alarmTypeName);*/
					resp.put("bellId", coverBell.getId());
					resp.put("bellNo", devNo);
					resp.put("lng",cv.getLongitude());
					resp.put("lat",cv.getLatitude());
					resp.put("no",cv.getNo());
					resp.put("address",cv.getAddressDetail());
					list.add(resp);
				}
				if(list.size()>200)
				{
					break;
				}

			}
		}
		//报警数据取出经纬度，返回前台数据：井卫编号，井盖编号，井盖详细地址
		if(list==null||list.size()<=0){
			j.setSuccess(false);
		}
		j.setData(list);
		return j;
	}

	@RequestMapping(value = "ajaxAlarmData", method = RequestMethod.POST)
	public void ajaxAlarmData(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter printWriter = null;
		//List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>(5);
		try {
			List<AlarmDevice> alarmDeviceList = deviceService.getAlarmDeviceList();
			Integer alarmNum=0;
			if(null!=alarmDeviceList&&alarmDeviceList.size()>0){
				for(AlarmDevice device:alarmDeviceList){
					String devNo=device.getDevNo();//井卫编号
					Cover cv=null;
					CoverBell coverBell=coverBellService.findUniqueByProperty("bell_no", devNo);
					if(null!=coverBell&&StringUtils.isNotEmpty(coverBell.getCoverId())){
						cv=coverService.get(coverBell.getCoverId());
					}
					if(null!=cv){
						alarmNum=alarmNum+1;
					}
				}
			}
/*
			if(null!=alarmDeviceList){
				 alarmNum=alarmDeviceList.size();
			}*/

			Map<String, Object> map= new HashMap<String, Object>();
			map.put("alarmNum",alarmNum);
			//datas.add(map);


			String jsonResult = JSON.toJSONString(map);
			printWriter = response.getWriter();
			printWriter.print(jsonResult);
		} catch (IOException ex) {
			//Logger.getLogger(HelloController.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (null != printWriter) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "createWork")
	public AjaxJson createWork(String id, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
	boolean flag=coverWorkService.queryCoverWork(id, CodeConstant.WORK_TYPE.ALARM);
	if(flag){
		j.setSuccess(false);
		j.setMsg("已经生成工单，无法重复生成!");
	}else{
		coverWorkService.createWorkByBell(coverBellService.get(id));
		j.setSuccess(true);
		j.setMsg("生成工单完成!");
	}

		return j;
	}

}
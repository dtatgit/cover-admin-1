/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.equinfo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
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
import com.jeeplus.modules.api.vo.ParamResVo;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.service.equinfo.CoverBellOperationService;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.projectInfo.entity.ProjectInfo;
import com.jeeplus.modules.projectInfo.service.ProjectInfoService;
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
 * 井卫设备信息Controller
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
	@Autowired
	private ProjectInfoService projectInfoService;

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
	 * 井卫设备信息列表页面
	 */
	@RequiresPermissions("cb:equinfo:coverBell:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/equinfo/coverBellList";
	}
	
		/**
	 * 井卫设备信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverBell coverBell, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverBell> page = coverBellService.findPage(new Page<CoverBell>(request, response), coverBell); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井卫设备信息表单页面
	 */
	@RequiresPermissions(value={"cb:equinfo:coverBell:view","cb:equinfo:coverBell:add","cb:equinfo:coverBell:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverBell coverBell, Model model) {
		model.addAttribute("coverBell", coverBell);
		return "modules/cb/equinfo/coverBellForm";
	}

	/**
	 * 查看，增加，编辑井卫设备信息表单页面
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
	 * 保存井卫设备信息
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
		//关联项目信息
		coverBell.setProjectId(user.getOffice().getProjectId());
		coverBell.setProjectName(user.getOffice().getProjectName());
		coverBellService.save(coverBell);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井卫设备信息成功");
		return j;
	}
	
	/**
	 * 删除井卫设备信息
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverBell coverBell, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		CoverBell oldBell=coverBellService.get(coverBell.getId());
		oldBell.setDelFlag(CoverBell.DEL_FLAG_DELETE);
		coverBellService.save(oldBell);
		//coverBellService.delete(coverBell);
		j.setMsg("删除井卫设备信息成功");
		return j;
	}
	
	/**
	 * 批量删除井卫设备信息
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			CoverBell oldBell=coverBellService.get(id);
			oldBell.setDelFlag(CoverBell.DEL_FLAG_DELETE);
			coverBellService.save(oldBell);
			//coverBellService.delete(coverBellService.get(id));
		}
		j.setMsg("删除井卫设备信息成功");
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
            String fileName = "井卫设备信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            //Page<CoverBell> page = coverBellService.findPage(new Page<CoverBell>(request, response, -1), coverBell);
			List<CoverBell> coverBelllist=coverBellService.findList(coverBell);
    		new ExportExcel("井卫设备信息", CoverBell.class).setDataList(coverBelllist).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井卫设备信息记录失败！失败信息："+e.getMessage());
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
					String bellNo=coverBell.getBellNo().trim();// 井卫编号
					String imei=coverBell.getImei().trim();// IMEI号
					CoverBell bell=new CoverBell();
					bell.setBellNo(bellNo);
					bell.setImei(imei);
					List<CoverBell> bellList=coverBellService.checkFindList(bell);
					CoverBell oldBell=null;
					if(null!=bellList&&bellList.size()>0){//已经存在，需要更新
						oldBell=bellList.get(0);
						oldBell.setIsNewRecord(false);
						if(StringUtils.isNotEmpty(coverBell.getBellModel())){
							oldBell.setBellModel(coverBell.getBellModel());
						}
						if(StringUtils.isNotEmpty(coverBell.getImei())){
							oldBell.setImei(coverBell.getImei());
						}
						if(StringUtils.isNotEmpty(coverBell.getSim())){
							oldBell.setSim(coverBell.getSim());
						}
					}else{//不存在，新增
						oldBell=coverBell;
						oldBell.setIsNewRecord(true);
						oldBell.setId(IdGen.uuid());
                        oldBell.setWorkStatus(CodeConstant.BELL_WORK_STATUS.INIT);//默认给初始状态
                        oldBell.setBellStatus(CodeConstant.BELL_STATUS.init);// 生命周期
					}

					String projectNo=coverBell.getProjectName();//所属项目编号
					if(StringUtils.isNotEmpty(projectNo)){
						ProjectInfo projectInfo=new ProjectInfo();
						projectInfo.setProjectNo(projectNo);
						List<ProjectInfo> projectList=projectInfoService.findList(projectInfo);
						if(null!=projectList&&projectList.size()>0){
							oldBell.setProjectId(projectList.get(0).getId());
							oldBell.setProjectName(projectList.get(0).getProjectName());
						}else{
							oldBell.setProjectName("");
						}
					}else{
						oldBell.setProjectId("");
						oldBell.setProjectName("");
					}
                    //补充其余参数信息
                    oldBell=coverBellService.processCoverBellExt(bellNo,oldBell) ;
					coverBellService.save(oldBell);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井卫设备信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井卫设备信息记录"+failureMsg);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导入井卫设备信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/equinfo/coverBell/?repage";
    }
	
	/**
	 * 下载导入井卫设备信息数据模板
	 */
	@RequiresPermissions("cb:equinfo:coverBell:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井卫设备信息数据导入模板.xlsx";
    		List<CoverBell> list = Lists.newArrayList(); 
    		new ExportExcel("井卫设备信息数据", CoverBell.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/equinfo/coverBell/?repage";
    }
    //查看井卫报警记录
	@RequiresPermissions("cb:equinfo:coverBell:alarmlist")
	@RequestMapping(value = "alarmlist")
	public String alarmlist(CoverBell coverBell, Model model) {
		model.addAttribute("coverBell", coverBell);
		return "modules/cb/alarm/showAlarmList";
	}

	//查看井卫操作记录(coverBellOperation)
	@RequiresPermissions("cb:equinfo:coverBell:operationList")
	@RequestMapping(value = "operationList")
	public String operationList(CoverBell coverBell, Model model) {
		model.addAttribute("coverBell", coverBell);
		return "modules/cb/equinfo/showOperationList";
	}

	//查看井卫状态数据(coverBellState)
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
		StringBuilder sb = new StringBuilder();
		for(String id : idArray) {
			try {
				CoverBell bell = coverBellService.get(id);
				if (bell != null) {
					//未安装的才能报废
					if (bell.getBellStatus().equals(CodeConstant.BELL_STATUS.notinstalled)) {
						//修改状态
						coverBellService.updateState(bell.getId(), CodeConstant.BELL_STATUS.scrap);

						//调用设备报废接口
						deviceService.deviceScrap(bell.getBellNo());

						//操作记录
						coverBellOperationService.genRecord(CodeConstant.operation_type.scrap, bell.getBellNo());

					} else {
						sb.append(bell.getBellNo() + "<br/>");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String msg = "操作成功";
		if(StringUtils.isNotBlank(sb.toString())){
			msg = "编号：<br/>"+sb.toString()+"不能废弃！！";
		}

		j.setMsg(msg);
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
		//DeviceParameterResult deviceParameterResult=deviceParameterService.getDeviceParameter(coverBell.getBellNo());
//		if(deviceParameterResult==null){
//			deviceParameterResult = new DeviceParameterResult(coverBell.getBellNo(),-1,-1,3,-1,"-1","-1",-1);
//
//		}

//		if(null!=deviceParameterResult){
//			model.addAttribute("deviceParameterResult", deviceParameterResult);
//			return "modules/c
//			b/equinfo/coverBellParameterResult";
//		}
//		return "error/400";
		//model.addAttribute("deviceParameterResult", deviceParameterResult);


		//最新获取公共参数信息 2-20 add by ffy
		List<ParamResVo> paramList = deviceParameterService.getDeviceParamete2(coverBell.getBellNo());

		model.addAttribute("deviceParameterResult", new DeviceParameterResult(coverBell.getBellNo()));
		model.addAttribute("paramList", paramList);
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

	@ResponseBody
//	@RequiresPermissions("cb:equinfo:coverBell:toSetParam")
	@RequestMapping(value = "setParam2/{devNo}")
	public AjaxJson setParam2(@PathVariable String devNo,@RequestBody String json){
		AjaxJson j = new AjaxJson();
		System.out.println("devNo:"+devNo);
		System.out.println("json:"+json);
		Result result =deviceParameterService.setDeviceParameter2(devNo,json);
		System.out.println("result:"+result);
		String msg="";
		if(result.getSuccess().equals("true")){
			j.setSuccess(true);
			msg="指令已下达！";
		}else{
			j.setSuccess(false);
			msg= result.getMsg();
		}

		//System.out.println("result:"+result.getSuccess());
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
				CoverBell coverBell=coverBellService.findUniqueByProperty("a.bell_no", devNo);
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
					CoverBell coverBell=coverBellService.findUniqueByProperty("a.bell_no", devNo);
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

	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:view")
	@RequestMapping(value = "queryDistanceData")
	public AjaxJson queryDistanceData(String devNo,String startDateTime,String endDateTime){
		AjaxJson j = new AjaxJson();
		Result result = deviceParameterService.queryDistanceData(devNo,startDateTime,endDateTime);
		String msg="";
		if(result.getSuccess().equals("true")){
			j.setSuccess(true);
			j.setData(result.getData());
			msg="查询水位数据成功！";
		}else{
			j.setSuccess(false);
			msg= result.getMsg();
		}


		j.setMsg(msg);
		return j;
	}
	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:view")
	@RequestMapping(value = "queryTemperatureData")
	public AjaxJson queryTemperatureData(String devNo,String startDateTime,String endDateTime){
		AjaxJson j = new AjaxJson();
		Result result = deviceParameterService.queryTemperatureData(devNo,startDateTime,endDateTime);
		String msg="";
		if(result.getSuccess().equals("true")){
			j.setSuccess(true);
			j.setData(result.getData());
			msg="查询温度数据成功！";
		}else{
			j.setSuccess(false);
			msg= result.getMsg();
		}

		return j;
	}

	@ResponseBody
	@RequiresPermissions("cb:equinfo:coverBell:syn")
	@RequestMapping(value = "synBellState")
	public AjaxJson synBellState(String devNo){
		String msg="";
		AjaxJson j = new AjaxJson();
		try{
		if(StringUtils.isNotEmpty(devNo)){
			coverBellService.synBellState(devNo);
			j.setSuccess(true);
			msg="数据同步成功！";
		}else{
			j.setSuccess(false);
			msg="井卫编号不能为空！";
		}
		}catch (Exception e){
			j.setSuccess(false);
			msg="数据同步异常:"+e.getMessage();
		}
		j.setMsg(msg);
		return j;
	}
}
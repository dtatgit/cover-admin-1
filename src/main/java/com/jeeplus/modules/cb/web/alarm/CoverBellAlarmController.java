/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.alarm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.sys.utils.DictUtils;
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
import com.jeeplus.modules.cb.entity.alarm.CoverBellAlarm;
import com.jeeplus.modules.cb.service.alarm.CoverBellAlarmService;

/**
 * 井铃报警信息Controller
 * @author crj
 * @version 2019-06-24
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/alarm/coverBellAlarm")
public class CoverBellAlarmController extends BaseController {

	@Autowired
	private CoverBellAlarmService coverBellAlarmService;
	@Autowired
	private CoverService coverService;
	@ModelAttribute
	public CoverBellAlarm get(@RequestParam(required=false) String id) {
		CoverBellAlarm entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverBellAlarmService.get(id);
		}
		if (entity == null){
			entity = new CoverBellAlarm();
		}
		return entity;
	}
	
	/**
	 * 井铃报警信息列表页面
	 */
	@RequiresPermissions("cb:alarm:coverBellAlarm:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/alarm/coverBellAlarmList";
	}

	/**
	 * 井铃报警信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cb:alarm:coverBellAlarm:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverBellAlarm coverBellAlarm, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverBellAlarm> page = coverBellAlarmService.findPage(new Page<CoverBellAlarm>(request, response), coverBellAlarm); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井铃报警信息表单页面
	 */
	@RequiresPermissions(value={"cb:alarm:coverBellAlarm:view","cb:alarm:coverBellAlarm:add","cb:alarm:coverBellAlarm:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CoverBellAlarm coverBellAlarm, Model model) {
		model.addAttribute("coverBellAlarm", coverBellAlarm);
		return "modules/cb/alarm/coverBellAlarmForm";
	}

	/**
	 * 保存井铃报警信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"cb:alarm:coverBellAlarm:add","cb:alarm:coverBellAlarm:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverBellAlarm coverBellAlarm, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverBellAlarm)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverBellAlarmService.save(coverBellAlarm);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井铃报警信息成功");
		return j;
	}
	
	/**
	 * 删除井铃报警信息
	 */
	@ResponseBody
	@RequiresPermissions("cb:alarm:coverBellAlarm:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverBellAlarm coverBellAlarm, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverBellAlarmService.delete(coverBellAlarm);
		j.setMsg("删除井铃报警信息成功");
		return j;
	}
	
	/**
	 * 批量删除井铃报警信息
	 */
	@ResponseBody
	@RequiresPermissions("cb:alarm:coverBellAlarm:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverBellAlarmService.delete(coverBellAlarmService.get(id));
		}
		j.setMsg("删除井铃报警信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cb:alarm:coverBellAlarm:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverBellAlarm coverBellAlarm, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井铃报警信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverBellAlarm> page = coverBellAlarmService.findPage(new Page<CoverBellAlarm>(request, response, -1), coverBellAlarm);
    		new ExportExcel("井铃报警信息", CoverBellAlarm.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井铃报警信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cb:alarm:coverBellAlarm:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverBellAlarm> list = ei.getDataList(CoverBellAlarm.class);
			for (CoverBellAlarm coverBellAlarm : list){
				try{
					coverBellAlarmService.save(coverBellAlarm);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井铃报警信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井铃报警信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井铃报警信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/alarm/coverBellAlarm/?repage";
    }
	
	/**
	 * 下载导入井铃报警信息数据模板
	 */
	@RequiresPermissions("cb:alarm:coverBellAlarm:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井铃报警信息数据导入模板.xlsx";
    		List<CoverBellAlarm> list = Lists.newArrayList(); 
    		new ExportExcel("井铃报警信息数据", CoverBellAlarm.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/alarm/coverBellAlarm/?repage";
    }


	/**
	 * 批量生成工单信息
	 */
	@ResponseBody
	@RequiresPermissions("cb:alarm:coverBellAlarm:work")
	@RequestMapping(value = "createWork")
	public AjaxJson createWork(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverBellAlarmService.createWork(coverBellAlarmService.get(id));
		}
		j.setMsg("生成工单完成!");
		return j;
	}


	/**
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

		//获取报警数据
		CoverBellAlarm alarm=new CoverBellAlarm();
		alarm.setIsGwo("N");//只读取未生成工单数据
		alarm.setDelFlag("0");
		List<CoverBellAlarm>  alarmList=coverBellAlarmService.findList(alarm);
		StringBuffer sb=new StringBuffer();
		for(CoverBellAlarm bellAlarm:alarmList){
			Map<String,Object> resp = new HashMap<String,Object>();
			resp.put("coverId", bellAlarm.getCoverId());
			resp.put("alarmId", bellAlarm.getId());
			resp.put("alarmNum", bellAlarm.getAlarmNum());
			resp.put("bellNo", bellAlarm.getBellNo());
			String alarmTypeName=DictUtils.getDictLabel(bellAlarm.getAlarmType(),"alarm_type", "--");
			resp.put("alarmTypeName", alarmTypeName);
			String coverId=bellAlarm.getCoverId();
			if(StringUtils.isNotEmpty(coverId)){
				Cover cv=null;
				String str=sb.toString();
				if (str.indexOf(coverId)!=-1){
					//返回的值不是-1说明存在包含关系,相反如果包含返回的值必定是-1"
					//System.out.println("存在包含关系，因为返回的值不等于-1");

				}else{
					cv=coverService.get(coverId);
					System.out.println("不存在包含关系，因为返回的值等于-1");
				}

				sb.append(coverId).append(",");

				if(null!=cv){
					resp.put("lng",cv.getLongitude());
					resp.put("lat",cv.getLatitude());
					resp.put("no",cv.getNo());
					resp.put("address",cv.getAddressDetail());
					list.add(resp);
				}
			}


			if(list.size()>200)
			{
				break;
			}

		}
		//报警数据取出经纬度，返回前台数据：报警编号，井卫编号，井盖编号，井盖详细地址
		if(list==null||list.size()<=0){
			j.setSuccess(false);
		}
		j.setData(list);
		return j;
	}


	/**
	 * ajax请求不需要返回页面，只需要得到response中的数据即可，所以方法签名为void即可
	 *
	 * @param request
	 * @param response
	 */

	@RequestMapping(value = "ajaxAlarmData", method = RequestMethod.POST)
	public void ajaxAlarmData(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter printWriter = null;
		//List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>(5);
		try {

					Integer alarmNum=coverBellAlarmService.queryAlarmData();
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

}
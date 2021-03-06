/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.web.equinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.collection.MapUtil;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.service.statis.CoverCollectStatisService;
import com.jeeplus.modules.sys.entity.Role;
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

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;

/**
 * 井盖基础信息Controller
 * @author crj
 * @version 2019-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/equinfo/cover")
public class CoverController extends BaseController {

	@Autowired
	private CoverService coverService;
	@Autowired
	private CoverCollectStatisService coverCollectStatisService;
	@ModelAttribute
	public Cover get(@RequestParam(required=false) String id) {
		Cover entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverService.get(id);
		}
		if (entity == null){
			entity = new Cover();
		}
		return entity;
	}
	
	/**
	 * 井盖基础信息列表页面
	 */
	@RequiresPermissions("cv:equinfo:cover:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cv/equinfo/coverList";
	}
	
		/**
	 * 井盖基础信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:cover:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Cover cover, HttpServletRequest request, HttpServletResponse response, Model model) {
				Page<Cover> page = coverService.findPage(new Page<Cover>(request, response), cover);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖基础信息表单页面
	 */
	@RequiresPermissions(value={"cv:equinfo:cover:view","cv:equinfo:cover:add","cv:equinfo:cover:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Cover cover, Model model) {
		model.addAttribute("cover", cover);
		return "modules/cv/equinfo/coverForm";
	}

	/**
	 * 保存井盖基础信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:equinfo:cover:add","cv:equinfo:cover:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Cover cover, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, cover)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		coverService.save(cover);//新建或者编辑保存
		j.setSuccess(true);
		j.setMsg("保存井盖基础信息成功");
		return j;
	}



	/**
	 * 保存井盖基础信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:equinfo:cover:add","cv:equinfo:cover:edit"},logical=Logical.OR)
	@RequestMapping(value = "repairSave")
	public AjaxJson repairSave(Cover cover, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, cover)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增或编辑表单保存
		coverService.repairSave(cover,CodeConstant.SOURCE.REPAIR);//保存
		j.setSuccess(true);
		j.setMsg("保存井盖基础信息成功");
		return j;
	}

	/**
	 * 保存井盖基础信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"cv:equinfo:cover:add","cv:equinfo:cover:edit"},logical=Logical.OR)
	@RequestMapping(value = "repairSaveTask")
	public AjaxJson repairSaveTask(Cover cover, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, cover)){
			j.setSuccess(false);
			j.setMsg("非法参数！");
			return j;
		}
		//新增或编辑表单保存
		coverService.repairSave(cover,CodeConstant.SOURCE.TASK);//保存
		j.setSuccess(true);
		j.setMsg("保存井盖基础信息成功");
		return j;
	}

	/**
	 * 删除井盖基础信息
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:cover:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Cover cover, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverService.delete(cover);
		j.setMsg("删除井盖基础信息成功");
		return j;
	}
	
	/**
	 * 批量删除井盖基础信息
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:cover:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverService.delete(coverService.get(id));
		}
		j.setMsg("删除井盖基础信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("cv:equinfo:cover:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(Cover cover, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "井盖基础信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Cover> page = coverService.findPage(new Page<Cover>(request, response, -1), cover);
    		new ExportExcel("井盖基础信息", Cover.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出井盖基础信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("cv:equinfo:cover:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Cover> list = ei.getDataList(Cover.class);
			for (Cover cover : list){
				try{
					coverService.save(cover);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条井盖基础信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条井盖基础信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入井盖基础信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/cover/?repage";
    }
	
	/**
	 * 下载导入井盖基础信息数据模板
	 */
	@RequiresPermissions("cv:equinfo:cover:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "井盖基础信息数据导入模板.xlsx";
    		List<Cover> list = Lists.newArrayList(); 
    		new ExportExcel("井盖基础信息数据", Cover.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cv/equinfo/cover/?repage";
    }


	/**
	 * 查看，增加，编辑井盖基础信息表单页面
	 */
	@RequiresPermissions(value={"cv:equinfo:cover:view","cv:equinfo:cover:add","cv:equinfo:cover:edit"},logical=Logical.OR)
	@RequestMapping(value = "view")
	public String view(Cover cover, Model model) {
		 cover=coverService.get(cover.getId());
		model.addAttribute("cover", cover);
		return "modules/cv/equinfo/coverView";
	}

	/**
	 * 返回井盖数据到地图上
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "mapdata")
	public AjaxJson mapdata(HttpServletRequest request, HttpServletResponse response, Model model) {
		boolean flag=true;//首页权限控制，true为看到全部数据
		//String officeId="0";
		//获取当前用户角色
/*		User user= UserUtils.getUser();
		List<Role> roleList=user.getRoleList();
		if(null!=roleList&&roleList.size()>0){
			for(Role r:roleList){
				if(r.getEnname().contains("thirdParty")){
					flag=false;
				}
			}
		}
		if(!flag){//代理商数据
			officeId=user.getOffice().getId();
		}*/

		AjaxJson j = new AjaxJson();
		Cover cover=new Cover();
//		if(!flag){//代理商数据
//
//		}
		String areaName=request.getParameter("areaName");
		String startLat=request.getParameter("startLat");
		String endLat=request.getParameter("endLat");
		String startLng=request.getParameter("startLng");
		String endLng=request.getParameter("endLng");

		List<Cover> coverlist=null;
		if(StringUtils.isNotEmpty(areaName)){
			cover.setDistrict(areaName);
			cover.setCoverStatus(CodeConstant.COVER_STATUS.AUDIT_PASS);//只展示审核通过的数据
			cover.setBeginLatitude(startLat);
			cover.setEndLatitude(endLat);
			cover.setBeginLongitude(startLng);
			cover.setEndLongitude(endLng);
		 	coverlist = coverService.findList(cover);
		}

		logger.info("*************获取审核通过的井盖数量****************"+coverlist.size());
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		if (null != coverlist) {
			for (Cover cv : coverlist) {
				Map<String,Object> resp = new HashMap<String,Object>();
				//Map<String,String> m= MapUtil.map_bd2tx(Double.parseDouble(cg.getLatitude()), Double.parseDouble(cg.getLongitude()));
				resp.put("lng",cv.getLongitude());
				resp.put("lat",cv.getLatitude());
				resp.put("no",cv.getNo());
				resp.put("address",cv.getAddressDetail());
				list.add(resp);

			}
		}

		if(list==null||list.size()<=0){
			j.setSuccess(false);
		}
		j.setData(list);
		return j;
	}
	/**
	 * 返回井盖数据到地图上
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "mapdatas")
	public AjaxJson mapdatas(HttpServletRequest request, HttpServletResponse response, Model model) {


		AjaxJson j = new AjaxJson();
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();

		Map<String,Object> resp = new HashMap<String,Object>();

		Map<String,String> map=coverCollectStatisService.statisGroupbyArea();
		resp.put("glq",map.get("鼓楼区"));
		resp.put("tsq",map.get("铜山区"));
		resp.put("ylq",map.get("云龙区"));
		resp.put("qsq",map.get("泉山区"));
		resp.put("jwq",map.get("贾汪区"));
		resp.put("xcq",map.get("新城区")==null?0:map.get("新城区"));
		resp.put("wyh",map.get("云龙湖风景管理委员会")==null?0:map.get("云龙湖风景管理委员会"));
		resp.put("kfq",map.get("徐州经济技术开发区")==null?0:map.get("徐州经济技术开发区"));
	/*	Integer glq=coverCollectStatisService.statisByArea("鼓楼区");
		Integer tsq=coverCollectStatisService.statisByArea("铜山区");
		Integer ylq=coverCollectStatisService.statisByArea("云龙区");
		Integer qsq=coverCollectStatisService.statisByArea("泉山区");
		resp.put("glq",glq);
		resp.put("tsq",tsq);
		resp.put("ylq",ylq);
		resp.put("qsq",qsq);*/
		//list.add(resp);


	/*	if(list==null||list.size()<=0){
			j.setSuccess(false);
		}*/
		j.setSuccess(true);
		j.setData(resp);
		return j;
	}

	/**
	 * 返回井盖数据到地图上
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="mapOutdatas",method = RequestMethod.POST,
			consumes="application/json", produces="application/json")
	public AjaxJson mapOutdatas(@RequestBody String param) {


		AjaxJson j = new AjaxJson();
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();

		Map<String,Object> resp = new HashMap<String,Object>();
		Integer glq=coverCollectStatisService.statisByArea("鼓楼区");
		Integer tsq=coverCollectStatisService.statisByArea("铜山区");
		Integer ylq=coverCollectStatisService.statisByArea("云龙区");
		Integer qsq=coverCollectStatisService.statisByArea("泉山区");
		resp.put("glq",glq);
		resp.put("tsq",tsq);
		resp.put("ylq",ylq);
		resp.put("qsq",qsq);
		//list.add(resp);


	/*	if(list==null||list.size()<=0){
			j.setSuccess(false);
		}*/
		j.setSuccess(true);
		j.setData(resp);
		return j;
	}

	@RequiresPermissions(value={"cv:equinfo:cover:work"})
	@RequestMapping(value = "createWorkPage")
	public String createWorkPage(Cover cover, Model model) {
		CoverWork  coverWork=new CoverWork();
		coverWork.setCoverIds(cover.getIds());
		coverWork.setCoverNos(cover.getCoverNos());
		//coverWork.setWorkNum(IdGen.getInfoCode("CW"));
		model.addAttribute("coverWork",coverWork);
		return "modules/cb/work/installCoverWork";
	}

	//查看井卫信息
	@RequiresPermissions("cv:equinfo:cover:bell")
	@RequestMapping(value = "belllist")
	public String belllist(Cover cover, Model model) {
		model.addAttribute("cover", cover);
		return "modules/cv/equinfo/showBellInfo";
	}

	//查看报警记录
	@RequiresPermissions("cv:equinfo:cover:alarm")
	@RequestMapping(value = "alarmlist")
	public String alarmlist(Cover cover, Model model) {
		model.addAttribute("cover", cover);
		return "modules/cv/equinfo/showAlarmInfo";
	}
}
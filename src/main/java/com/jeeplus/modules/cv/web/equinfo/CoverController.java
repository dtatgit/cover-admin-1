/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.web.equinfo;

import com.alibaba.fastjson.JSONObject;
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
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import com.jeeplus.modules.cb.constant.bizAlarm.BizAlarmConstant;
import com.jeeplus.modules.cb.entity.bizAlarm.BizAlarm;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.entity.equinfo.CoverBellVo;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.service.bizAlarm.BizAlarmService;
import com.jeeplus.modules.cb.service.equinfo.CoverBellOperationService;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.entity.equinfo.CoverAudit;
import com.jeeplus.modules.cv.service.equinfo.CoverDamageService;
import com.jeeplus.modules.cv.service.equinfo.CoverImageService;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.cv.service.statis.CoverCollectStatisService;
import com.jeeplus.modules.cv.vo.CountVo;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 井盖基础信息Controller
 * @author crj
 * @version 2019-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/cv/equinfo/cover")
public class CoverController extends BaseController {


	private static final String coverBellServerUrl = Global.getConfig("coverBell.server.url");

	@Autowired
	private CoverService coverService;
	@Autowired
	private CoverCollectStatisService coverCollectStatisService;
	@Autowired
	private CoverImageService coverImageService;
	@Autowired
	private CoverDamageService coverDamageService;
	@Autowired
	CoverBellService coverBellService;
	@Autowired
	private CoverBellOperationService coverBellOperationService;
	@Autowired
	private CoverWorkService workService;
	@Autowired
	private BizAlarmService bizAlarmService;


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
				cover.setCoverStatus(CodeConstant.COVER_STATUS.AUDIT_PASS);//只展示审核通过的数据
				Page<Cover> page = coverService.findPage(new Page<Cover>(request, response), cover);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑井盖基础信息表单页面
	 */
	@RequiresPermissions(value={"cv:equinfo:cover:view","cv:equinfo:cover:add","cv:equinfo:cover:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Cover cover, Model model) {

		List<CoverBell> list = coverBellService.getByCoverId(cover.getId());


		logger.info("******井卫硬件接口地址deviceSimpleInfo()：********{}",coverBellServerUrl);
		List<CoverBellVo> collect = list.stream().map(item -> {
			String devNo = item.getBellNo();
			String deviceUrl = coverBellServerUrl + "/device/deviceSimpleInfo/" + devNo;


			CoverBellVo vo = new CoverBellVo();
			BeanUtils.copyProperties(item, vo);

			try {
				logger.info("【【 接口开始-->");
				String str = HttpClientUtil.get(deviceUrl);
				logger.info("<--接口结束】】");
				logger.info("deviceSimpleInfo(),结果:{}", str);

				Result resultTemp = JSONObject.parseObject(str, Result.class);

				if (resultTemp.getSuccess().equals("true")) {
					Object data = resultTemp.getData();
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
					String angle = jsonObject.getString("angle");
					String temperature = jsonObject.getString("temperature");
					String waterLevel = jsonObject.getString("waterLevel");
					vo.setAngle(angle);
					vo.setTemperature(temperature);
					vo.setWaterLevel(waterLevel);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return vo;
		}).collect(Collectors.toList());


		logger.info("执行结束");

		model.addAttribute("cover", cover);
		model.addAttribute("belllist", collect);
		return "modules/cv/equinfo/coverFormNew";
	}



	@ResponseBody
	@RequestMapping(value = "bellList")
	public AjaxJson bellList(String coverId) {
		AjaxJson j = new AjaxJson();


		List<CoverBell> list = coverBellService.getByCoverId(coverId);

		logger.info("******井卫硬件接口地址deviceSimpleInfo()：********{}",coverBellServerUrl);
		List<CoverBellVo> collect = list.stream().map(item -> {
			String devNo = item.getBellNo();
			String deviceUrl = coverBellServerUrl + "/device/deviceSimpleInfo/" + devNo;


			CoverBellVo vo = new CoverBellVo();
			BeanUtils.copyProperties(item, vo);

			try {
				logger.info("【【 接口开始-->");
				String str = HttpClientUtil.get(deviceUrl);
				logger.info("<--接口结束】】");
				logger.info("deviceSimpleInfo(),结果:{}", str);

				Result resultTemp = JSONObject.parseObject(str, Result.class);

				if (resultTemp.getSuccess().equals("true")) {
					Object data = resultTemp.getData();
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(data);
					String angle = jsonObject.getString("angle");
					String temperature = jsonObject.getString("temperature");
					String waterLevel = jsonObject.getString("waterLevel");
					vo.setAngle(angle);
					vo.setTemperature(temperature);
					vo.setWaterLevel(waterLevel);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return vo;
		}).collect(Collectors.toList());

		j.setData(collect);


		return j;
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

		List<CoverBell> bellList= coverBellService.getByCoverId(cover.getId());
		if(bellList!=null && bellList.size()>0){
			j.setSuccess(false);
			j.setMsg("该井盖还有未解绑井卫，请先解绑！");
		}else{
			coverService.delete(cover);
			j.setMsg("删除井盖信息成功");
		}

//		Cover oldCover=coverService.get(cover.getId());
//		oldCover.setDelFlag(Cover.DEL_FLAG_DELETE);
//		coverService.save(oldCover);
//		//coverService.delete(cover);

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
			Cover oldCover=coverService.get(id);
			oldCover.setDelFlag(Cover.DEL_FLAG_DELETE);
			coverService.save(oldCover);
			//coverService.delete(coverService.get(id));
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
           // Page<Cover> page = coverService.findPage(new Page<Cover>(request, response, -1), cover);
			List<Cover> coverlist=coverService.findAllCovers(cover);

    		new ExportExcel("井盖基础信息", Cover.class).setDataList(coverlist).write(response, fileName).dispose();
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
//					String coverNo=cover.getNo();//井盖编号
//					String projectNo=cover.getProjectName();//所属项目编号
//					if(StringUtils.isNotEmpty(coverNo)&&StringUtils.isNotEmpty(projectNo)){//项目编号为空，不做任务处理，否则把井盖数据克隆都该项目
//						coverService.cloneCoverForProject(coverNo,projectNo);
//					}
					String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
					String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
					//项目内用户导入，需要校验项目内是否存在
					Cover query=new Cover();
					query.setNo(cover.getNo());
					List<Cover> coverList=coverService.findList(query);
					if(null!=coverList&&coverList.size()>0){//进行覆盖？
						//cover.setId(coverList.get(0).getId());
						//cover.setIsNewRecord(false);upload
					}else if(StringUtils.isNotEmpty(projectId)){
						cover.setProjectId(projectId);
						cover.setProjectName(projectName);
						cover.setDataSource(CodeConstant.COVER_DATA_SOURCE.GATHER);
						cover.setId(IdGen.uuid());
						cover.setIsNewRecord(true);
						coverService.save(cover);

						//关联井盖图片关联关系
						coverImageService.cloneCoverImage(cover.getId(), cover.getImageInfoStr());
						//关联井盖损坏形式关系
						coverDamageService.cloneCoverDamage(cover.getId(), cover.getDamageInfoStr());
						successNum++;
					}

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
		//获取当前用户岗位
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

	/**
	 * 跳转到 创建工单界面
	 * @param cover
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "createWorkPageNew")
	public String createWorkPageNew(Cover cover, Model model) {
		CoverWork  coverWork=new CoverWork();
		coverWork.setCoverIds(cover.getIds());
		coverWork.setCoverNos(cover.getCoverNos());
		model.addAttribute("coverWork",coverWork);
		return "modules/cv/work/createWork";
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

	/**
	 * 批量设防 add by 2021-06-09
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:cover:defense")
	@RequestMapping(value = "fortify")
	public AjaxJson fortify(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		boolean flag=coverBellService.CoverWorkStatus(ids, CodeConstant.DEFENSE_STATUS.FORTIFY);

		if(flag){
			j.setSuccess(true);
			j.setMsg("批量设防成功!");
		}else{
			j.setMsg("批量设防失败!");
		}
		return j;
	}


	/**
	 * 批量设防 new
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "fortifyNew")
	public AjaxJson fortifyNew(String ids,String nos, RedirectAttributes redirectAttributes) {

		AjaxJson j = coverBellService.CoverWorkStatusNew(ids,nos,CodeConstant.DEFENSE_STATUS.FORTIFY);

		return j;
	}

	/**
	 * 批量撤防 new
	 * @param ids
	 * @param nos
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "revokeNew")
	public AjaxJson revokeNew(String ids,String nos, RedirectAttributes redirectAttributes) {

		AjaxJson j = coverBellService.CoverWorkStatusNew(ids, nos,CodeConstant.DEFENSE_STATUS.REVOKE);

		return j;
	}

	/**
	 * 单独一个井盖 设防
	 * @param id
	 * @param coverno
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "fortifySingle")
	public AjaxJson fortifySingle(String id,String coverno) {

		AjaxJson j = new AjaxJson();

		StringBuilder sb = new StringBuilder();

		String status = CodeConstant.DEFENSE_STATUS.FORTIFY;

		List<CoverBell> bellList= coverBellService.getByCoverId(id);
		if(null!=bellList&&bellList.size()>0){
			for(CoverBell bell:bellList){
				String success="";
				Result result =coverBellService.setDefense(bell, status);
				if(null!=result){
					success=result.getSuccess();
				}
				if(StringUtils.isNotEmpty(success)&&success.equals("true")){
					try {
						coverBellOperationService.genRecordNew(status,bell);
					} catch (Exception e) {
						logger.error("操作记录异常：{}",e.getMessage());
						e.printStackTrace();
					}
				}else{
					sb.append(bell.getBellNo()+"<br/>");
					logger.info(bell.getBellNo()+":"+result.getMsg());
				}
			}
		}

		if(StringUtils.isNotBlank(sb.toString())){
			String message = "井卫：" +sb.toString()+"设防失败";

			j.setSuccess(false);
			j.setMsg(message);
		}else {
			//修改表
			coverService.updateWorkStatus(id,status);

			j.setMsg("设防成功");
		}

		return j;
	}


	/**
	 * 单独的撤防
	 * @param id
	 * @param coverno
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "chefangSingle")
	public AjaxJson chefangSingle(String id,String coverno) {

		AjaxJson j = new AjaxJson();

		StringBuilder sb = new StringBuilder();

		String status = CodeConstant.DEFENSE_STATUS.REVOKE;

		List<CoverBell> bellList= coverBellService.getByCoverId(id);
		if(null!=bellList&&bellList.size()>0){
			for(CoverBell bell:bellList){
				String success="";
				Result result =coverBellService.setDefense(bell, status);
				if(null!=result){
					success=result.getSuccess();
				}
				if(StringUtils.isNotEmpty(success)&&success.equals("true")){
					try {
						coverBellOperationService.genRecordNew(status,bell);
					} catch (Exception e) {
						logger.error("操作记录异常：{}",e.getMessage());
						e.printStackTrace();
					}
				}else{
					sb.append(bell.getBellNo()+"<br/>");
					logger.info(bell.getBellNo()+":"+result.getMsg());
				}
			}
		}

		if(StringUtils.isNotBlank(sb.toString())){
			String message = "井卫：" +sb.toString()+"撤防失败";

			j.setSuccess(false);
			j.setMsg(message);
		}else {
			//修改表
			coverService.updateWorkStatus(id,status);

			j.setMsg("撤防成功");
		}

		return j;
	}
	/**
	 * 批量撤防 add by 2021-06-09
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:cover:defense")
	@RequestMapping(value = "revoke")
	public AjaxJson revoke(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		boolean flag=coverBellService.CoverWorkStatus(ids, CodeConstant.DEFENSE_STATUS.REVOKE);

		if(flag){
			j.setSuccess(true);
			j.setMsg("批量撤防成功!");

		}else{
			j.setSuccess(false);
			j.setMsg("批量撤防失败!");
		}
		return j;
	}

	/**
	 *批量解绑操作
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("cb:equinfo:cover:untying")
	@RequestMapping(value = "untying")
	public AjaxJson untying(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		boolean success=coverBellService.batchUntying(ids);
		if(success){
			j.setSuccess(true);
			j.setMsg("批量解绑成功!");

		}else{
			j.setSuccess(false);
			j.setMsg("批量解绑失败!");
		}

		return j;
	}


	@ResponseBody
	@RequestMapping(value = "unbindGuard")
	public AjaxJson unbindGuard(String bid, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();

		int i = workService.countByBellIdNoComplete(bid);
		if(i>0){
			j.setSuccess(false);
			j.setMsg("该井卫还有未完成工单,请处理后再进行解绑");
		}else{
			CoverBell coverBell = coverBellService.get(bid);
			coverBellService.untyingNew(coverBell);
			j.setMsg("解绑成功!");
		}


		return j;
	}


	/**
	 * 井盖审核
	 * @param
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "audit")
	public AjaxJson auditPass(@RequestParam("coverId")String coverId,
						  @RequestParam("desc")String desc,
						  @RequestParam("status")String status,
						  @RequestParam("imgIds")String imgIds,Model model) throws Exception{
		AjaxJson j = new AjaxJson();

		Cover cover = coverService.get(coverId);

		//审核记录
		CoverAudit audit = new CoverAudit();
		audit.setCover(new Cover(coverId));
		audit.setAuditStatus(status);
		audit.setAuditResult(desc);
		audit.setAuditTime(new Date());
		audit.setAuditUser(UserUtils.getUser());
		audit.setImgIds(imgIds);
		if(cover!=null){
			audit.setProjectId(cover.getProjectId());
			audit.setProjectName(cover.getProjectName());
		}

		coverService.audit(audit,coverId,status);

		j.setSuccess(true);
		j.setMsg("审核操作成功");
		return j;
	}


	/**
	 * 批量通过
	 * @param ids
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/batchPass")
	public AjaxJson batchPass(String ids, Model model) {

		AjaxJson j = new AjaxJson();

		List<String> list = Arrays.asList(ids.split(","));
		if(null!=list&&list.size()>0){
			for(String id:list){

				//审核记录
				CoverAudit audit = new CoverAudit();
				audit.setCover(new Cover(id));
				audit.setAuditStatus(CodeConstant.AUDIT_STATUS.AUDIT_PASS);
				audit.setAuditResult("");
				audit.setAuditTime(new Date());
				audit.setAuditUser(UserUtils.getUser());

				coverService.audit(audit,id,CodeConstant.AUDIT_STATUS.AUDIT_PASS);
			}
		}

		j.setSuccess(true);
		j.setMsg("批量审核通过成功！");

		return j;
	}


	/**
	 * 批量驳回
	 * @param ids
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/batchReject")
	public AjaxJson batchReject(String ids, Model model) {

		AjaxJson j = new AjaxJson();

		List<String> list = Arrays.asList(ids.split(","));
		if(null!=list&&list.size()>0){
			for(String id:list){

				//审核记录
				CoverAudit audit = new CoverAudit();
				audit.setCover(new Cover(id));
				audit.setAuditStatus(CodeConstant.AUDIT_STATUS.AUDIT_FAIL);
				audit.setAuditResult("");
				audit.setAuditTime(new Date());
				audit.setAuditUser(UserUtils.getUser());

				coverService.audit(audit,id,CodeConstant.AUDIT_STATUS.AUDIT_FAIL);
			}
		}

		j.setSuccess(true);
		j.setMsg("批量审核通过成功！");

		return j;
	}


	/**
	 * 井盖统计
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/coverCount")
	public AjaxJson coverCount() {
		AjaxJson j = new AjaxJson();


		List<CountVo> countVos = coverService.countSql();

		j.setData(countVos);

		return j;
	}


	/**
	 * 报警统计
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/alarmCount")
	public AjaxJson alarmCount() {
		AjaxJson j = new AjaxJson();


		List<CountVo> countVos = bizAlarmService.countSql();

		j.setData(countVos);

		return j;
	}


	/**
	 * 首页 报警记录
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/alarmList")
	public AjaxJson alarmList() {
		AjaxJson j = new AjaxJson();
		BizAlarm bizAlarm = new BizAlarm();
		bizAlarm.setDealStatus(BizAlarmConstant.BizAlarmDealStatus.NOT_DEAL);
		Page<BizAlarm> page = bizAlarmService.findPage(new Page<BizAlarm>(1,10), bizAlarm);
		List<BizAlarm> list = page.getList();

		j.setData(list);

		return j;
	}


	/**
	 * 工单状态(基于生命周期字段)统计
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workStatusCount")
	public AjaxJson workStatusCount() {
		AjaxJson j = new AjaxJson();


		List<CountVo> countVos = workService.lifeCycleCountSql();

		j.setData(countVos);

		return j;
	}

	/**
	 * 工单类型统计
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/workTypeCount")
	public AjaxJson workTypeCount() {
		AjaxJson j = new AjaxJson();


		List<CountVo> countVos = workService.workTypeCountSql();

		j.setData(countVos);

		return j;
	}
}
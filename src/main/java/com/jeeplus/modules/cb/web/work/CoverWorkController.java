/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.web.work;

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
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.entity.work.CoverWorkOperationDetail;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cb.service.work.CoverWorkOperationDetailService;
import com.jeeplus.modules.cb.service.work.CoverWorkOperationService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.flow.entity.base.FlowProc;
import com.jeeplus.modules.flow.entity.opt.FlowWorkOpt;
import com.jeeplus.modules.flow.service.opt.FlowWorkOptService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ????????????Controller
 * @author crj
 * @version 2019-06-26
 */
@Controller
@RequestMapping(value = "${adminPath}/cb/work/coverWork")
public class CoverWorkController extends BaseController {

	@Autowired
	private CoverWorkService coverWorkService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private CoverService coverService;
	@Autowired
	private CoverBellService coverBellService;
	@Autowired
	private CoverWorkOperationService coverWorkOperationService;
	@Autowired
	private CoverWorkOperationDetailService coverWorkOperationDetailService;
	@Autowired
	private FlowWorkOptService flowWorkOptService;
	
	@ModelAttribute
	public CoverWork get(@RequestParam(required=false) String id) {
		CoverWork entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = coverWorkService.get(id);
		}
		if (entity == null){
			entity = new CoverWork();
		}
		return entity;
	}
	
	/**
	 * ????????????????????????
	 */
	@RequiresPermissions("cb:work:coverWork:list")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/cb/work/coverWorkList";
	}
	
		/**
	 * ????????????????????????
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWork:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CoverWork coverWork, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CoverWork> page = coverWorkService.findPage(new Page<CoverWork>(request, response), coverWork);
		List<CoverWork> coverWorkList = page.getList();

		if(null!=coverWorkList&&coverWorkList.size()>0){
		for(CoverWork work:coverWorkList){
			if(null!=work.getCreateBy()&&StringUtils.isNotEmpty(work.getCreateBy().getId())){
				work.setCreateBy(systemService.getUser(work.getCreateBy().getId()));
				Office office = officeService.get(work.getCreateDepart());
				if(null!=office&&StringUtils.isNotEmpty(office.getName())){
					work.setCreateDepart(office.getName());
				}

			}
			FlowProc proc=work.getFlowId();
			if(null!=proc){
				work.setFlowProId(proc.getId());
				work.setFlowNo(proc.getFlowNo());
			}


		}
		}
		return getBootstrapData(page);
	}

	/**
	 * ????????????????????????????????????????????????
	 */
	@RequiresPermissions(value={"cb:work:coverWork:view","cb:work:coverWork:add","cb:work:coverWork:edit"},logical=Logical.OR)
    @RequestMapping(value = "form")
    public String form(CoverWork coverWork, Model model) {
        String id=coverWork.getId();
        if(StringUtils.isEmpty(id)){
            coverWork.setWorkNum(IdGen.getInfoCode("CW"));
        }
        model.addAttribute("coverWork", coverWork);
        return "modules/cb/work/coverWorkForm";
    }

    /**
     * ??????????????????????????????
     */
    @RequiresPermissions("cb:work:coverWork:edit")
    @RequestMapping(value = "edit")
    public String edit(CoverWork coverWork, Model model) {
        String id=coverWork.getId();
        if(StringUtils.isEmpty(id)){
            coverWork.setWorkNum(IdGen.getInfoCode("CW"));
        }
        model.addAttribute("coverWork", coverWork);
        return "modules/cb/work/coverWorkEdit";
    }

	/**
	 * ??????????????????
	 */
	@ResponseBody
	@RequiresPermissions(value={"cb:work:coverWork:add","cb:work:coverWork:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CoverWork coverWork, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverWork)){
			j.setSuccess(false);
			j.setMsg("???????????????");
			return j;
		}

		Cover cover=coverWork.getCover();
		if(coverWork.getIsNewRecord() && coverWork.getWorkType().equals(CodeConstant.WORK_TYPE.INSTALL)){
			cover = coverService.get(cover.getId());
			String isGwo = cover.getIsGwo();
			if(isGwo == null || isGwo.equals(CodeConstant.cover_gwo.not_install)){
				coverWork.setCoverNo(cover.getNo());
				coverWork.setLatitude(cover.getLatitude());
				coverWork.setLongitude(cover.getLongitude());
				String workStatus=coverWork.getWorkStatus();// ????????????
				if(StringUtils.isEmpty(workStatus)){
					coverWork.setWorkStatus(CodeConstant.WORK_STATUS.ASSIGN);
				}
				coverWorkService.save(coverWork);//????????????????????????
				//???????????????????????????
				coverService.updateGwoById(cover.getId(), CodeConstant.cover_gwo.handle);
			}else{
				j.setSuccess(false);
				j.setMsg("??????????????????????????????");

				return j;
			}
		}else{
			if(null!=cover){
				cover=coverService.get(cover.getId());
				coverWork.setCoverNo(cover.getNo());
				coverWork.setLatitude(cover.getLatitude());
				coverWork.setLongitude(cover.getLongitude());
			}
			String workStatus=coverWork.getWorkStatus();// ????????????
			if(StringUtils.isEmpty(workStatus)){
				coverWork.setWorkStatus(CodeConstant.WORK_STATUS.ASSIGN);
			}
			coverWorkService.save(coverWork);//????????????????????????
			j.setMsg("????????????????????????");
		}

		return j;
	}
	
	/**
	 * ??????????????????
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWork:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CoverWork coverWork, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		coverWorkService.delete(coverWork);
		j.setMsg("????????????????????????");
		return j;
	}
	
	/**
	 * ????????????????????????
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWork:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			coverWorkService.delete(coverWorkService.get(id));
		}
		j.setMsg("????????????????????????");
		return j;
	}
	
	/**
	 * ??????excel??????
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWork:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public AjaxJson exportFile(CoverWork coverWork, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "????????????"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CoverWork> page = coverWorkService.findPage(new Page<CoverWork>(request, response, -1), coverWork);
    		new ExportExcel("????????????", CoverWork.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("???????????????");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("????????????????????????????????????????????????"+e.getMessage());
		}
			return j;
    }

	/**
	 * ??????Excel??????

	 */
	@RequiresPermissions("cb:work:coverWork:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CoverWork> list = ei.getDataList(CoverWork.class);
			for (CoverWork coverWork : list){
				try{
					coverWorkService.save(coverWork);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "????????? "+failureNum+" ????????????????????????");
			}
			addMessage(redirectAttributes, "??????????????? "+successNum+" ?????????????????????"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "??????????????????????????????????????????"+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWork/?repage";
    }
	
	/**
	 * ????????????????????????????????????
	 */
	@RequiresPermissions("cb:work:coverWork:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "??????????????????????????????.xlsx";
    		List<CoverWork> list = Lists.newArrayList(); 
    		new ExportExcel("??????????????????", CoverWork.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "??????????????????????????????????????????"+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/cb/work/coverWork/?repage";
    }

	@RequiresPermissions("cb:work:coverWork:assign")
	@RequestMapping(value = "toWorkAssign")
	public String toWorkAssign(CoverWork coverWork, Model model) {
		model.addAttribute("coverWork", coverWork);
		return "modules/cb/work/coverWorkAssign";
	}

	@RequiresPermissions("cb:work:coverWork:assign")
	@RequestMapping(value = "workAssign")
	@ResponseBody
	public AjaxJson workAssign(CoverWork coverWork) {
		AjaxJson j = new AjaxJson();
		coverWorkService.workAssign(coverWork);
		j.setSuccess(true);
		j.setMsg("????????????");
		return j;
	}

    //????????????????????????(coverWorkOperation)
    @RequiresPermissions("cb:work:coverWork:workOperationList")
    @RequestMapping(value = "workOperationList")
    public String workOperationList(CoverWork coverWork, Model model) {
        model.addAttribute("coverWork", coverWork);
        //return "modules/cb/work/showWorkOperationList";
		String coverAppUrl = Global.getConfig("coverBell.api.url");  //app??????url
		model.addAttribute("coverAppUrl", coverAppUrl);
		return "modules/cb/work/showWorkFlowOptList";
    }
	/**
	 * ??????????????????????????????
	 */
	@ResponseBody
	@RequestMapping(value = "createWork")
	public AjaxJson createWork(CoverWork coverWork, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();
		if (!beanValidator(model, coverWork)){
			j.setSuccess(false);
			j.setMsg("???????????????");
			return j;
		}
		String coverIds=coverWork.getCoverIds();
		if(StringUtils.isNotEmpty(coverIds)){

			if(coverWork.getWorkType().equals(CodeConstant.WORK_TYPE.INSTALL)){

				StringBuilder newids = new StringBuilder();
				StringBuilder nouseids = new StringBuilder();

				String[] arrStr = coverIds.split(",");
				for (int i = 0; i < arrStr.length; i++) {
					String idTemp = arrStr[i];
					Cover cover = coverService.get(idTemp);
					if(cover!=null){
						String isaz = cover.getIsGwo();
						if(StringUtils.isBlank(isaz) || isaz.equals("N")){
							newids.append(idTemp).append(","); //??????
						}else{
							//?????????
							nouseids.append(cover.getNo()).append("<br/>");
						}
					}
				}
				if(StringUtils.isNotBlank(newids.toString())){
					coverWorkService.createWorkForAll(coverWork,coverIds);//??????
					j.setMsg("????????????????????????!");
				}

				if(StringUtils.isNotBlank(nouseids)){
					j.setSuccess(false);
					j.setMsg("?????????"+nouseids.toString()+"???????????????????????????");
				}

			}else{
				//coverWorkService.createWorkForInstall(coverWork,coverIds);//??????????????????
				//add by 2019-12-20 ??????????????????
				coverWorkService.createWorkForAll(coverWork,coverIds);//??????
				j.setMsg("????????????????????????!");
			}

			j.setSuccess(true);

		}else{
			j.setSuccess(false);
			j.setMsg("????????????????????????!");
		}


		return j;
	}

	/**
	 * ??????????????????
	 * @param coverWork
	 * @param model
	 * @return
	 */
	@RequiresPermissions("cb:work:coverWork:view")
	@RequestMapping(value = "workDetail")
	public String workDetail(CoverWork coverWork, Model model) {
		CoverWork work=coverWorkService.get(coverWork.getId());//????????????
		Cover cover=coverService.get(work.getCover().getId());// ????????????
		CoverBell coverBell=coverBellService.get(work.getCoverBellId());// ????????????

/*		CoverWorkOperation coverWorkOperation=new  CoverWorkOperation();
		coverWorkOperation.setCoverWork(work);*/
/*		List<CoverWorkOperation> operateionList = coverWorkOperationService.findList(coverWorkOperation);
		if(null!=operateionList&&operateionList.size()>0){
			for(CoverWorkOperation operation:operateionList){
				operation.setWorkOperationDetail(coverWorkOperationDetailService.obtainDetail(coverWork.getId()));
			}
		}

		CoverWorkOperation workOperation=new CoverWorkOperation();//??????????????????(????????????)*/

		model.addAttribute("coverWork", work);//????????????
		model.addAttribute("cover", cover);// ????????????
		model.addAttribute("coverBell", coverBell);// ????????????
/*		model.addAttribute("workOperationList", operateionList);//??????????????????
		model.addAttribute("workOperation", workOperation);//??????????????????(????????????)*/

		//????????????????????????
		List<CoverWorkOperationDetail> installDetailList =coverWorkOperationDetailService.obtainDetailByWork(coverWork.getId(), CodeConstant.record_type.install);
		model.addAttribute("installDetailList", installDetailList);

		//add by 2019-11-29?????????????????????????????????????????????
		List<FlowWorkOpt>  flowOptList=flowWorkOptService.queryFlowOptByWork(work);

		//????????????????????????
		flowImagesOpt(flowOptList);

		model.addAttribute("flowOptList", flowOptList);//????????????????????????
		return "modules/cb/work/coverWorkDetailPage";
	}

	private void flowImagesOpt(List<FlowWorkOpt> flowOptList) {
		if(flowOptList!=null){
			String coverAppUrl = Global.getConfig("coverBell.api.url")+"/sys/file/download/";  //app??????url
			flowOptList.forEach(item->{
				try {
					String data = item.getData();
					JSONObject jsonObject = JSONObject.parseObject(data);
					String imgs = jsonObject.getString("imageIds"); //??????
					if(StringUtils.isNotBlank(imgs)){
						String[] arr = imgs.split(",");
						List<String> list = Stream.of(arr).map(a->coverAppUrl+a).collect(Collectors.toList());
						item.setImagesList(list);
						String optRemarks = jsonObject.getString("remarks");
						item.setOptRemarks(optRemarks);
					}
				} catch (Exception e) {
					//e.printStackTrace();
					logger.error("??????????????????????????????????????????:{}",e.getMessage());
				}
			});
		}
	}

	/**
	 * ??????????????????
	 * @param coverWork
	 * @param model
	 * @return
	 */
	@RequiresPermissions("cb:work:coverWork:audit")
	@RequestMapping(value = "auditPage")
	public String auditPage(CoverWork coverWork, Model model) {
		CoverWork work=coverWorkService.get(coverWork.getId());//????????????
		Cover cover=coverService.get(work.getCover().getId());// ????????????
		CoverBell coverBell=coverBellService.get(work.getCoverBellId());// ????????????

/*		CoverWorkOperation coverWorkOperation=new  CoverWorkOperation();
		coverWorkOperation.setCoverWork(work);*/
/*		List<CoverWorkOperation> operateionList = coverWorkOperationService.findList(coverWorkOperation);
		if(null!=operateionList&&operateionList.size()>0){
			for(CoverWorkOperation operation:operateionList){
				operation.setWorkOperationDetail(coverWorkOperationDetailService.obtainDetail(coverWork.getId()));
			}
		}*/

	/*	CoverWorkOperation workOperation=new CoverWorkOperation();//??????????????????(????????????)*/

		model.addAttribute("coverWork", work);//????????????
		model.addAttribute("cover", cover);// ????????????
		model.addAttribute("coverBell", coverBell);// ????????????
/*		model.addAttribute("workOperationList", operateionList);//??????????????????
		model.addAttribute("workOperation", workOperation);//??????????????????(????????????)*/

		//????????????????????????
		List<CoverWorkOperationDetail> installDetailList =coverWorkOperationDetailService.obtainDetailByWork(coverWork.getId(), CodeConstant.record_type.install);
		model.addAttribute("installDetailList", installDetailList);

		//add by 2019-11-29?????????????????????????????????????????????
		List<FlowWorkOpt>  flowOptList=flowWorkOptService.queryFlowOptByWork(work);
		//????????????????????????
		flowImagesOpt(flowOptList);
		model.addAttribute("flowOptList", flowOptList);//????????????????????????
		return "modules/cb/work/coverWorkAuditPage";
	}

	/**
	 * ????????????????????????
	 * @param coverWork
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequiresPermissions("cb:work:coverWork:audit")
	@RequestMapping(value = "saveAudit")
	public AjaxJson saveAudit(CoverWork coverWork, Model model, RedirectAttributes redirectAttributes) throws Exception{
		AjaxJson j = new AjaxJson();

		System.out.println("****************"+coverWork.getOperationResult());
		System.out.println("****************"+coverWork.getOperationStatus());
		AjaxJson temp = coverWorkService.auditCoverWork(coverWork);
		if(temp.isSuccess()){
			j.setSuccess(true);
			j.setMsg("???????????????????????????");

		}else{
			j.setSuccess(false);
			j.setMsg(temp.getMsg());
		}

		return j;
	}
}
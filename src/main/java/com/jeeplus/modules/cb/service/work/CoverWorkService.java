/*
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.antu.message.Message;
import com.antu.message.dispatch.MessageDispatcher;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.api.pojo.ApiResult;
import com.jeeplus.modules.api.utils.HttpClientUtil;
import com.jeeplus.modules.cb.entity.alarm.CoverBellAlarm;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.mapper.equinfo.CoverBellMapper;
import com.jeeplus.modules.cb.mapper.work.CoverWorkMapper;
import com.jeeplus.modules.cb.service.alarm.CoverBellAlarmService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.cv.service.equinfo.CoverOfficeOwnerService;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.cv.utils.EntityUtils;
import com.jeeplus.modules.flow.entity.base.FlowProc;
import com.jeeplus.modules.flow.entity.opt.FlowOpt;
import com.jeeplus.modules.flow.service.base.FlowProcService;
import com.jeeplus.modules.flow.service.opt.FlowOptService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ????????????Service
 *
 * @author crj
 * @version 2019-06-26
 */
@Service
@Transactional(readOnly = true)
public class CoverWorkService extends CrudService<CoverWorkMapper, CoverWork> {
    @Autowired
    private CoverBellMapper coverBellMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CoverService coverService;
    @Autowired
    private CoverWorkOperationService coverWorkOperationService;
    @Autowired
    private CoverWorkMapper coverWorkMapper;
    @Autowired
    private CoverBellAlarmService coverBellAlarmService;
    @Autowired
    private CoverOfficeOwnerService coverOfficeOwnerService;
    @Autowired
    private CoverCollectStatisMapper coverCollectStatisMapper;
    @Autowired
    private FlowProcService flowProcService;
    @Autowired
    private FlowOptService flowOptService;
    @Autowired
    private MessageDispatcher messageDispatcher;

    public CoverWork get(String id) {
        return super.get(id);
    }

    public List<CoverWork> findList(CoverWork coverWork) {
        return super.findList(coverWork);
    }

    public Page<CoverWork> findPage(Page<CoverWork> page, CoverWork coverWork) {
        return super.findPage(page, coverWork);
    }

    @Transactional(readOnly = false)
    public void save(CoverWork coverWork) {
        CoverBell bell = coverBellMapper.findUniqueByProperty("cover_id", coverWork.getCover().getId());
        if (null != bell) {
            coverWork.setCoverBellId(bell.getId());
        }

        //????????????
        User conUser = coverWork.getConstructionUser();
        if (null != conUser && !conUser.getId().equals("")) {//??????????????????
            User conuser2 = userMapper.get(conUser.getId());
            coverWork.setConstructionDepart(conuser2.getOffice());

            if (StringUtils.isNotEmpty(conuser2.getMobile())) {
                //coverWork.setPhone(conuser2.getMobile()==""?conuser2.getPhone():conuser2.getMobile());
                coverWork.setPhone(conuser2.getMobile());
            } else {
                coverWork.setPhone(conuser2.getPhone());
            }

        }


/*
		User user = UserUtils.getUser();
		Office office=null;
		if (StringUtils.isNotBlank(user.getId())){
			office=user.getOffice();
		}
		if(null!=office){
			if (coverWork.getIsNewRecord()){
				coverWork.setCreateDepart(office.getId());//????????????
				coverWork.setUpdateDepart(office.getId());//????????????
			}else{
				coverWork.setUpdateDepart(office.getId());//????????????
			}
		}
*/
        coverWork = preDepart(coverWork);
        boolean flag = false;
        if (coverWork.getIsNewRecord()) {
            flag = true;
        }
        super.save(coverWork);
        if (flag) {
            //coverWorkOperationService.createRecord(coverWork,CodeConstant.WORK_OPERATION_TYPE.CREATE,"??????????????????");
            coverWorkOperationService.createRecord(coverWork, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "??????????????????");
            messageDispatcher.publish("/workflow/create", Message.of(coverWork));
        }
    }


    @Transactional(readOnly = false)
    public void delete(CoverWork coverWork) {
        super.delete(coverWork);
    }

    //????????????????????????
    public CoverWork preDepart(CoverWork coverWork) {
        User user = UserUtils.getUser();
        Office office = null;
        if (StringUtils.isNotBlank(user.getId())) {
            office = user.getOffice();
        }
        if (null != office) {
            //????????????
            if (coverWork.getIsNewRecord()) {
                coverWork.setCreateDepart(office.getId());//????????????
            }
            coverWork.setUpdateDepart(office.getId());//????????????
        }

        return coverWork;
    }

    //??????????????????????????????
    @Transactional(readOnly = false)
    public void createWork(CoverBellAlarm coverBellAlarm) {
        CoverWork entity = new CoverWork();
        entity.setWorkNum(IdGen.getInfoCode("CW"));
        entity.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//????????????
        entity.setLifeCycle(CodeConstant.WORK_STATUS.INIT);//add by 2019-11-25??????????????????
        entity.setWorkType(CodeConstant.WORK_TYPE.ALARM);//????????????
        entity.setSource(coverBellAlarm.getId());//????????????
        if (StringUtils.isNotEmpty(coverBellAlarm.getCoverId())) {
            Cover cover = coverService.get(coverBellAlarm.getCoverId());
            entity.setCover(cover);
            entity.setLatitude(cover.getLatitude());
            entity.setLongitude(cover.getLongitude());
        }

        entity.setCoverNo(coverBellAlarm.getCoverNo());
        entity.setCoverBellId(coverBellAlarm.getCoverBellId());
        entity = preDepart(entity);
        super.save(entity);
        coverWorkOperationService.createRecord(entity, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "??????????????????");
        //coverWorkOperationService.createRecord(entity,CodeConstant.WORK_OPERATION_TYPE.CREATE,"??????????????????");
        messageDispatcher.publish("/workflow/create", Message.of(entity));
    }

    //??????????????????????????????

    @Transactional(readOnly = false)
    public void createWorkByBell(CoverBell coverBell) {
        CoverWork entity = new CoverWork();
        entity.setWorkNum(IdGen.getInfoCode("CW"));
        entity.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//????????????
        entity.setLifeCycle(CodeConstant.WORK_STATUS.INIT);//add by 2019-11-25??????????????????
        entity.setWorkType(CodeConstant.WORK_TYPE.ALARM);//????????????
        entity.setWorkLevel(CodeConstant.work_level.urgent);//??????????????????
        //entity.setSource(coverBellAlarm.getId());//????????????
        if (StringUtils.isNotEmpty(coverBell.getCoverId())) {
            Cover cover = coverService.get(coverBell.getCoverId());
            entity.setCover(cover);
            entity.setLatitude(cover.getLatitude());
            entity.setLongitude(cover.getLongitude());
            entity.setCoverNo(cover.getNo());
        }
        entity.setCoverBellId(coverBell.getId());
        entity = preDepart(entity);
        entity.setConstructionContent(coverBellAlarmService.queryAlarmTypeByBell(coverBell.getBellNo()));//?????????????????????????????????
        super.save(entity);
        coverWorkOperationService.createRecord(entity, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "????????????");
        //coverWorkOperationService.createRecord(entity,CodeConstant.WORK_OPERATION_TYPE.CREATE,"??????????????????");
        messageDispatcher.publish("/workflow/create", Message.of(entity));
    }

    /**
     * ?????????????????????2019-12-20??????
     * ?????????????????????????????????????????????
     *
     * @param coverIds ????????????
     */
    @Transactional(readOnly = false)
    public void createWorkForInstall(CoverWork coverWork, String coverIds) {
        coverWork.setBatch(IdGen.getInfoCode(null));
        //????????????
        User conUser = coverWork.getConstructionUser();
        Office office = null;
        if (null != conUser && !conUser.getId().equals("")) {//??????????????????
            User conuser2 = userMapper.get(conUser.getId());
            office = conuser2.getOffice();
            if (StringUtils.isNotEmpty(conuser2.getMobile())) {
                //coverWork.setPhone(conuser2.getMobile()==""?conuser2.getPhone():conuser2.getMobile());
                coverWork.setPhone(conuser2.getMobile());
            } else {
                coverWork.setPhone(conuser2.getPhone());
            }

        }
        if (StringUtils.isNotEmpty(coverIds)) {
            String[] ids = coverIds.split(",");
            for (String id : ids) {
                CoverWork work = EntityUtils.copyData(coverWork, CoverWork.class);
                Cover cover = coverService.get(id);
                work.setConstructionUser(conUser);
                if (null != office) {
                    work.setConstructionDepart(office);
                }
                work.setWorkNum(IdGen.getInfoCode("CW"));
                work.setCover(cover);
                work.setCoverNo(cover.getNo());
                work.setLatitude(cover.getLatitude());
                work.setLongitude(cover.getLongitude());
                work.setUpdateDate(new Date());
                work.setUpdateBy(UserUtils.getUser());
                if (null != work.getConstructionUser() && null != work.getConstructionDepart() && work.getConstructionUser().getId().equals("") && work.getConstructionDepart().getId().equals("")) {
                    //work.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//????????????

                    work.setLifeCycle(CodeConstant.WORK_STATUS.INIT);//add by 2019-11-25??????????????????
                }
                work = preDepart(work);
                super.save(work);
                messageDispatcher.publish("/workflow/create", Message.of(work));
                cover.setIsGwo(CodeConstant.cover_gwo.handle);
                coverService.save(cover);
                coverWorkOperationService.createRecord(work, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "????????????????????????");
                //coverWorkOperationService.createRecord(work,CodeConstant.WORK_OPERATION_TYPE.CREATE,"????????????????????????");
            }
        }

    }

    /**
     * 2019-12-20??????
     * ?????????????????????????????????????????????????????????????????????????????????
     *
     * @param coverIds ????????????
     */
    @Transactional(readOnly = false)
    public void createWorkForAll(CoverWork coverWork, String coverIds) {
        coverWork.setBatch(IdGen.getInfoCode(null));
        //????????????
        User conUser = coverWork.getConstructionUser();
        Office office = null;
        if (null != conUser && !conUser.getId().equals("")) {//??????????????????
            User conuser2 = userMapper.get(conUser.getId());
            office = conuser2.getOffice();
            if (StringUtils.isNotEmpty(conuser2.getMobile())) {
                coverWork.setPhone(conuser2.getMobile());
            } else {
                coverWork.setPhone(conuser2.getPhone());
            }

        }
        if (StringUtils.isNotEmpty(coverIds)) {
            String[] ids = coverIds.split(",");
            for (String id : ids) {
                CoverWork work = EntityUtils.copyData(coverWork, CoverWork.class);
                Cover cover = coverService.get(id);
                work.setConstructionUser(conUser);
                if (null != office) {
                    work.setConstructionDepart(office);
                }
                work.setWorkNum(IdGen.getInfoCode("CW"));
                work.setCover(cover);
                work.setCoverNo(cover.getNo());
                work.setLatitude(cover.getLatitude());
                work.setLongitude(cover.getLongitude());
                work.setUpdateDate(new Date());
                work.setUpdateBy(UserUtils.getUser());
                if (null != work.getConstructionUser() && null != work.getConstructionDepart() && work.getConstructionUser().getId().equals("") && work.getConstructionDepart().getId().equals("")) {
                    //work.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//????????????

                    work.setLifeCycle(CodeConstant.WORK_STATUS.INIT);//add by 2019-11-25??????????????????
                }
                work = preDepart(work);
                super.save(work);
                messageDispatcher.publish("/workflow/create", Message.of(work));
                if (coverWork.getWorkType().equals(CodeConstant.WORK_TYPE.INSTALL)) {
                    cover.setIsGwo(CodeConstant.cover_gwo.handle);
                    coverService.save(cover);
                }

                coverWorkOperationService.createRecord(work, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "????????????????????????");
                //coverWorkOperationService.createRecord(work,CodeConstant.WORK_OPERATION_TYPE.CREATE,"????????????????????????");
            }
        }

    }

    @Transactional(readOnly = false)
    public void workAssign(CoverWork coverWork) {

        String id = coverWork.getIds();//??????ID???
        //????????????
        User conUser = coverWork.getConstructionUser();
        Office office = null;
        if (null != conUser) {//??????????????????
            User conuser2 = userMapper.get(conUser.getId());
            office = conuser2.getOffice();
            if (StringUtils.isNotEmpty(conuser2.getMobile())) {
                //coverWork.setPhone(conuser2.getMobile()==""?conuser2.getPhone():conuser2.getMobile());
                coverWork.setPhone(conuser2.getMobile());
            } else {
                coverWork.setPhone(conuser2.getPhone());
            }

            //coverWork.setPhone(conuser2.getMobile()==""?conuser2.getPhone():conuser2.getMobile());
        }
        if (StringUtils.isNotEmpty(id)) {
            String[] ids = id.split(",");
            for (String s : ids) {
                CoverWork work = super.get(s);
                work.setConstructionUser(conUser);
                if (null != office) {
                    work.setConstructionDepart(office);
                }
                work.setUpdateDate(new Date());
                work.setUpdateBy(UserUtils.getUser());
                work.setPhone(coverWork.getPhone());// ????????????
                work.setWorkLevel(coverWork.getWorkLevel());// ????????????
                work.setConstructionContent(coverWork.getConstructionContent());// ????????????
                work.setWorkStatus(CodeConstant.WORK_STATUS.ASSIGN);
                coverWorkMapper.update(work);
                coverWorkOperationService.createRecord(work, CodeConstant.WORK_OPERATION_TYPE.ASSIGN, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "??????????????????");
                //coverWorkOperationService.createRecord(work,CodeConstant.WORK_OPERATION_TYPE.ASSIGN,"??????????????????");
            }
        }
    }

	/*@Transactional(readOnly = false)
	public boolean auditCoverWork(CoverWork coverWork){
		boolean flag=true;
		CoverWorkOperation workOperation=new CoverWorkOperation();//??????????????????(????????????)
		try {
			System.out.println("****************"+coverWork.getOperationResult());
			System.out.println("****************"+coverWork.getOperationStatus());
			String operationStatus=coverWork.getOperationStatus();// ????????????
			String operationResult=coverWork.getOperationResult();// ????????????
			//????????????????????????
			coverWorkOperationService.createRecord(coverWork,CodeConstant.WORK_OPERATION_TYPE.AUDIT,operationStatus,operationResult);
			CoverWork work=super.get(coverWork.getId());
			String workStatus=work.getWorkStatus();		// ????????????

			Cover cover=null;
			if(StringUtils.isNotEmpty(work.getCover().getId())){
				cover=coverService.get(work.getCover().getId());
			}


			//????????????
			if (com.jeeplus.common.utils.StringUtils.isNotEmpty(operationStatus) && operationStatus.equals(CodeConstant.WORK_OPERATION_STATUS.AUDIT_FAIL)) {
			*//*	if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_COMPLETE)){
					//???????????????????????????????????????????????????????????????

				}else if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_FAIL)){
					//???????????????????????????????????????????????????????????????

				}*//*
				work.setWorkStatus(CodeConstant.WORK_STATUS.ASSIGN);//?????????

				//????????????
			} else if (com.jeeplus.common.utils.StringUtils.isNotEmpty(operationStatus) && operationStatus.equals(CodeConstant.WORK_OPERATION_STATUS.AUDIT_PASS)) {
				if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_COMPLETE)){
					//????????????????????????????????????????????????????????????
					work.setWorkStatus(CodeConstant.WORK_STATUS.COMPLETE);//??????
					//add by 2019-10-24?????????????????????????????????????????????????????????
					cover.setIsGwo(CodeConstant.cover_gwo.install);
					cover.setCoverType(CodeConstant.cover_type.smart);//?????????????????????????????????????????????add by 2019-11-11

				}else if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_FAIL)){
					//????????????????????????????????????????????????????????????
					work.setWorkStatus(CodeConstant.WORK_STATUS.SCRAP); //??????
					//add by 2019-10-24?????????????????????????????????????????????????????????
					cover.setIsGwo(CodeConstant.cover_gwo.not_install);
				}

			}
			super.save(work);

			//add by 2019-10-24 ?????????????????????????????????????????????????????????????????????
			if(null!=cover&&work.getWorkType().equals(CodeConstant.WORK_TYPE.INSTALL)){
				coverService.save(cover);
			}



		}catch (Exception e){
			flag=false;
			e.printStackTrace();
		}
		return flag;
	}*/

    /**
     * 2019-11-29???????????????????????????
     *
     * @param coverWork ??????
     * @return ??????????????????
     */
    @Transactional(readOnly = false)
    public AjaxJson auditCoverWork(CoverWork coverWork) {
        AjaxJson j = new AjaxJson();
        try {
            User user = UserUtils.getUser();
            String operationStatus = coverWork.getOperationStatus();// ????????????
            String operationResult = coverWork.getOperationResult();// ????????????
            FlowOpt flowOpt = flowOptService.queryFlowByOpt(coverWork.getFlowId().getId(), "audit");//???????????????????????????????????????
            logger.info("*****??????id***********" + coverWork.getId());
            logger.info("*****???????????????id***********" + flowOpt.getId());
            logger.info("*****??????id***********" + user.getId());

            //????????????
            Map<String, Object> map = new HashMap<>();
            map.put("message", operationResult);
            map.put("result", operationStatus);
            String data = JSON.toJSONString(map);
            ApiResult apiResult = pushForApi(coverWork.getId(), flowOpt.getId(), user.getId(), data);
            if(apiResult==null){
                j.setSuccess(false);
                j.setMsg("Api????????????????????????????????????");
            }else{
                j.setMsg("Api??????????????????");
            }

        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg("??????????????????");
        }
        return j;
    }

    /**
     * @param billId    ??????id
     * @param flowOptId ??????id
     * @param userId    ??????id
     * @param data      ?????????json??????
     */
    public ApiResult pushForApi(String billId, String flowOptId, String userId, String data) {

        String apiUrl = Global.getConfig("coverBell.api.url") + "/workFlow/submitFlowOpts";
        Map<String, Object> param = new HashMap<>();
        param.put("billId", billId);
        param.put("flowOptId", flowOptId);
        param.put("userId", userId);
        param.put("data", data);
        try {
            logger.info("apiurl:{}",apiUrl);
            String str = HttpClientUtil.doPost(apiUrl, param);
            System.out.println("str:" + str);
            ApiResult result = JSONObject.parseObject(str, ApiResult.class);
            return result;
            //return result.getCode().equals("0");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * ????????????????????????????????????????????????????????????
     *
     * @param bellId   ??????ID
     * @param workType ????????????
     * @return {@code boolean}
     */
    public boolean queryCoverWork(String bellId, String workType) {
        //StringBuffer sqlBase=new StringBuffer("select id  from cover_work where work_status not in('complete','scrap')  ");
        StringBuilder sqlBase = new StringBuilder("select id  from cover_work where work_status not in('SH','E0','E1')  ");
        if (StringUtils.isNotEmpty(bellId)) {
            sqlBase.append(" and cover_bell_id='").append(bellId).append("'");
        }
        if (StringUtils.isNotEmpty(workType)) {
            sqlBase.append(" and work_type='").append(workType).append("'");
        }
        String sql = sqlBase.toString();
        List<Object> resultList = coverWorkMapper.execSelectSql(sql);

        return null != resultList && resultList.size() > 0;
    }


    /**
     * add by 2019-11-11
     * ????????????????????????????????????
     * ???????????????????????????????????????????????????,????????????????????????
     *
     * @param coverBellAlarm ??????????????????
     */
    @Transactional(readOnly = false)
    public void createCoverWork(CoverBellAlarm coverBellAlarm) {
        //???????????????????????????????????????????????????
        boolean flag = queryCoverWork(coverBellAlarm.getCoverBellId(), CodeConstant.WORK_TYPE.ALARM);
        if (!flag) {
            Cover cover = coverService.get(coverBellAlarm.getCoverId());
            CoverWork entity = new CoverWork();
            entity.setWorkNum(IdGen.getInfoCode("CW"));
            entity.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//????????????
            entity.setLifeCycle(CodeConstant.lifecycle.init);//add by 2019-11-25??????????????????
            entity.setWorkType(CodeConstant.WORK_TYPE.ALARM);//????????????
            entity.setSource(coverBellAlarm.getId());//????????????
            entity.setWorkLevel(CodeConstant.work_level.urgent);//??????????????????
            if (StringUtils.isNotEmpty(coverBellAlarm.getCoverId())) {
                //Cover cover=coverService.get(coverBellAlarm.getCoverId());
                entity.setCover(cover);
                entity.setLatitude(cover.getLatitude());
                entity.setLongitude(cover.getLongitude());
            }

            entity.setCoverNo(coverBellAlarm.getCoverNo());
            entity.setCoverBellId(coverBellAlarm.getCoverBellId());
            //entity=preDepart(entity);
            super.save(entity);
            coverWorkOperationService.createRecord(entity, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "????????????????????????");
            //???????????????????????????
            Office office = null;
            if (StringUtils.isNotEmpty(cover.getOwnerDepart())) {
                office = coverOfficeOwnerService.findOfficeByOwner(cover.getOwnerDepart());
            }
            List<FlowProc> flowProcList = null;
            if (null != office) {//add by 2019-11-25???????????????????????????????????????id
                flowProcList = flowProcService.queryFlowByOffice(office, CodeConstant.WORK_TYPE.ALARM);
            }
            if (CollectionUtil.isNotEmpty(flowProcList)) {//null!=flowProcList
                FlowProc flowProc = flowProcList.get(0);
                entity.setFlowId(flowProc);//????????????????????????
            }
            super.save(entity);
            coverWorkOperationService.createRecord(entity, CodeConstant.WORK_OPERATION_TYPE.ASSIGN, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "????????????????????????");
            messageDispatcher.publish("/workflow/create", Message.of(entity));
/*			if(null!=office){
				entity.setConstructionDepart(office);
				entity.setWorkStatus(CodeConstant.WORK_STATUS.ASSIGN);//????????????,?????????(??????????????????)
				super.save(entity);
				coverWorkOperationService.createRecord(entity,CodeConstant.WORK_OPERATION_TYPE.ASSIGN,CodeConstant.WORK_OPERATION_STATUS.SUCCESS,"????????????????????????");
				messageDispatcher.publish("/workflow/create", Message.of(entity));
			}*/

        }
    }


    /**
     * ??????????????????
     *
     * @return ????????????
     */
    @SuppressWarnings("rawtypes")
    public Map statisWork() {
        Map<String, Object> map = new HashMap<>();

        Integer assignNum;        // ???????????????
        String sql = " select count(id) as num from cover_work where work_status in('S11') and to_days(create_date) = to_days(now())  ";
        List<Map<String, Object>> resultList = coverCollectStatisMapper.selectBySql(sql);
        assignNum = indexStatisJobData(resultList, "num");
        map.put("assignNum", assignNum);

        Integer completeNum;        // ????????????
        String sql2 = " select count(id) as num from cover_work where work_status in('E0','E1')  ";
        List<Map<String, Object>> result2List = coverCollectStatisMapper.selectBySql(sql2);
        completeNum = indexStatisJobData(result2List, "num");
        map.put("completeNum", completeNum);

        Integer processingNum;        // ????????????
        String sql3 = " select count(id) as num from cover_work where work_status in('S18')  ";
        List<Map<String, Object>> result3List = coverCollectStatisMapper.selectBySql(sql3);
        processingNum = indexStatisJobData(result3List, "num");
        map.put("processingNum", processingNum);


        Integer overtimeNum;        // ???????????????
        String sql4 = " select count(id) as num from cover_work_overtime where del_flag='0'  ";
        List<Map<String, Object>> result4List = coverCollectStatisMapper.selectBySql(sql4);
        overtimeNum = indexStatisJobData(result4List, "num");
        map.put("overtimeNum", overtimeNum);

        Integer coverBellNum;        // ??????????????????
        String sql5 = " select count(id) as num  from cover where del_flag='0' and  is_gwo='Y'  ";
        List<Map<String, Object>> result5List = coverCollectStatisMapper.selectBySql(sql5);
        coverBellNum = indexStatisJobData(result5List, "num");
        map.put("coverBellNum", coverBellNum);

        return map;
    }

    private Integer indexStatisJobData(List<Map<String, Object>> rsList, String name) {

        int num = 0;
        if (CollectionUtils.isNotEmpty(rsList)) {
            Map<String, Object> result = rsList.get(0);

            if (result == null || !result.containsKey(name)) {
                num = 0;
            } else {
                num = Integer.parseInt(String.valueOf(result.get(name)));
            }
        }
        return num;
    }

}

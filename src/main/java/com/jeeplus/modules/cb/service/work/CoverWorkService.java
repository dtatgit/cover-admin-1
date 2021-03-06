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
 * 工单信息Service
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

        //施工人员
        User conUser = coverWork.getConstructionUser();
        if (null != conUser && !conUser.getId().equals("")) {//获取施工部门
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
				coverWork.setCreateDepart(office.getId());//创建部门
				coverWork.setUpdateDepart(office.getId());//更新部门
			}else{
				coverWork.setUpdateDepart(office.getId());//更新部门
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
            //coverWorkOperationService.createRecord(coverWork,CodeConstant.WORK_OPERATION_TYPE.CREATE,"后台创建工单");
            coverWorkOperationService.createRecord(coverWork, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "后台创建工单");
            messageDispatcher.publish("/workflow/create", Message.of(coverWork));
        }
    }


    @Transactional(readOnly = false)
    public void delete(CoverWork coverWork) {
        super.delete(coverWork);
    }

    //工单加上部门信息
    public CoverWork preDepart(CoverWork coverWork) {
        User user = UserUtils.getUser();
        Office office = null;
        if (StringUtils.isNotBlank(user.getId())) {
            office = user.getOffice();
        }
        if (null != office) {
            //更新部门
            if (coverWork.getIsNewRecord()) {
                coverWork.setCreateDepart(office.getId());//创建部门
            }
            coverWork.setUpdateDepart(office.getId());//更新部门
        }

        return coverWork;
    }

    //根据报警信息生成工单
    @Transactional(readOnly = false)
    public void createWork(CoverBellAlarm coverBellAlarm) {
        CoverWork entity = new CoverWork();
        entity.setWorkNum(IdGen.getInfoCode("CW"));
        entity.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//工单状态
        entity.setLifeCycle(CodeConstant.WORK_STATUS.INIT);//add by 2019-11-25新增生命周期
        entity.setWorkType(CodeConstant.WORK_TYPE.ALARM);//工单类型
        entity.setSource(coverBellAlarm.getId());//工单来源
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
        coverWorkOperationService.createRecord(entity, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "报警记录生成");
        //coverWorkOperationService.createRecord(entity,CodeConstant.WORK_OPERATION_TYPE.CREATE,"报警记录生成");
        messageDispatcher.publish("/workflow/create", Message.of(entity));
    }

    //根据井卫生成报警工单

    @Transactional(readOnly = false)
    public void createWorkByBell(CoverBell coverBell) {
        CoverWork entity = new CoverWork();
        entity.setWorkNum(IdGen.getInfoCode("CW"));
        entity.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//工单状态
        entity.setLifeCycle(CodeConstant.WORK_STATUS.INIT);//add by 2019-11-25新增生命周期
        entity.setWorkType(CodeConstant.WORK_TYPE.ALARM);//工单类型
        entity.setWorkLevel(CodeConstant.work_level.urgent);//工单紧急程度
        //entity.setSource(coverBellAlarm.getId());//工单来源
        if (StringUtils.isNotEmpty(coverBell.getCoverId())) {
            Cover cover = coverService.get(coverBell.getCoverId());
            entity.setCover(cover);
            entity.setLatitude(cover.getLatitude());
            entity.setLongitude(cover.getLongitude());
            entity.setCoverNo(cover.getNo());
        }
        entity.setCoverBellId(coverBell.getId());
        entity = preDepart(entity);
        entity.setConstructionContent(coverBellAlarmService.queryAlarmTypeByBell(coverBell.getBellNo()));//施工内容为报警工单类型
        super.save(entity);
        coverWorkOperationService.createRecord(entity, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "报警工单");
        //coverWorkOperationService.createRecord(entity,CodeConstant.WORK_OPERATION_TYPE.CREATE,"报警记录生成");
        messageDispatcher.publish("/workflow/create", Message.of(entity));
    }

    /**
     * 只针对安装井盖2019-12-20废弃
     * 根据井盖编号，批量生成安装工单
     *
     * @param coverIds 井盖编号
     */
    @Transactional(readOnly = false)
    public void createWorkForInstall(CoverWork coverWork, String coverIds) {
        coverWork.setBatch(IdGen.getInfoCode(null));
        //施工人员
        User conUser = coverWork.getConstructionUser();
        Office office = null;
        if (null != conUser && !conUser.getId().equals("")) {//获取施工部门
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
                    //work.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//工单状态

                    work.setLifeCycle(CodeConstant.WORK_STATUS.INIT);//add by 2019-11-25新增生命周期
                }
                work = preDepart(work);
                super.save(work);
                messageDispatcher.publish("/workflow/create", Message.of(work));
                cover.setIsGwo(CodeConstant.cover_gwo.handle);
                coverService.save(cover);
                coverWorkOperationService.createRecord(work, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "井盖安装工单生成");
                //coverWorkOperationService.createRecord(work,CodeConstant.WORK_OPERATION_TYPE.CREATE,"井盖安装工单生成");
            }
        }

    }

    /**
     * 2019-12-20新增
     * 根据井盖编号，批量生成工单（包含安装、修复等所有工单）
     *
     * @param coverIds 井盖编号
     */
    @Transactional(readOnly = false)
    public void createWorkForAll(CoverWork coverWork, String coverIds) {
        coverWork.setBatch(IdGen.getInfoCode(null));
        //施工人员
        User conUser = coverWork.getConstructionUser();
        Office office = null;
        if (null != conUser && !conUser.getId().equals("")) {//获取施工部门
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
                    //work.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//工单状态

                    work.setLifeCycle(CodeConstant.WORK_STATUS.INIT);//add by 2019-11-25新增生命周期
                }
                work = preDepart(work);
                super.save(work);
                messageDispatcher.publish("/workflow/create", Message.of(work));
                if (coverWork.getWorkType().equals(CodeConstant.WORK_TYPE.INSTALL)) {
                    cover.setIsGwo(CodeConstant.cover_gwo.handle);
                    coverService.save(cover);
                }

                coverWorkOperationService.createRecord(work, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "井盖安装工单生成");
                //coverWorkOperationService.createRecord(work,CodeConstant.WORK_OPERATION_TYPE.CREATE,"井盖安装工单生成");
            }
        }

    }

    @Transactional(readOnly = false)
    public void workAssign(CoverWork coverWork) {

        String id = coverWork.getIds();//设备ID号
        //施工人员
        User conUser = coverWork.getConstructionUser();
        Office office = null;
        if (null != conUser) {//获取施工部门
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
                work.setPhone(coverWork.getPhone());// 联系电话
                work.setWorkLevel(coverWork.getWorkLevel());// 紧急程度
                work.setConstructionContent(coverWork.getConstructionContent());// 施工内容
                work.setWorkStatus(CodeConstant.WORK_STATUS.ASSIGN);
                coverWorkMapper.update(work);
                coverWorkOperationService.createRecord(work, CodeConstant.WORK_OPERATION_TYPE.ASSIGN, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "工单任务分配");
                //coverWorkOperationService.createRecord(work,CodeConstant.WORK_OPERATION_TYPE.ASSIGN,"工单任务分配");
            }
        }
    }

	/*@Transactional(readOnly = false)
	public boolean auditCoverWork(CoverWork coverWork){
		boolean flag=true;
		CoverWorkOperation workOperation=new CoverWorkOperation();//工单操作记录(审核记录)
		try {
			System.out.println("****************"+coverWork.getOperationResult());
			System.out.println("****************"+coverWork.getOperationStatus());
			String operationStatus=coverWork.getOperationStatus();// 操作状态
			String operationResult=coverWork.getOperationResult();// 操作结果
			//生成工单审核记录
			coverWorkOperationService.createRecord(coverWork,CodeConstant.WORK_OPERATION_TYPE.AUDIT,operationStatus,operationResult);
			CoverWork work=super.get(coverWork.getId());
			String workStatus=work.getWorkStatus();		// 工单状态

			Cover cover=null;
			if(StringUtils.isNotEmpty(work.getCover().getId())){
				cover=coverService.get(work.getCover().getId());
			}


			//审核失败
			if (com.jeeplus.common.utils.StringUtils.isNotEmpty(operationStatus) && operationStatus.equals(CodeConstant.WORK_OPERATION_STATUS.AUDIT_FAIL)) {
			*//*	if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_COMPLETE)){
					//处理完成的工单审核失败，工单状态为：已指派

				}else if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_FAIL)){
					//处理失败的工单审核失败，工单状态为：已指派

				}*//*
				work.setWorkStatus(CodeConstant.WORK_STATUS.ASSIGN);//已指派

				//审核成功
			} else if (com.jeeplus.common.utils.StringUtils.isNotEmpty(operationStatus) && operationStatus.equals(CodeConstant.WORK_OPERATION_STATUS.AUDIT_PASS)) {
				if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_COMPLETE)){
					//处理完成的工单审核成功，工单状态为：结束
					work.setWorkStatus(CodeConstant.WORK_STATUS.COMPLETE);//结束
					//add by 2019-10-24工单结束后，井盖中安装工单状态为已安装
					cover.setIsGwo(CodeConstant.cover_gwo.install);
					cover.setCoverType(CodeConstant.cover_type.smart);//智能化井盖为已经安装井卫的井盖add by 2019-11-11

				}else if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_FAIL)){
					//处理失败的工单审核成功，工单状态为：废弃
					work.setWorkStatus(CodeConstant.WORK_STATUS.SCRAP); //废弃
					//add by 2019-10-24工单废弃后，井盖中安装工单状态为未安装
					cover.setIsGwo(CodeConstant.cover_gwo.not_install);
				}

			}
			super.save(work);

			//add by 2019-10-24 只有安装工单才需要维护井盖基础信息安装工单状态
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
     * 2019-11-29新版工作流工单审核
     *
     * @param coverWork 工单
     * @return 是否审核成功
     */
    @Transactional(readOnly = false)
    public AjaxJson auditCoverWork(CoverWork coverWork) {
        AjaxJson j = new AjaxJson();
        try {
            User user = UserUtils.getUser();
            String operationStatus = coverWork.getOperationStatus();// 操作状态
            String operationResult = coverWork.getOperationResult();// 操作结果
            FlowOpt flowOpt = flowOptService.queryFlowByOpt(coverWork.getFlowId().getId(), "audit");//获取需要审核的工单操作信息
            logger.info("*****工单id***********" + coverWork.getId());
            logger.info("*****工作流操作id***********" + flowOpt.getId());
            logger.info("*****用户id***********" + user.getId());

            //审核信息
            Map<String, Object> map = new HashMap<>();
            map.put("message", operationResult);
            map.put("result", operationStatus);
            String data = JSON.toJSONString(map);
            ApiResult apiResult = pushForApi(coverWork.getId(), flowOpt.getId(), user.getId(), data);
            if(apiResult==null){
                j.setSuccess(false);
                j.setMsg("Api接口请求失败，请联系运维");
            }else{
                j.setMsg("Api接口请求成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg("工单审核异常");
        }
        return j;
    }

    /**
     * @param billId    工单id
     * @param flowOptId 操作id
     * @param userId    用户id
     * @param data      数据域json数据
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
     * 判断指定的井卫是否可以生成指定类型的工单
     *
     * @param bellId   井卫ID
     * @param workType 工单类型
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
     * 井卫报警自动生成报警工单
     * 对报警工单根据井盖维护单位自动派单,注意：无权属单位
     *
     * @param coverBellAlarm 井卫报警信息
     */
    @Transactional(readOnly = false)
    public void createCoverWork(CoverBellAlarm coverBellAlarm) {
        //需要校验该井卫不能重复生成报警工单
        boolean flag = queryCoverWork(coverBellAlarm.getCoverBellId(), CodeConstant.WORK_TYPE.ALARM);
        if (!flag) {
            Cover cover = coverService.get(coverBellAlarm.getCoverId());
            CoverWork entity = new CoverWork();
            entity.setWorkNum(IdGen.getInfoCode("CW"));
            entity.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//工单状态
            entity.setLifeCycle(CodeConstant.lifecycle.init);//add by 2019-11-25新增生命周期
            entity.setWorkType(CodeConstant.WORK_TYPE.ALARM);//工单类型
            entity.setSource(coverBellAlarm.getId());//工单来源
            entity.setWorkLevel(CodeConstant.work_level.urgent);//工单紧急程度
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
            coverWorkOperationService.createRecord(entity, CodeConstant.WORK_OPERATION_TYPE.CREATE, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "自动生成报警工单");
            //获取井盖的维护部门
            Office office = null;
            if (StringUtils.isNotEmpty(cover.getOwnerDepart())) {
                office = coverOfficeOwnerService.findOfficeByOwner(cover.getOwnerDepart());
            }
            List<FlowProc> flowProcList = null;
            if (null != office) {//add by 2019-11-25根据维护单位来获取工单流程id
                flowProcList = flowProcService.queryFlowByOffice(office, CodeConstant.WORK_TYPE.ALARM);
            }
            if (CollectionUtil.isNotEmpty(flowProcList)) {//null!=flowProcList
                FlowProc flowProc = flowProcList.get(0);
                entity.setFlowId(flowProc);//工单中新增工作流
            }
            super.save(entity);
            coverWorkOperationService.createRecord(entity, CodeConstant.WORK_OPERATION_TYPE.ASSIGN, CodeConstant.WORK_OPERATION_STATUS.SUCCESS, "报警工单自动分配");
            messageDispatcher.publish("/workflow/create", Message.of(entity));
/*			if(null!=office){
				entity.setConstructionDepart(office);
				entity.setWorkStatus(CodeConstant.WORK_STATUS.ASSIGN);//工单状态,已指派(原为：待接收)
				super.save(entity);
				coverWorkOperationService.createRecord(entity,CodeConstant.WORK_OPERATION_TYPE.ASSIGN,CodeConstant.WORK_OPERATION_STATUS.SUCCESS,"报警工单自动分配");
				messageDispatcher.publish("/workflow/create", Message.of(entity));
			}*/

        }
    }


    /**
     * 统计工单数据
     *
     * @return 统计数据
     */
    @SuppressWarnings("rawtypes")
    public Map statisWork() {
        Map<String, Object> map = new HashMap<>();

        Integer assignNum;        // 今日派单数
        String sql = " select count(id) as num from cover_work where work_status in('S11') and to_days(create_date) = to_days(now())  ";
        List<Map<String, Object>> resultList = coverCollectStatisMapper.selectBySql(sql);
        assignNum = indexStatisJobData(resultList, "num");
        map.put("assignNum", assignNum);

        Integer completeNum;        // 处理完成
        String sql2 = " select count(id) as num from cover_work where work_status in('E0','E1')  ";
        List<Map<String, Object>> result2List = coverCollectStatisMapper.selectBySql(sql2);
        completeNum = indexStatisJobData(result2List, "num");
        map.put("completeNum", completeNum);

        Integer processingNum;        // 待完成数
        String sql3 = " select count(id) as num from cover_work where work_status in('S18')  ";
        List<Map<String, Object>> result3List = coverCollectStatisMapper.selectBySql(sql3);
        processingNum = indexStatisJobData(result3List, "num");
        map.put("processingNum", processingNum);


        Integer overtimeNum;        // 超时工单数
        String sql4 = " select count(id) as num from cover_work_overtime where del_flag='0'  ";
        List<Map<String, Object>> result4List = coverCollectStatisMapper.selectBySql(sql4);
        overtimeNum = indexStatisJobData(result4List, "num");
        map.put("overtimeNum", overtimeNum);

        Integer coverBellNum;        // 井盖监控总数
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

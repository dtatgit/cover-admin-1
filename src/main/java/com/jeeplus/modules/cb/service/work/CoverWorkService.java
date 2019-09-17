/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import java.util.Date;
import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.cb.entity.alarm.CoverBellAlarm;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.entity.work.CoverWorkOperation;
import com.jeeplus.modules.cb.mapper.equinfo.CoverBellMapper;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.entity.equinfo.CoverAudit;
import com.jeeplus.modules.cv.entity.equinfo.CoverHistory;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import com.jeeplus.modules.cv.utils.EntityUtils;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.mapper.work.CoverWorkMapper;

/**
 * 工单信息Service
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
		 CoverBell bell=coverBellMapper.findUniqueByProperty("cover_id", coverWork.getCover().getId());
		 if(null!=bell){
			 coverWork.setCoverBellId(bell.getId());
		 }

		 //施工人员
		User conUser=coverWork.getConstructionUser();
		if(null!=conUser){//获取施工部门
			User conuser2=userMapper.get(conUser.getId());
			coverWork.setConstructionDepart(conuser2.getOffice());
			coverWork.setPhone(conuser2.getMobile()==""?conuser2.getMobile():conuser2.getPhone());
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
		coverWork=preDepart(coverWork);
		boolean flag=false;
		if (coverWork.getIsNewRecord()){
			flag=true;
		}
		super.save(coverWork);
		if(flag){
			//coverWorkOperationService.createRecord(coverWork,CodeConstant.WORK_OPERATION_TYPE.CREATE,"后台创建工单");
			coverWorkOperationService.createRecord(coverWork,CodeConstant.WORK_OPERATION_TYPE.CREATE,CodeConstant.WORK_OPERATION_STATUS.SUCCESS,"后台创建工单");
		}
	}

	
	@Transactional(readOnly = false)
	public void delete(CoverWork coverWork) {
		super.delete(coverWork);
	}

	//工单加上部门信息
	public  CoverWork preDepart(CoverWork coverWork){
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

		return coverWork;
	}
	//根据报警信息生成工单
	@Transactional(readOnly = false)
	public void createWork(CoverBellAlarm coverBellAlarm) {
		CoverWork entity = new CoverWork();
		entity.setWorkNum(IdGen.getInfoCode("CW"));
		entity.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//工单状态
		entity.setWorkType(CodeConstant.WORK_TYPE.ALARM);//工单类型
		entity.setSource(coverBellAlarm.getId());//工单来源
		if(StringUtils.isNotEmpty(coverBellAlarm.getCoverId())){
			Cover cover=coverService.get(coverBellAlarm.getCoverId());
			entity.setCover(cover);
			entity.setLatitude(cover.getLatitude());
			entity.setLongitude(cover.getLongitude());
		}

		entity.setCoverNo(coverBellAlarm.getCoverNo());
		entity.setCoverBellId(coverBellAlarm.getCoverBellId());
		entity=preDepart(entity);
		super.save(entity);
		coverWorkOperationService.createRecord(entity,CodeConstant.WORK_OPERATION_TYPE.CREATE,CodeConstant.WORK_OPERATION_STATUS.SUCCESS,"报警记录生成");
		//coverWorkOperationService.createRecord(entity,CodeConstant.WORK_OPERATION_TYPE.CREATE,"报警记录生成");
	}

	/**
	 *根据井盖编号，批量生成安装工单
	 * @param coverIds 井盖编号
	 */
	@Transactional(readOnly = false)
	public void createWorkForInstall(CoverWork coverWork,String coverIds) {
		coverWork.setBatch(IdGen.getInfoCode(null));
		//施工人员
		User conUser=coverWork.getConstructionUser();
		Office office=null;
		if(null!=conUser&&!conUser.getId().equals("")){//获取施工部门
			User conuser2=userMapper.get(conUser.getId());
			office=conuser2.getOffice();
			coverWork.setPhone(conuser2.getMobile()==""?conuser2.getPhone():conuser2.getMobile());
		}
		if(StringUtils.isNotEmpty(coverIds)){
			String [] ids = coverIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				CoverWork work= EntityUtils.copyData(coverWork,CoverWork.class);
				Cover cover=coverService.get(ids[i]);
				work.setConstructionUser(conUser);
				if(null!=office){
					work.setConstructionDepart(office);
				}
				work.setWorkNum(IdGen.getInfoCode("CW"));
				work.setCover(cover);
				work.setCoverNo(cover.getNo());
				work.setLatitude(cover.getLatitude());
				work.setLongitude(cover.getLongitude());
				work.setUpdateDate(new Date());
				work.setUpdateBy(UserUtils.getUser());
				if(work.getConstructionUser().getId().equals("")&&work.getConstructionDepart().getId().equals("")){
					work.setWorkStatus(CodeConstant.WORK_STATUS.INIT);//工单状态
				}
				work=preDepart(work);
				super.save(work);
				cover.setIsGwo(CodeConstant.BOOLEAN.YES);
				coverService.save(cover);
				coverWorkOperationService.createRecord(work,CodeConstant.WORK_OPERATION_TYPE.CREATE,CodeConstant.WORK_OPERATION_STATUS.SUCCESS,"井盖安装工单生成");
				//coverWorkOperationService.createRecord(work,CodeConstant.WORK_OPERATION_TYPE.CREATE,"井盖安装工单生成");
			}
		}

	}

	@Transactional(readOnly = false)
	public void workAssign(CoverWork coverWork){

		String id = coverWork.getIds();//设备ID号
		//施工人员
		User conUser=coverWork.getConstructionUser();
		Office office=null;
		if(null!=conUser&&!conUser.equals("")){//获取施工部门
			User conuser2=userMapper.get(conUser.getId());
			office=conuser2.getOffice();
			coverWork.setPhone(conuser2.getMobile()==""?conuser2.getPhone():conuser2.getMobile());
		}
		if(StringUtils.isNotEmpty(id)){
			String [] ids = id.split(",");
			for (int i = 0; i < ids.length; i++) {
				CoverWork work=super.get(ids[i]);
				work.setConstructionUser(conUser);
				if(null!=office){
					work.setConstructionDepart(office);
				}
				work.setUpdateDate(new Date());
				work.setUpdateBy(UserUtils.getUser());
				work.setPhone(coverWork.getPhone());// 联系电话
				work.setWorkLevel(coverWork.getWorkLevel());// 紧急程度
				work.setConstructionContent(coverWork.getConstructionContent());// 施工内容
				work.setWorkStatus(CodeConstant.WORK_STATUS.ASSIGN);
				coverWorkMapper.update(work);
				coverWorkOperationService.createRecord(work,CodeConstant.WORK_OPERATION_TYPE.ASSIGN,CodeConstant.WORK_OPERATION_STATUS.SUCCESS,"工单任务分配");
				//coverWorkOperationService.createRecord(work,CodeConstant.WORK_OPERATION_TYPE.ASSIGN,"工单任务分配");
			}
		}
	}

	@Transactional(readOnly = false)
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
			//审核失败
			if (com.jeeplus.common.utils.StringUtils.isNotEmpty(operationStatus) && operationStatus.equals(CodeConstant.WORK_OPERATION_STATUS.AUDIT_FAIL)) {
			/*	if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_COMPLETE)){
					//处理完成的工单审核失败，工单状态为：已指派

				}else if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_FAIL)){
					//处理失败的工单审核失败，工单状态为：已指派

				}*/
				work.setWorkStatus(CodeConstant.WORK_STATUS.ASSIGN);//已指派

				//审核成功
			} else if (com.jeeplus.common.utils.StringUtils.isNotEmpty(operationStatus) && operationStatus.equals(CodeConstant.WORK_OPERATION_STATUS.AUDIT_PASS)) {
				if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_COMPLETE)){
					//处理完成的工单审核成功，工单状态为：结束
					work.setWorkStatus(CodeConstant.WORK_STATUS.COMPLETE);//结束
				}else if(StringUtils.isNotBlank(workStatus)&&workStatus.equals(CodeConstant.WORK_STATUS.PROCESS_FAIL)){
					//处理失败的工单审核成功，工单状态为：废弃
					work.setWorkStatus(CodeConstant.WORK_STATUS.SCRAP); //废弃
				}

			}
			super.save(work);
		}catch (Exception e){
			flag=false;
			e.printStackTrace();
		}
		return flag;
	}
}
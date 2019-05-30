/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.task;

import java.util.Date;
import java.util.List;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.entity.equinfo.CoverAudit;
import com.jeeplus.modules.cv.entity.equinfo.CoverOwner;
import com.jeeplus.modules.cv.entity.task.CoverTaskInfo;
import com.jeeplus.modules.cv.mapper.equinfo.CoverMapper;
import com.jeeplus.modules.cv.mapper.equinfo.CoverOwnerMapper;
import com.jeeplus.modules.cv.mapper.task.CoverTaskInfoMapper;
import com.jeeplus.modules.cv.service.equinfo.CoverOwnerService;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.task.CoverTaskProcess;
import com.jeeplus.modules.cv.mapper.task.CoverTaskProcessMapper;

/**
 * 任务处理明细Service
 * @author crj
 * @version 2019-05-16
 */
@Service
@Transactional(readOnly = true)
public class CoverTaskProcessService extends CrudService<CoverTaskProcessMapper, CoverTaskProcess> {
	@Autowired
	private CoverTaskProcessMapper coverTaskProcessMapper;
	@Autowired
	private CoverTaskInfoMapper coverTaskInfoMapper;
	@Autowired
	private CoverTaskInfoService coverTaskInfoService;
	@Autowired
	private CoverOwnerService coverOwnerService;
	@Autowired
	private CoverOwnerMapper coverOwnerMapper;
	@Autowired
	private CoverMapper coverMapper;
	@Autowired
	private UserMapper userMapper;

	public CoverTaskProcess get(String id) {
		CoverTaskProcess p=super.get(id);
		if(null!=p){
			if(null!=p.getCover()&&StringUtils.isNotEmpty(p.getCover().getId())){
				p.setCover(coverMapper.get(p.getCover().getId()));
			}
			if(null!=p.getAuditUser()&&StringUtils.isNotEmpty(p.getAuditUser().getId())){
				p.setAuditUser(userMapper.get(p.getAuditUser().getId()));
			}
		}

		return p;
	}
	
	public List<CoverTaskProcess> findList(CoverTaskProcess coverTaskProcess) {
		List<CoverTaskProcess> processList=super.findList(coverTaskProcess);
		if(null!=processList&&processList.size()>0){
		for(CoverTaskProcess p:processList){
			if(null!=p.getCover()&&StringUtils.isNotEmpty(p.getCover().getId())){
				p.setCover(coverMapper.get(p.getCover().getId()));
			}
			if(null!=p.getAuditUser()&&StringUtils.isNotEmpty(p.getAuditUser().getId())){
				p.setAuditUser(userMapper.get(p.getAuditUser().getId()));
			}
		}
		}
		return processList;
	}
	
	public Page<CoverTaskProcess> findPage(Page<CoverTaskProcess> page, CoverTaskProcess coverTaskProcess) {
		//return super.findPage(page, coverTaskProcess);
		Page<CoverTaskProcess> pageValue=super.findPage(page, coverTaskProcess);
		List<CoverTaskProcess> list=pageValue.getList();
		if(null!=list&&list.size()>0){
			for(CoverTaskProcess p:list){
				if(null!=p.getCover()&&StringUtils.isNotEmpty(p.getCover().getId())){
					p.setCover(coverMapper.get(p.getCover().getId()));
				}
				if(null!=p.getAuditUser()&&StringUtils.isNotEmpty(p.getAuditUser().getId())){
					p.setAuditUser(userMapper.get(p.getAuditUser().getId()));
				}
			}
		}
		//return super.findPage(page, cover);
		return pageValue;
	}
	
	@Transactional(readOnly = false)
	public void save(CoverTaskProcess coverTaskProcess) {
		super.save(coverTaskProcess);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverTaskProcess coverTaskProcess) {
		super.delete(coverTaskProcess);
	}
	/**
	 * 生成任务明细数据
	 * @param coverList 任务数据明细
	 */
	@Transactional(readOnly = false)
	public void generateTaskPro(CoverTaskInfo coverTaskInfo, List<Cover> coverList){
		int i=0;
		if(null!=coverList&&coverList.size()>0){
			for(Cover cover:coverList){
				CoverTaskProcess process=new CoverTaskProcess();
				process.setCover(cover);
				process.setTaskStatus(CodeConstant.TASK_STATUS.ASSIGN);
				process.setCoverTaskInfo(coverTaskInfo);
				super.save(process);
				i=i++;
				logger.info("************任务编号*******************"+coverTaskInfo.getTaskNo());
				logger.info("************生成任务明细数据*******************"+i);
			}
		}
	}


	@Transactional(readOnly = false)
	public String obtainCover(CoverTaskInfo coverTaskInfo){
		String resultRerurn="";
		List<CoverTaskProcess> processList = null;
		try {
			String taskId=coverTaskInfo.getId();

			if (StringUtils.isNotEmpty(taskId) ) {
				//获取该任务下所有已分配任务明细
				CoverTaskProcess coverTaskProcess=new CoverTaskProcess();
				coverTaskProcess.setCoverTaskInfo(coverTaskInfo);
				coverTaskProcess.setTaskStatus(CodeConstant.TASK_STATUS.ASSIGN);
				processList=coverTaskProcessMapper.findList(coverTaskProcess);

			}
			System.out.println("*****************"+processList.size());
			if (null != processList && processList.size() > 0) {
				for (CoverTaskProcess process : processList) {
					User user = UserUtils.getUser();
					int flag = coverTaskProcessMapper.updateForProcess(process.getId());//返回1更新成功，返回0更新失败
					logger.info("*********更新待处理任务明细返回结果********" + flag);
					if (flag == 1) {
						//生成明细任务处理记录

					    String 	coverId=process.getCover().getId();
						process.setAuditUser(user);
						process.setAuditTime(new Date());
						process.setTaskStatus(CodeConstant.TASK_STATUS.PROCESSING);
						coverTaskProcessMapper.update(process);
						//判断任务是否为处理中，如果不是，则改为处理中
						String taskStatus=coverTaskInfo.getTaskStatus();
						if(StringUtils.isNotEmpty(taskStatus)&&!taskStatus.equals(CodeConstant.TASK_STATUS.PROCESSING)){
							coverTaskInfo.setTaskStatus(CodeConstant.TASK_STATUS.PROCESSING);
							coverTaskInfoService.save(coverTaskInfo);
						}
						resultRerurn=process.getId();
						break;
					}


				}
			}
		}catch(Exception e){
			resultRerurn="";
			e.printStackTrace();
		}

		return resultRerurn;
	}

	/**
	 * 任务处理完成
	 * @param cover
	 */
	@Transactional(readOnly = false)
	public void taskProcessComplete(Cover cover){
		String	coverTaskProcessId=cover.getCoverTaskProcessId();
		CoverTaskProcess coverTaskProcess= super.get(coverTaskProcessId);
		coverTaskProcess.setTaskStatus(CodeConstant.TASK_STATUS.COMPLETE);
		super.save(coverTaskProcess);//修改任务明细为完成状态

		/***********************查询该任务下是否还有未完成的信息*********************************/
		String coverTaskInfoId=coverTaskProcess.getCoverTaskInfo().getId();
		CoverTaskInfo coverTaskInfo=coverTaskInfoService.get(coverTaskInfoId);
		coverTaskInfo.getTaskNum();//任务数量
		CoverTaskProcess query=new CoverTaskProcess();
		query.setCoverTaskInfo(coverTaskInfo);
		query.setTaskStatus(CodeConstant.TASK_STATUS.COMPLETE);
		List<CoverTaskProcess> completeList = coverTaskProcessMapper.findList(query);
		if(String.valueOf(completeList.size()).equals(coverTaskInfo.getTaskNum())){
			coverTaskInfo.setTaskStatus(CodeConstant.TASK_STATUS.COMPLETE);
			//coverTaskInfoService.save(coverTaskInfo);
			coverTaskInfoMapper.update(coverTaskInfo);
		}else if(coverTaskInfo.getTaskStatus().equals(CodeConstant.TASK_STATUS.ASSIGN)){
			coverTaskInfo.setTaskStatus(CodeConstant.TASK_STATUS.PROCESSING);
			//coverTaskInfoService.save(coverTaskInfo);
			coverTaskInfoMapper.update(coverTaskInfo);
		}
	}
	@Transactional(readOnly = false)
	public boolean updateForProcess(CoverTaskProcess coverTaskProcess){
		int flag = coverTaskProcessMapper.updateForProcess(coverTaskProcess.getId());//返回1更新成功，返回0更新失败
		if(flag==1){
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 任务处理完成
	 * @param coverTaskProcess
	 */
	@Transactional(readOnly = false)
	public void taskProcessComplete(CoverTaskProcess coverTaskProcess){
		super.save(coverTaskProcess);//修改任务明细状态

		/***********************查询该任务下是否还有未完成的信息*********************************/
		String coverTaskInfoId=coverTaskProcess.getCoverTaskInfo().getId();
		CoverTaskInfo coverTaskInfo=coverTaskInfoService.get(coverTaskInfoId);
		coverTaskInfo.getTaskNum();//任务数量
		CoverTaskProcess query=new CoverTaskProcess();
		query.setCoverTaskInfo(coverTaskInfo);
		query.setTaskStatus(CodeConstant.TASK_STATUS.COMPLETE);
		List<CoverTaskProcess> completeList = coverTaskProcessMapper.findList(query);
		if(String.valueOf(completeList.size()).equals(coverTaskInfo.getTaskNum())){
			coverTaskInfo.setTaskStatus(CodeConstant.TASK_STATUS.COMPLETE);
			//coverTaskInfoService.save(coverTaskInfo);
			coverTaskInfoMapper.update(coverTaskInfo);
		}else if(coverTaskInfo.getTaskStatus().equals(CodeConstant.TASK_STATUS.ASSIGN)){
			coverTaskInfo.setTaskStatus(CodeConstant.TASK_STATUS.PROCESSING);
			//coverTaskInfoService.save(coverTaskInfo);
			coverTaskInfoMapper.update(coverTaskInfo);
		}
	}

	/**
	 *操作之前状态改为：处理中
	 一、确定
	 1.当前部门设为权属单位
	 2.移除，其他和不明归属2个权属单位
	 3.状态改为已经完成

	 二、取消的时候
	 1.状态改为：已分配，等待下一个部门来认领
	 2.当前部门如果是权属部门，则移除，改为其他
	 * @param ownerResult 当前部门是否是权属单位，Y是，N否
	 * @param ownerResult    井盖信息
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean assignOwner(String ownerResult,Cover cover){
		String coverTaskProcessId=cover.getCoverTaskProcessId();//获取任务明细id
		System.out.println("*******coverTaskProcessId***********"+coverTaskProcessId);
		CoverTaskProcess coverTaskProcess=coverTaskProcessMapper.get(coverTaskProcessId);

		User user = UserUtils.getUser();
		String officeName=user.getOffice().getName();
		List<CoverOwner> ownerList=coverOwnerService.obtainOwner(cover.getId());//权属单位集合
		if(StringUtils.isNotEmpty(ownerResult)&&ownerResult.equals("Y")){//确认操作
			configOwner(officeName,ownerList,cover);
			coverTaskProcess.setTaskStatus(CodeConstant.TASK_STATUS.COMPLETE);
		}else if(StringUtils.isNotEmpty(ownerResult)&&ownerResult.equals("N")){//取消操作
			cancelOwner(officeName,ownerList,cover);
			//coverTaskProcess.setTaskStatus(CodeConstant.TASK_STATUS.ASSIGN);
			//
			coverTaskProcess.setTaskStatus(CodeConstant.TASK_STATUS.COMPLETE);
		}
		coverTaskProcess.setAuditTime(new Date());
		coverTaskProcess.setAuditUser(user);
		taskProcessComplete(coverTaskProcess);
		return true;
	}

	/**
	 * 一、确定
	 1.当前部门设为权属单位
	 2.移除，其他和不明归属2个权属单位
	 3.状态改为已经完成

	 * @param officeName 当前部门名称
	 * @param ownerList  当前井盖权属单位集合
	 * @param cover   当前井盖信息
	 */

	public void configOwner(String officeName,List<CoverOwner> ownerList,Cover cover){
		boolean flag=false;
		if(null!=ownerList&&ownerList.size()>0){
			for(CoverOwner coverOwner : ownerList){
				String	name=coverOwner.getOwnerName();
				if(name.equals("其他")||name.equals("不明归属")){
					coverOwner.setDelFlag(CoverOwner.DEL_FLAG_DELETE);
				}
				if(name.equals(officeName)){
					flag=true;
				}
			}
		}
		if(!flag){
			CoverOwner coverOwner=new CoverOwner();
			coverOwner.setOwnerName(officeName);
			coverOwner.setCoverId(cover.getId());
			//coverOwner.preInsert();
			coverOwner.setOwnerType("org");
			coverOwner.setDelFlag(CoverOwner.DEL_FLAG_NORMAL);
			ownerList.add(coverOwner);
		}
		ownerHandle(ownerList,cover);

	}

	/**
	 *  二、取消的时候
	 1.状态改为：已分配，等待下一个部门来认领
	 2.当前部门如果是权属部门，则移除，改为其他
	 * @param officeName
	 * @param ownerList
	 * @param cover
	 */

	public void cancelOwner(String officeName,List<CoverOwner> ownerList,Cover cover){
		boolean flag=false;
		if(null!=ownerList&&ownerList.size()>0){
			for(CoverOwner coverOwner : ownerList){
				String	name=coverOwner.getOwnerName();
				if(name.equals(officeName)){
					coverOwner.setDelFlag(CoverOwner.DEL_FLAG_DELETE);
				}
				if(name.equals("其他")){
					flag=true;
				}
			}
		}
		if(!flag&&ownerList.size()==0){
			CoverOwner coverOwner=new CoverOwner();
			coverOwner.setOwnerName("其他");
			coverOwner.setCoverId(cover.getId());
			//coverOwner.preInsert();
			coverOwner.setOwnerType("org");
			coverOwner.setDelFlag(CoverOwner.DEL_FLAG_NORMAL);
			ownerList.add(coverOwner);
		}
		ownerHandle(ownerList,cover);

	}
	//保存最终的权属单位信息
	@Transactional(readOnly = false)
	public void ownerHandle(List<CoverOwner> ownerList,Cover cover){
		for (CoverOwner coverOwner : ownerList){
		/*	if (coverOwner.getId() == null){
				continue;
			}*/
			if (CoverOwner.DEL_FLAG_NORMAL.equals(coverOwner.getDelFlag())){
				if (StringUtils.isBlank(coverOwner.getId())){
					coverOwner.setCoverId(cover.getId());
					coverOwner.preInsert();
					coverOwner.setOwnerType("org");
					coverOwnerMapper.insert(coverOwner);
				}else{
					coverOwner.preUpdate();
					coverOwnerMapper.update(coverOwner);
				}
			}else{
				coverOwnerMapper.delete(coverOwner);
			}
		}
	}

	/**
	 * 一键获取待归属井盖任务明细
	 * @return
	 */
	@Transactional(readOnly = false)
	public String obtainAssignOwnerPage(){
		String resultRerurn="";
		List<CoverTaskProcess> processList = null;
		try {
		//获取当前部门所在任务
		User user = UserUtils.getUser();
		CoverTaskInfo queryTaskInfo=new CoverTaskInfo();
		queryTaskInfo.setOffice(user.getOffice());
		List<CoverTaskInfo> taskInfoList=coverTaskInfoService.findList(queryTaskInfo);
		if(null!=taskInfoList&&taskInfoList.size()>0){
			for(CoverTaskInfo coverTaskInfo:taskInfoList){
				resultRerurn=obtainCover(coverTaskInfo);
				if(StringUtils.isNotEmpty(resultRerurn)){
					break;
				}
			}

		}


		}catch(Exception e){
			resultRerurn="";
			e.printStackTrace();
		}

		return resultRerurn;
	}

}
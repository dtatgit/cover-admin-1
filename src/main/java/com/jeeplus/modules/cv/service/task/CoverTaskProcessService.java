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
import com.jeeplus.modules.cv.entity.task.CoverTaskInfo;
import com.jeeplus.modules.cv.mapper.task.CoverTaskInfoMapper;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.User;
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

	public CoverTaskProcess get(String id) {
		return super.get(id);
	}
	
	public List<CoverTaskProcess> findList(CoverTaskProcess coverTaskProcess) {
		return super.findList(coverTaskProcess);
	}
	
	public Page<CoverTaskProcess> findPage(Page<CoverTaskProcess> page, CoverTaskProcess coverTaskProcess) {
		return super.findPage(page, coverTaskProcess);
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
		if(null!=coverList&&coverList.size()>0){
			for(Cover cover:coverList){
				CoverTaskProcess process=new CoverTaskProcess();
				process.setCover(cover);
				process.setTaskStatus(CodeConstant.TASK_STATUS.ASSIGN);
				process.setCoverTaskInfo(coverTaskInfo);
				super.save(process);
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
}
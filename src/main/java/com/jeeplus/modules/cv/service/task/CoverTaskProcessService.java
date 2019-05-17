/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.task;

import java.util.List;

import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.entity.task.CoverTaskInfo;
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

}
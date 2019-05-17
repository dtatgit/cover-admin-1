/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.task;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.task.CoverTaskInfo;
import com.jeeplus.modules.cv.mapper.task.CoverTaskInfoMapper;

/**
 * 井盖数据处理任务Service
 * @author crj
 * @version 2019-05-16
 */
@Service
@Transactional(readOnly = true)
public class CoverTaskInfoService extends CrudService<CoverTaskInfoMapper, CoverTaskInfo> {

	public CoverTaskInfo get(String id) {
		return super.get(id);
	}
	
	public List<CoverTaskInfo> findList(CoverTaskInfo coverTaskInfo) {
		return super.findList(coverTaskInfo);
	}
	
	public Page<CoverTaskInfo> findPage(Page<CoverTaskInfo> page, CoverTaskInfo coverTaskInfo) {
		return super.findPage(page, coverTaskInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverTaskInfo coverTaskInfo) {
		super.save(coverTaskInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverTaskInfo coverTaskInfo) {
		super.delete(coverTaskInfo);
	}
	
}
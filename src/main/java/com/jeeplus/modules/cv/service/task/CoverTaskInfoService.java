/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.task;

import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.entity.task.CoverTableField;
import com.jeeplus.modules.cv.entity.task.CoverTaskProcess;
import com.jeeplus.modules.cv.mapper.equinfo.CoverMapper;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.cv.vo.UserCollectionVO;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private CoverMapper coverMapper;
	@Autowired
	private CoverTableFieldService coverTableFieldService;
	@Autowired
	private CoverTaskProcessService coverTaskProcessService;

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
		//任务初始状态为已分配
		coverTaskInfo.setTaskStatus(CodeConstant.TASK_STATUS.ASSIGN);

		/**************获取任务过滤的数据start************************/
		Cover cover=coverTaskInfo.getCover();
		if(cover!=null){
			List<Cover> coverList=coverMapper.findList(cover);
			coverTaskInfo.setTaskNum(String.valueOf(coverList.size()));
			super.save(coverTaskInfo);
			if(null!=coverList&&coverList.size()>0){
				coverTaskProcessService.generateTaskPro(coverTaskInfo,coverList);
			}

		}
		//coverTaskInfo.setTaskContent();//任务过滤条件
		/**************获取任务过滤的数据end************************/
		coverTableFieldService.generateTaskField(coverTaskInfo, "cover", "井盖基础信息");


	}
	
	@Transactional(readOnly = false)
	public void delete(CoverTaskInfo coverTaskInfo) {
		super.delete(coverTaskInfo);
	}



	
}
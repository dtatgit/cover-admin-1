/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.base;

import java.util.Date;
import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.modules.cv.utils.EntityUtils;
import com.jeeplus.modules.flow.entity.opt.FlowOpt;
import com.jeeplus.modules.projectInfo.entity.ProjectInfo;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.flow.entity.base.FlowState;
import com.jeeplus.modules.flow.mapper.base.FlowStateMapper;

/**
 * 工单流程状态Service
 * @author crj
 * @version 2019-11-21
 */
@Service
@Transactional(readOnly = true)
public class FlowStateService extends CrudService<FlowStateMapper, FlowState> {

	public FlowState get(String id) {
		return super.get(id);
	}
	
	public List<FlowState> findList(FlowState flowState) {
		return super.findList(flowState);
	}
	
	public Page<FlowState> findPage(Page<FlowState> page, FlowState flowState) {
		return super.findPage(page, flowState);
	}
	
	@Transactional(readOnly = false)
	public void save(FlowState flowState) {
		String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
		String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
		if(StringUtils.isNotEmpty(projectId)) {
			flowState.setProjectId(projectId);
		}
		if(StringUtils.isNotEmpty(projectName)) {
			flowState.setProjectName(projectName);
		}
		super.save(flowState);
	}
	
	@Transactional(readOnly = false)
	public void delete(FlowState flowState) {
		super.delete(flowState);
	}

	public void synData(String standardProjectId,ProjectInfo projectInfo){
		FlowState queryFlowState=new FlowState();
		queryFlowState.setProjectId(standardProjectId);
		List<FlowState> oldDataList=mapper.checkFindList(queryFlowState);
		if(CollectionUtil.isNotEmpty(oldDataList)) {
			for (FlowState flowState : oldDataList) {
				FlowState newFlowState= EntityUtils.copyData(flowState,FlowState.class);
				newFlowState.setId(IdGen.uuid());
				newFlowState.setProjectId(projectInfo.getId());
				newFlowState.setProjectName(projectInfo.getProjectName());
				newFlowState.setCreateDate(new Date());
				newFlowState.setCreateBy(projectInfo.getCreateBy());
				mapper.insert(newFlowState);
			}
		}
	}
}
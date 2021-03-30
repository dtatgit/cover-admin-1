/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.opt;

import java.util.Date;
import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.modules.cv.utils.EntityUtils;
import com.jeeplus.modules.flow.entity.base.FlowProc;
import com.jeeplus.modules.flow.service.base.FlowProcService;
import com.jeeplus.modules.projectInfo.entity.ProjectInfo;
import com.jeeplus.modules.sys.entity.DictType;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.flow.entity.opt.FlowOpt;
import com.jeeplus.modules.flow.mapper.opt.FlowOptMapper;

/**
 * 工单流程操作定义Service
 * @author crj
 * @version 2019-11-21
 */
@Service
@Transactional(readOnly = true)
public class FlowOptService extends CrudService<FlowOptMapper, FlowOpt> {
	@Autowired
	private FlowProcService flowProcService;

	public FlowOpt get(String id) {
		return super.get(id);
	}
	
	public List<FlowOpt> findList(FlowOpt flowOpt) {
		return super.findList(flowOpt);
	}
	
	public Page<FlowOpt> findPage(Page<FlowOpt> page, FlowOpt flowOpt) {
		return super.findPage(page, flowOpt);
	}
	
	@Transactional(readOnly = false)
	public void save(FlowOpt flowOpt) {
		String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
		String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
		if(StringUtils.isNotEmpty(projectId)) {
			flowOpt.setProjectId(projectId);
		}
		if(StringUtils.isNotEmpty(projectName)) {
			flowOpt.setProjectName(projectName);
		}
		super.save(flowOpt);
	}
	
	@Transactional(readOnly = false)
	public void delete(FlowOpt flowOpt) {
		super.delete(flowOpt);
	}


	/**
	 *
	 * @param flowId  流程id
	 * @param optCode 操作代码
	 * @return
	 */
	public FlowOpt queryFlowByOpt(String flowId,String optCode){
		FlowOpt flowOpt=new FlowOpt();
		FlowOpt optResult=null;
		flowOpt.setOptCode(optCode);
		FlowProc flow=null;
		if(StringUtils.isNotEmpty(flowId)){
			 flow=flowProcService.get(flowId);
		}
		if(null!=flow){
			flowOpt.setFlowId(flow);
		}
		List<FlowOpt> optList=super.findList(flowOpt);
		if(null!=optList&&optList.size()>0){
			optResult=optList.get(0);
		}
		return optResult;
	}

	public void synData(String standardProjectId,ProjectInfo projectInfo){
		FlowOpt queryFlowOpt=new FlowOpt();
		queryFlowOpt.setProjectId(standardProjectId);
		List<FlowOpt> optList=mapper.checkFindList(queryFlowOpt);
		if(CollectionUtil.isNotEmpty(optList)) {
			for (FlowOpt flowOpt : optList) {
				FlowOpt newFlowOpt= EntityUtils.copyData(flowOpt,FlowOpt.class);
				newFlowOpt.setId(IdGen.uuid());
				newFlowOpt.setProjectId(projectInfo.getId());
				newFlowOpt.setProjectName(projectInfo.getProjectName());
				newFlowOpt.setCreateDate(new Date());
				newFlowOpt.setCreateBy(projectInfo.getCreateBy());
				mapper.insert(newFlowOpt);
			}
		}
	}
	
}
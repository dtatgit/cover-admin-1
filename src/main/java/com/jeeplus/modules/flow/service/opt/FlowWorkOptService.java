/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.opt;

import java.util.List;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.flow.entity.base.FlowProc;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.flow.entity.opt.FlowWorkOpt;
import com.jeeplus.modules.flow.mapper.opt.FlowWorkOptMapper;

/**
 * 工单操作记录Service
 * @author crj
 * @version 2019-11-21
 */
@Service
@Transactional(readOnly = true)
public class FlowWorkOptService extends CrudService<FlowWorkOptMapper, FlowWorkOpt> {

	public FlowWorkOpt get(String id) {
		return super.get(id);
	}
	
	public List<FlowWorkOpt> findList(FlowWorkOpt flowWorkOpt) {
		return super.findList(flowWorkOpt);
	}
	
	public Page<FlowWorkOpt> findPage(Page<FlowWorkOpt> page, FlowWorkOpt flowWorkOpt) {
		return super.findPage(page, flowWorkOpt);
	}
	
	@Transactional(readOnly = false)
	public void save(FlowWorkOpt flowWorkOpt) {
		String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
		String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
		if(StringUtils.isNotEmpty(projectId)) {
			flowWorkOpt.setProjectId(projectId);
		}
		if(StringUtils.isNotEmpty(projectName)) {
			flowWorkOpt.setProjectName(projectName);
		}
		super.save(flowWorkOpt);
	}
	
	@Transactional(readOnly = false)
	public void delete(FlowWorkOpt flowWorkOpt) {
		super.delete(flowWorkOpt);
	}

	/**
	 * 根据工单获取工单流程操作记录
	 * @param work
	 * @return
	 */
	public List<FlowWorkOpt> queryFlowOptByWork(CoverWork work){
		List<FlowWorkOpt> optList=null;
		if(null!=work){
			FlowWorkOpt workOpt=new FlowWorkOpt();
			workOpt.setBillId(work);
			optList=super.findList(workOpt);
		}
		return optList;
	}
	
}
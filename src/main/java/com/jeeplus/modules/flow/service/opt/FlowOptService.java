/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.opt;

import java.util.List;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.flow.entity.base.FlowProc;
import com.jeeplus.modules.flow.service.base.FlowProcService;
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
	
}
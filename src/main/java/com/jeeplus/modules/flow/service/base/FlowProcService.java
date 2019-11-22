/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.base;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.flow.entity.base.FlowProc;
import com.jeeplus.modules.flow.mapper.base.FlowProcMapper;

/**
 * 工单流程定义Service
 * @author crj
 * @version 2019-11-21
 */
@Service
@Transactional(readOnly = true)
public class FlowProcService extends CrudService<FlowProcMapper, FlowProc> {

	public FlowProc get(String id) {
		return super.get(id);
	}
	
	public List<FlowProc> findList(FlowProc flowProc) {
		return super.findList(flowProc);
	}
	
	public Page<FlowProc> findPage(Page<FlowProc> page, FlowProc flowProc) {
		return super.findPage(page, flowProc);
	}
	
	@Transactional(readOnly = false)
	public void save(FlowProc flowProc) {
		super.save(flowProc);
	}
	
	@Transactional(readOnly = false)
	public void delete(FlowProc flowProc) {
		super.delete(flowProc);
	}
	
}
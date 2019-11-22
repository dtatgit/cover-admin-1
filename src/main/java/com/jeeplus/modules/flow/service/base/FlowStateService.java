/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.base;

import java.util.List;

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
		super.save(flowState);
	}
	
	@Transactional(readOnly = false)
	public void delete(FlowState flowState) {
		super.delete(flowState);
	}
	
}
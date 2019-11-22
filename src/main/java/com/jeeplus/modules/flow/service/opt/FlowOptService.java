/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.opt;

import java.util.List;

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
	
}
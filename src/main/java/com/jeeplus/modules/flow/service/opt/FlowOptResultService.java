/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.opt;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.flow.entity.opt.FlowOptResult;
import com.jeeplus.modules.flow.mapper.opt.FlowOptResultMapper;

/**
 * 工单流程操作结果定义Service
 * @author crj
 * @version 2019-11-21
 */
@Service
@Transactional(readOnly = true)
public class FlowOptResultService extends CrudService<FlowOptResultMapper, FlowOptResult> {

	public FlowOptResult get(String id) {
		return super.get(id);
	}
	
	public List<FlowOptResult> findList(FlowOptResult flowOptResult) {
		return super.findList(flowOptResult);
	}
	
	public Page<FlowOptResult> findPage(Page<FlowOptResult> page, FlowOptResult flowOptResult) {
		return super.findPage(page, flowOptResult);
	}
	
	@Transactional(readOnly = false)
	public void save(FlowOptResult flowOptResult) {
		super.save(flowOptResult);
	}
	
	@Transactional(readOnly = false)
	public void delete(FlowOptResult flowOptResult) {
		super.delete(flowOptResult);
	}
	
}
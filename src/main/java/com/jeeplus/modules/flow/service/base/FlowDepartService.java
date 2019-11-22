/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.base;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.flow.entity.base.FlowDepart;
import com.jeeplus.modules.flow.mapper.base.FlowDepartMapper;

/**
 * 部门流程配置Service
 * @author crj
 * @version 2019-11-21
 */
@Service
@Transactional(readOnly = true)
public class FlowDepartService extends CrudService<FlowDepartMapper, FlowDepart> {

	public FlowDepart get(String id) {
		return super.get(id);
	}
	
	public List<FlowDepart> findList(FlowDepart flowDepart) {
		return super.findList(flowDepart);
	}
	
	public Page<FlowDepart> findPage(Page<FlowDepart> page, FlowDepart flowDepart) {
		return super.findPage(page, flowDepart);
	}
	
	@Transactional(readOnly = false)
	public void save(FlowDepart flowDepart) {
		super.save(flowDepart);
	}
	
	@Transactional(readOnly = false)
	public void delete(FlowDepart flowDepart) {
		super.delete(flowDepart);
	}
	
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.base;

import java.util.List;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.flow.entity.base.FlowUserOrg;
import com.jeeplus.modules.flow.mapper.base.FlowUserOrgMapper;

/**
 * 用户组织关系配置Service
 * @author crj
 * @version 2019-11-21
 */
@Service
@Transactional(readOnly = true)
public class FlowUserOrgService extends CrudService<FlowUserOrgMapper, FlowUserOrg> {

	public FlowUserOrg get(String id) {
		return super.get(id);
	}
	
	public List<FlowUserOrg> findList(FlowUserOrg flowUserOrg) {
		return super.findList(flowUserOrg);
	}
	
	public Page<FlowUserOrg> findPage(Page<FlowUserOrg> page, FlowUserOrg flowUserOrg) {
		return super.findPage(page, flowUserOrg);
	}
	
	@Transactional(readOnly = false)
	public void save(FlowUserOrg flowUserOrg) {
		String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
		String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
		if(StringUtils.isNotEmpty(projectId)) {
			flowUserOrg.setProjectId(projectId);
		}
		if(StringUtils.isNotEmpty(projectName)) {
			flowUserOrg.setProjectName(projectName);
		}
		super.save(flowUserOrg);
	}
	
	@Transactional(readOnly = false)
	public void delete(FlowUserOrg flowUserOrg) {
		super.delete(flowUserOrg);
	}
	
}
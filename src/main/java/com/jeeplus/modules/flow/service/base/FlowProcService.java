/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.service.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.utils.EntityUtils;
import com.jeeplus.modules.flow.entity.base.FlowDepart;
import com.jeeplus.modules.flow.entity.base.FlowState;
import com.jeeplus.modules.projectInfo.entity.ProjectInfo;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private FlowDepartService flowDepartService;
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
		String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
		String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
		if(StringUtils.isNotEmpty(projectId)) {
			flowProc.setProjectId(projectId);
		}
		if(StringUtils.isNotEmpty(projectName)) {
			flowProc.setProjectName(projectName);
		}
		super.save(flowProc);
	}
	
	@Transactional(readOnly = false)
	public void delete(FlowProc flowProc) {
		super.delete(flowProc);
	}

	/**
	 * 根据部门获取流程信息列表
	 * @return
	 */
	public List<FlowProc> queryFlowByOffice(Office office,String workType){
		List<FlowProc> flowProcList=new ArrayList<>();
		List<FlowDepart> flowDepartList=null;
		if(null!=office){//add by 2019-11-25根据维护单位来获取工单流程id
			FlowDepart query=new FlowDepart();
			query.setOrgId(office);
			if(StringUtils.isNotEmpty(workType)){
				query.setBillType(workType);
			}
			flowDepartList=flowDepartService.findList(query);
		}
		if(null!=flowDepartList&&flowDepartList.size()>0){
			for(FlowDepart flowconfig:flowDepartList){
				String flowNo=flowconfig.getFlowNo();//	流程编号
				if(StringUtils.isNotEmpty(flowNo)){
					FlowProc flowProc=super.findUniqueByProperty("a.flow_no", flowNo);
					if(null!=flowProc){
						flowProcList.add(flowProc);
					}
				}

			}

		}
		return flowProcList;
	}

	public void synData(String standardProjectId,ProjectInfo projectInfo){
		FlowProc queryFlowProc=new FlowProc();
		queryFlowProc.setProjectId(standardProjectId);
		List<FlowProc> oldDataList=mapper.checkFindList(queryFlowProc);
		if(CollectionUtil.isNotEmpty(oldDataList)) {
			for (FlowProc flowProc : oldDataList) {
				FlowProc newFlowProc= EntityUtils.copyData(flowProc,FlowProc.class);
				newFlowProc.setId(IdGen.uuid());
				newFlowProc.setProjectId(projectInfo.getId());
				newFlowProc.setProjectName(projectInfo.getProjectName());
				newFlowProc.setCreateDate(new Date());
				newFlowProc.setCreateBy(projectInfo.getCreateBy());
				mapper.insert(newFlowProc);
			}
		}
	}
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import java.util.List;

import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.work.CoverWorkOperation;
import com.jeeplus.modules.cb.mapper.work.CoverWorkOperationMapper;

/**
 * 工单操作记录Service
 * @author crj
 * @version 2019-06-26
 */
@Service
@Transactional(readOnly = true)
public class CoverWorkOperationService extends CrudService<CoverWorkOperationMapper, CoverWorkOperation> {

	@Autowired
	private CoverWorkOperationDetailService coverWorkOperationDetailService;
	public CoverWorkOperation get(String id) {
		CoverWorkOperation coverWorkOperation=super.get(id);
		coverWorkOperation.setWorkOperationDetail(coverWorkOperationDetailService.obtainDetail(id));
		return coverWorkOperation;
	}
	
	public List<CoverWorkOperation> findList(CoverWorkOperation coverWorkOperation) {
		return super.findList(coverWorkOperation);
	}
	
	public Page<CoverWorkOperation> findPage(Page<CoverWorkOperation> page, CoverWorkOperation coverWorkOperation) {
		return super.findPage(page, coverWorkOperation);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverWorkOperation coverWorkOperation) {
		super.save(coverWorkOperation);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverWorkOperation coverWorkOperation) {
		super.delete(coverWorkOperation);
	}

	@Transactional(readOnly = false)
	public void createRecord(CoverWork work,String operationType,String operationStatus,String operationResult ){
		CoverWorkOperation coverWorkOperation=new CoverWorkOperation();
		coverWorkOperation.setCoverWork(work);// 工单信息
/*		coverWorkOperation.setOperationType(CodeConstant.WORK_OPERATION_TYPE.CREATE);// 操作类型
		coverWorkOperation.setOperationStatus("初始化");// 操作状态
		coverWorkOperation.setOperationResult("自动生成");// 操作结果*/

		coverWorkOperation.setOperationType(operationType);// 操作类型
		coverWorkOperation.setOperationStatus(operationStatus);// 操作状态
		coverWorkOperation.setOperationResult(operationResult);// 操作结果
		User user = UserUtils.getUser();
		Office office=null;
		if (StringUtils.isNotBlank(user.getId())){
			office=user.getOffice();
		}
		if(null!=office){
			coverWorkOperation.setCreateDepart(office);// 操作部门
		}
		String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
		String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
		if(com.jeeplus.common.utils.StringUtils.isNotEmpty(projectId)) {
			coverWorkOperation.setProjectId(projectId);
		}
		if(com.jeeplus.common.utils.StringUtils.isNotEmpty(projectName)) {
			coverWorkOperation.setProjectName(projectName);
		}
		super.save(coverWorkOperation);


	}
	
}
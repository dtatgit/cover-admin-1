/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.work.CoverWorkOperationDetail;
import com.jeeplus.modules.cb.mapper.work.CoverWorkOperationDetailMapper;

/**
 * 工单操作记录明细Service
 * @author crj
 * @version 2019-08-07
 */
@Service
@Transactional(readOnly = true)
public class CoverWorkOperationDetailService extends CrudService<CoverWorkOperationDetailMapper, CoverWorkOperationDetail> {

	public CoverWorkOperationDetail get(String id) {
		return super.get(id);
	}
	
	public List<CoverWorkOperationDetail> findList(CoverWorkOperationDetail coverWorkOperationDetail) {
		return super.findList(coverWorkOperationDetail);
	}
	
	public Page<CoverWorkOperationDetail> findPage(Page<CoverWorkOperationDetail> page, CoverWorkOperationDetail coverWorkOperationDetail) {
		return super.findPage(page, coverWorkOperationDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverWorkOperationDetail coverWorkOperationDetail) {
		super.save(coverWorkOperationDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverWorkOperationDetail coverWorkOperationDetail) {
		super.delete(coverWorkOperationDetail);
	}

	/**
	 *
	 * @param workOperationId  工单操作记录ID
	 * @return
	 */
	public List<CoverWorkOperationDetail> obtainDetail(String workOperationId){
		CoverWorkOperationDetail queryDetail=new CoverWorkOperationDetail();
		queryDetail.setCoverWorkOperationId(workOperationId);
		List<CoverWorkOperationDetail> detailList=super.findList(queryDetail);
		return detailList;
	}
	
}
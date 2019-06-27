/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import java.util.List;

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

	public CoverWorkOperation get(String id) {
		return super.get(id);
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
	
}
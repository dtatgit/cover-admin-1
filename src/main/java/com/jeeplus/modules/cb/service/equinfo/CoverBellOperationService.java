/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.equinfo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.equinfo.CoverBellOperation;
import com.jeeplus.modules.cb.mapper.equinfo.CoverBellOperationMapper;

/**
 * 井铃操作记录Service
 * @author crj
 * @version 2019-06-24
 */
@Service
@Transactional(readOnly = true)
public class CoverBellOperationService extends CrudService<CoverBellOperationMapper, CoverBellOperation> {

	public CoverBellOperation get(String id) {
		return super.get(id);
	}
	
	public List<CoverBellOperation> findList(CoverBellOperation coverBellOperation) {
		return super.findList(coverBellOperation);
	}
	
	public Page<CoverBellOperation> findPage(Page<CoverBellOperation> page, CoverBellOperation coverBellOperation) {
		return super.findPage(page, coverBellOperation);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverBellOperation coverBellOperation) {
		super.save(coverBellOperation);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverBellOperation coverBellOperation) {
		super.delete(coverBellOperation);
	}
	
}
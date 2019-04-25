/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.equinfo.CoverAudit;
import com.jeeplus.modules.cv.mapper.equinfo.CoverAuditMapper;

/**
 * 井盖审核信息Service
 * @author crj
 * @version 2019-04-24
 */
@Service
@Transactional(readOnly = true)
public class CoverAuditService extends CrudService<CoverAuditMapper, CoverAudit> {

	public CoverAudit get(String id) {
		return super.get(id);
	}
	
	public List<CoverAudit> findList(CoverAudit coverAudit) {
		return super.findList(coverAudit);
	}
	
	public Page<CoverAudit> findPage(Page<CoverAudit> page, CoverAudit coverAudit) {
		return super.findPage(page, coverAudit);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverAudit coverAudit) {
		super.save(coverAudit);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverAudit coverAudit) {
		super.delete(coverAudit);
	}
	
}
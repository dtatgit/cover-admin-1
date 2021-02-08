/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.work.CoverWorkInspection;
import com.jeeplus.modules.cb.mapper.work.CoverWorkInspectionMapper;

/**
 * 工单巡查Service
 * @author crj
 * @version 2021-02-08
 */
@Service
@Transactional(readOnly = true)
public class CoverWorkInspectionService extends CrudService<CoverWorkInspectionMapper, CoverWorkInspection> {

	public CoverWorkInspection get(String id) {
		return super.get(id);
	}
	
	public List<CoverWorkInspection> findList(CoverWorkInspection coverWorkInspection) {
		return super.findList(coverWorkInspection);
	}
	
	public Page<CoverWorkInspection> findPage(Page<CoverWorkInspection> page, CoverWorkInspection coverWorkInspection) {
		return super.findPage(page, coverWorkInspection);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverWorkInspection coverWorkInspection) {
		super.save(coverWorkInspection);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverWorkInspection coverWorkInspection) {
		super.delete(coverWorkInspection);
	}
	
}
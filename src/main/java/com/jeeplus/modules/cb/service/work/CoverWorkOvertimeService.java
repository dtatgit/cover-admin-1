/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.work.CoverWorkOvertime;
import com.jeeplus.modules.cb.mapper.work.CoverWorkOvertimeMapper;

/**
 * 超时工单Service
 * @author crj
 * @version 2019-11-07
 */
@Service
@Transactional(readOnly = true)
public class CoverWorkOvertimeService extends CrudService<CoverWorkOvertimeMapper, CoverWorkOvertime> {

	public CoverWorkOvertime get(String id) {
		return super.get(id);
	}
	
	public List<CoverWorkOvertime> findList(CoverWorkOvertime coverWorkOvertime) {
		return super.findList(coverWorkOvertime);
	}
	
	public Page<CoverWorkOvertime> findPage(Page<CoverWorkOvertime> page, CoverWorkOvertime coverWorkOvertime) {
		return super.findPage(page, coverWorkOvertime);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverWorkOvertime coverWorkOvertime) {
		super.save(coverWorkOvertime);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverWorkOvertime coverWorkOvertime) {
		super.delete(coverWorkOvertime);
	}
	
}
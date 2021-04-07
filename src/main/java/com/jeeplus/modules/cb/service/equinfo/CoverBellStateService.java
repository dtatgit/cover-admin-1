/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.equinfo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.equinfo.CoverBellState;
import com.jeeplus.modules.cb.mapper.equinfo.CoverBellStateMapper;

/**
 * 井卫状态上报Service
 * @author crj
 * @version 2019-06-24
 */
@Service
@Transactional(readOnly = true)
public class CoverBellStateService extends CrudService<CoverBellStateMapper, CoverBellState> {

	public CoverBellState get(String id) {
		return super.get(id);
	}
	
	public List<CoverBellState> findList(CoverBellState coverBellState) {
		return super.findList(coverBellState);
	}
	
	public Page<CoverBellState> findPage(Page<CoverBellState> page, CoverBellState coverBellState) {
		return super.findPage(page, coverBellState);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverBellState coverBellState) {
		super.save(coverBellState);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverBellState coverBellState) {
		super.delete(coverBellState);
	}
	
}
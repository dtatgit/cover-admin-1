/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.statis;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.statis.CoverStatis;
import com.jeeplus.modules.cv.mapper.statis.CoverStatisMapper;

/**
 * 井盖相关统计Service
 * @author crj
 * @version 2021-02-08
 */
@Service
@Transactional(readOnly = true)
public class CoverStatisService extends CrudService<CoverStatisMapper, CoverStatis> {

	public CoverStatis get(String id) {
		return super.get(id);
	}
	
	public List<CoverStatis> findList(CoverStatis coverStatis) {
		return super.findList(coverStatis);
	}
	
	public Page<CoverStatis> findPage(Page<CoverStatis> page, CoverStatis coverStatis) {
		return super.findPage(page, coverStatis);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverStatis coverStatis) {
		super.save(coverStatis);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverStatis coverStatis) {
		super.delete(coverStatis);
	}
	
}
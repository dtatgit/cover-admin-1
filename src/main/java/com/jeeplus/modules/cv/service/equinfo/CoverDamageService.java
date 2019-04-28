/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.equinfo.CoverDamage;
import com.jeeplus.modules.cv.mapper.equinfo.CoverDamageMapper;

/**
 * 井盖损坏形式Service
 * @author crj
 * @version 2019-04-28
 */
@Service
@Transactional(readOnly = true)
public class CoverDamageService extends CrudService<CoverDamageMapper, CoverDamage> {

	public CoverDamage get(String id) {
		return super.get(id);
	}
	
	public List<CoverDamage> findList(CoverDamage coverDamage) {
		return super.findList(coverDamage);
	}
	
	public Page<CoverDamage> findPage(Page<CoverDamage> page, CoverDamage coverDamage) {
		return super.findPage(page, coverDamage);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverDamage coverDamage) {
		super.save(coverDamage);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverDamage coverDamage) {
		super.delete(coverDamage);
	}
	
}
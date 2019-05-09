/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.equinfo.CoverOwner;
import com.jeeplus.modules.cv.mapper.equinfo.CoverOwnerMapper;

/**
 * 井盖权属单位Service
 * @author crj
 * @version 2019-05-09
 */
@Service
@Transactional(readOnly = true)
public class CoverOwnerService extends CrudService<CoverOwnerMapper, CoverOwner> {

	public CoverOwner get(String id) {
		return super.get(id);
	}
	
	public List<CoverOwner> findList(CoverOwner coverOwner) {
		return super.findList(coverOwner);
	}
	
	public Page<CoverOwner> findPage(Page<CoverOwner> page, CoverOwner coverOwner) {
		return super.findPage(page, coverOwner);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverOwner coverOwner) {
		super.save(coverOwner);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverOwner coverOwner) {
		super.delete(coverOwner);
	}
	
}
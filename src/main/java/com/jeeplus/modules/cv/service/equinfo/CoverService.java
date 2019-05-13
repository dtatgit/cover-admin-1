/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.List;

import com.jeeplus.modules.cv.entity.equinfo.CoverImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.mapper.equinfo.CoverMapper;

/**
 * 井盖基础信息Service
 * @author crj
 * @version 2019-04-19
 */
@Service
@Transactional(readOnly = true)
public class CoverService extends CrudService<CoverMapper, Cover> {
	@Autowired
	private CoverImageService coverImageService;
	@Autowired
	private CoverDamageService coverDamageService;
	@Autowired
	private CoverOwnerService coverOwnerService;

	public Cover get(String id) {
		Cover cover=super.get(id);
		cover.setCoverImageList(coverImageService.obtainImage(id));
		cover.setCoverDamageList(coverDamageService.obtainDamage(id));
		cover.setCoverOwnerList(coverOwnerService.obtainOwner(id));
		return cover;
	}
	
	public List<Cover> findList(Cover cover) {
		return super.findList(cover);
	}
	
	public Page<Cover> findPage(Page<Cover> page, Cover cover) {
		return super.findPage(page, cover);
	}
	
	@Transactional(readOnly = false)
	public void save(Cover cover) {
		super.save(cover);
	}
	
	@Transactional(readOnly = false)
	public void delete(Cover cover) {
		super.delete(cover);
	}



}
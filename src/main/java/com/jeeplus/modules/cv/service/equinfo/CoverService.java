/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cv.entity.equinfo.*;
import com.jeeplus.modules.cv.mapper.equinfo.CoverDamageMapper;
import com.jeeplus.modules.cv.mapper.equinfo.CoverHistoryMapper;
import com.jeeplus.modules.cv.mapper.equinfo.CoverOwnerMapper;
import com.jeeplus.modules.cv.utils.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
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
	@Autowired
	private CoverDamageMapper coverDamageMapper;
	@Autowired
	private CoverOwnerMapper coverOwnerMapper;
	@Autowired
	private CoverHistoryMapper coverHistoryMapper;


	public Cover get(String id) {
		Cover cover=super.get(id);
		cover.setCoverImageList(coverImageService.obtainImage(id));
		cover.setCoverDamageList(coverDamageService.obtainDamage(id));
		cover.setCoverOwnerList(coverOwnerService.obtainOwner(id));

//		cover.setCoverDamageList(coverDamageMapper.findList(new CoverDamage(cover)));
//		cover.setCoverOwnerList(coverOwnerMapper.findList(new CoverOwner(cover)));
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


	@Transactional(readOnly = false)
	public void repairSave(Cover cover) {
		super.save(cover);
		CoverHistory coverHistory= EntityUtils.copyData(cover,CoverHistory.class);
		//coverHistory.setId(IdGen.uuid());
		coverHistory.setCoverId(cover.getId());
		coverHistory.preInsert();
		coverHistory.setSource("信息修复");
		StringBuffer damageSB=new StringBuffer();
		StringBuffer ownerSB=new StringBuffer();
		for (CoverDamage coverDamage : cover.getCoverDamageList()){
			if (coverDamage.getId() == null){
				continue;
			}
			damageSB.append(coverDamage.getDamage()).append(",");
			if (CoverDamage.DEL_FLAG_NORMAL.equals(coverDamage.getDelFlag())){
				if (StringUtils.isBlank(coverDamage.getId())){
					coverDamage.setCoverId(cover.getId());
					coverDamage.preInsert();
					coverDamage.setStatus("normal");
					coverDamageMapper.insert(coverDamage);
				}else{
					coverDamage.preUpdate();
					coverDamageMapper.update(coverDamage);
				}

			}else{
				coverDamageMapper.delete(coverDamage);
			}
		}
		for (CoverOwner coverOwner : cover.getCoverOwnerList()){
			if (coverOwner.getId() == null){
				continue;
			}
			ownerSB.append(coverOwner.getOwnerName()).append(",");
			if (CoverOwner.DEL_FLAG_NORMAL.equals(coverOwner.getDelFlag())){
				if (StringUtils.isBlank(coverOwner.getId())){
					coverOwner.setCoverId(cover.getId());
					coverOwner.preInsert();
					coverOwner.setOwnerType("org");
					coverOwnerMapper.insert(coverOwner);
				}else{
					coverOwner.preUpdate();
					coverOwnerMapper.update(coverOwner);
				}
			}else{
				coverOwnerMapper.delete(coverOwner);
			}
		}
		String damageStr=damageSB.toString();
		String ownerStr=ownerSB.toString();
		if(StringUtils.isNotEmpty(damageStr)){
			coverHistory.setCoverDamage(damageStr.substring(0, damageStr.length()-1));
		}
		if(StringUtils.isNotEmpty(ownerStr)){
			coverHistory.setCoverOwner(ownerStr.substring(0, ownerStr.length()-1));
		}
		coverHistoryMapper.insert(coverHistory);
	}
}
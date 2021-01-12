/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.Arrays;
import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.entity.equinfo.CoverImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
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
@Transactional
public class CoverDamageService extends CrudService<CoverDamageMapper, CoverDamage> {

	@Autowired
	private CoverDamageMapper coverDamageMapper;


	private final static Logger logger = LoggerFactory.getLogger(CoverDamageService.class);

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

	public List<CoverDamage> obtainDamage(String coverId){
		CoverDamage coverDamage=new CoverDamage();
		coverDamage.setCoverId(coverId);
		List<CoverDamage> damageList=super.findList(coverDamage);

		return damageList;
	}

	public boolean cloneCoverDamage(Cover sourceCover, Cover targetCover) {
		try {
			CoverDamage param = new CoverDamage();
			param.setCoverId(sourceCover.getId());
			List<CoverDamage> coverDamages = findList(param);
			if (CollectionUtil.isNotEmpty(coverDamages)) {
				for (CoverDamage coverDamage : coverDamages) {
					CoverDamage obj = new CoverDamage();
					BeanUtils.copyProperties(coverDamage, obj);
					obj.setCoverId(targetCover.getId());
					obj.setId(IdGen.uuid());
					obj.setIsNewRecord(true);
					coverDamageMapper.insert(obj);
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("导入井盖复制井盖损坏形式失败:" + e.getMessage());
		}
		return false;
	}



	public boolean cloneCoverDamage(String coverId, String damageInfoStr) {
		if (StringUtils.isNotBlank(coverId) && StringUtils.isNotBlank(damageInfoStr)) {
			List<String> infos = Arrays.asList(damageInfoStr.split(","));
			for (String info : infos) {
				CoverDamage coverDamage = new CoverDamage();
				coverDamage.setId(IdGen.uuid());
				coverDamage.setCoverId(coverId);
				coverDamage.setIsNewRecord(true);
				String [] damageInfo = info.split(";");
				if (damageInfo != null) {
					coverDamage.setDamage(damageInfo[0]);
					coverDamage.setStatus(damageInfo[1]);
				}
				this.save(coverDamage);
			}
		}
			return true;
	}

	public static void main(String[] args) {
		String s = "124";
		List<String> infos = Arrays.asList(s.split(","));
		System.out.println(infos.size());
	}


}
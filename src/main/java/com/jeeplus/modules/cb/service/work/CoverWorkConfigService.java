/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.work.CoverWorkConfig;
import com.jeeplus.modules.cb.mapper.work.CoverWorkConfigMapper;

/**
 * 工单配置Service
 * @author crj
 * @version 2019-11-07
 */
@Service
@Transactional(readOnly = true)
public class CoverWorkConfigService extends CrudService<CoverWorkConfigMapper, CoverWorkConfig> {

	public CoverWorkConfig get(String id) {
		return super.get(id);
	}
	
	public List<CoverWorkConfig> findList(CoverWorkConfig coverWorkConfig) {
		return super.findList(coverWorkConfig);
	}
	
	public Page<CoverWorkConfig> findPage(Page<CoverWorkConfig> page, CoverWorkConfig coverWorkConfig) {
		return super.findPage(page, coverWorkConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverWorkConfig coverWorkConfig) {
		super.save(coverWorkConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverWorkConfig coverWorkConfig) {
		super.delete(coverWorkConfig);
	}
	
}
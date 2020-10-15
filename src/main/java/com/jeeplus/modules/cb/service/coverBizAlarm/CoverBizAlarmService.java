/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.coverBizAlarm;

import java.util.List;
import java.util.Map;

import com.jeeplus.modules.cb.entity.coverBizAlarm.CoverBizAlarm;
import com.jeeplus.modules.cb.mapper.coverBizAlarm.CoverBizAlarmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

/**
 * 井盖业务报警Service
 * @author Peter
 * @version 2020-10-13
 */
@Service
@Transactional
public class CoverBizAlarmService extends CrudService<CoverBizAlarmMapper, CoverBizAlarm> {


	@Autowired
	private CoverBizAlarmMapper coverBizAlarmMapper;

	public CoverBizAlarm get(String id) {
		return super.get(id);
	}
	
	public List<CoverBizAlarm> findList(CoverBizAlarm coverBizAlarm) {
		return super.findList(coverBizAlarm);
	}
	
	public Page<CoverBizAlarm> findPage(Page<CoverBizAlarm> page, CoverBizAlarm coverBizAlarm) {
		return super.findPage(page, coverBizAlarm);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverBizAlarm coverBizAlarm) {
		super.save(coverBizAlarm);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverBizAlarm coverBizAlarm) {
		super.delete(coverBizAlarm);
	}

	public List<CoverBizAlarm> queryByParam(Map<String, Object> map) {
		return coverBizAlarmMapper.queryByParam(map);
	}


	public void createCoverBizAlarm(String coverId, String alarmType) {
		CoverBizAlarm coverBizAlarm = new CoverBizAlarm(coverId, alarmType);
		this.save(coverBizAlarm);
	}

}
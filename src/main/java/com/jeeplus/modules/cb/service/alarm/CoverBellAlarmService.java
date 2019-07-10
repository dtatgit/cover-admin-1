/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.alarm;

import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.alarm.CoverBellAlarm;
import com.jeeplus.modules.cb.mapper.alarm.CoverBellAlarmMapper;

/**
 * 井铃报警信息Service
 * @author crj
 * @version 2019-06-24
 */
@Service
@Transactional(readOnly = true)
public class CoverBellAlarmService extends CrudService<CoverBellAlarmMapper, CoverBellAlarm> {
	@Autowired
	private CoverWorkService coverWorkService;
	public CoverBellAlarm get(String id) {
		return super.get(id);
	}
	
	public List<CoverBellAlarm> findList(CoverBellAlarm coverBellAlarm) {
		return super.findList(coverBellAlarm);
	}
	
	public Page<CoverBellAlarm> findPage(Page<CoverBellAlarm> page, CoverBellAlarm coverBellAlarm) {
		return super.findPage(page, coverBellAlarm);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverBellAlarm coverBellAlarm) {
		super.save(coverBellAlarm);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverBellAlarm coverBellAlarm) {
		super.delete(coverBellAlarm);
	}

	@Transactional(readOnly = false)
	public void createWork(CoverBellAlarm coverBellAlarm) {
		coverWorkService.createWork(coverBellAlarm);
	}

}
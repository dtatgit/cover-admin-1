/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.service;

import java.util.List;

import com.jeeplus.modules.cb.entity.exceptionReport.ExceptionReport;
import com.jeeplus.modules.cb.mapper.exceptionReport.ExceptionReportMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

/**
 * 异常上报Service
 * @author Peter
 * @version 2020-10-19
 */
@Service
@Transactional(readOnly = true)
public class ExceptionReportService extends CrudService<ExceptionReportMapper, ExceptionReport> {

	public ExceptionReport get(String id) {
		return super.get(id);
	}
	
	public List<ExceptionReport> findList(ExceptionReport exceptionReport) {
		return super.findList(exceptionReport);
	}
	
	public Page<ExceptionReport> findPage(Page<ExceptionReport> page, ExceptionReport exceptionReport) {
		return super.findPage(page, exceptionReport);
	}
	
	@Transactional(readOnly = false)
	public void save(ExceptionReport exceptionReport) {
		super.save(exceptionReport);
	}
	
	@Transactional(readOnly = false)
	public void delete(ExceptionReport exceptionReport) {
		super.delete(exceptionReport);
	}
	
}
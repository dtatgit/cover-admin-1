/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeeplus.modules.cb.entity.exceptionReport.ExceptionReport;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.mapper.exceptionReport.ExceptionReportMapper;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

/**
 * 异常上报Service
 *
 * @author Peter
 * @version 2020-10-19
 */
@Service
public class ExceptionReportService extends CrudService<ExceptionReportMapper, ExceptionReport> {

    @Autowired
    private CoverWorkService coverWorkService;

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


    public String createWorks(ExceptionReport exceptionReport) {
		String errorMsg = "";
		List<String> idList = Arrays.asList(exceptionReport.getIds().split(","));
		//批量创建工单
		for (String id : idList) {
			ExceptionReport report = this.get(id);
			if (report == null) {
				errorMsg = errorMsg + id + "异常上报不存在.";
				continue;
			}
			CoverWork coverWork = coverWorkService.get(report.getCoverWorkId());
			if (coverWork == null) {
				errorMsg = errorMsg + id + "对应的工单信息不存在.";
				continue;
			}
			Cover cover = coverWork.getCover();
			if (cover == null) {
				errorMsg = errorMsg + id + "对应的井盖信息不存在.";
				continue;
			}
			//业务报警工单
			if (CodeConstant.WORK_TYPE.BIZ_ALARM.equals(exceptionReport.getWorkType())) {
				//已生成未处理完的工单
				if (coverWorkService.isCreatedBizAlarmWork(cover.getId())) {
					continue;
				}
			}
			//生成工单
			try {
				coverWorkService.createCoverWork(cover, exceptionReport.getWorkType());
			} catch (Exception e) {
				logger.error("批量创建工单异常, 异常上报Id：" + id + "异常信息：" + e.getMessage());
				errorMsg = errorMsg + id + "生成工单异常.";
				continue;
			}
		}
		return errorMsg;
    }
}
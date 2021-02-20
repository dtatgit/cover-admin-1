/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.coverWorkAttr;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cb.entity.coverWorkAttr.CoverWorkAttr;
import com.jeeplus.modules.cb.entity.exceptionReport.ExceptionReport;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.mapper.coverWorkAttr.CoverWorkAttrMapper;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.listener.TopicMessageListen;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

/**
 * 工单属性Service
 * @author Peter
 * @version 2021-02-20
 */
@Service
@Transactional(readOnly = true)
public class CoverWorkAttrService extends CrudService<CoverWorkAttrMapper, CoverWorkAttr> {

	public final String format = "yyyy-MM-dd HH:mm:ss";
	public SimpleDateFormat sdf = new SimpleDateFormat(format);

	@Autowired
	private SystemService systemService;

	private static final Logger logger = LoggerFactory.getLogger(TopicMessageListen.class);

	public CoverWorkAttr get(String id) {
		return super.get(id);
	}
	
	public List<CoverWorkAttr> findList(CoverWorkAttr coverWorkAttr) {
		return super.findList(coverWorkAttr);
	}
	
	public Page<CoverWorkAttr> findPage(Page<CoverWorkAttr> page, CoverWorkAttr coverWorkAttr) {
		return super.findPage(page, coverWorkAttr);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverWorkAttr coverWorkAttr) {
		super.save(coverWorkAttr);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverWorkAttr coverWorkAttr) {
		super.delete(coverWorkAttr);
	}


	public boolean saveAttrForExceptionReport(CoverWork coverWork, ExceptionReport exceptionReport) {
		try {
			if (coverWork == null || StringUtils.isBlank(coverWork.getId())) {
				throw new Exception("保存工单属性异常:工单为空");
			}
			if (exceptionReport != null) {
				List<CoverWorkAttr> attrs = new ArrayList<>();
				User createUser = systemService.getUser(exceptionReport.getCreateBy().getId());
				CoverWorkAttr attrCreateByName = new CoverWorkAttr(coverWork.getId(), "上报人","createByName" , createUser.getLoginName(), "STRING", 1);
				CoverWorkAttr attrCreateDate = new CoverWorkAttr(coverWork.getId(), "上报时间","createDate" , sdf.format(exceptionReport.getCreateDate()), "STRING", 2);
				CoverWorkAttr attrAddress = new CoverWorkAttr(coverWork.getId(), "所在地区","address" , exceptionReport.getAddress(), "STRING", 3);
				CoverWorkAttr attrLng = new CoverWorkAttr(coverWork.getId(), "经度","lng" , String.valueOf(exceptionReport.getLng()), "STRING", 4);
				CoverWorkAttr attrLat = new CoverWorkAttr(coverWork.getId(), "纬度","lat" , String.valueOf(exceptionReport.getLat()), "STRING",5);
				CoverWorkAttr attrImages = new CoverWorkAttr(coverWork.getId(), "照片","imageIds" , String.valueOf(exceptionReport.getImageIds()), "STRING", 6);
				attrs.add(attrCreateByName);
				attrs.add(attrCreateDate);
				attrs.add(attrAddress);
				attrs.add(attrLng);
				attrs.add(attrLat);
				attrs.add(attrImages);

				for (CoverWorkAttr attr : attrs) {
					this.save(attr);
				}
			}
		} catch (Exception e) {
			logger.error("存储工单对应异常上报属性异常：" + e.getMessage());
			return false;
		}
		return true;
	}


	
}
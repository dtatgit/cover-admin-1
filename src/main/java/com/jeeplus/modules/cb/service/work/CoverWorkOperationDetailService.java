/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import java.util.ArrayList;
import java.util.List;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sys.entity.SystemConfig;
import com.jeeplus.modules.sys.service.SystemConfigService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.work.CoverWorkOperationDetail;
import com.jeeplus.modules.cb.mapper.work.CoverWorkOperationDetailMapper;

/**
 * 工单操作记录明细Service
 * @author crj
 * @version 2019-08-07
 */
@Service
@Transactional(readOnly = true)
public class CoverWorkOperationDetailService extends CrudService<CoverWorkOperationDetailMapper, CoverWorkOperationDetail> {
	@Autowired
	private SystemConfigService systemConfigService;
	public CoverWorkOperationDetail get(String id) {
		return super.get(id);
	}
	
	public List<CoverWorkOperationDetail> findList(CoverWorkOperationDetail coverWorkOperationDetail) {
		return super.findList(coverWorkOperationDetail);
	}
	
	public Page<CoverWorkOperationDetail> findPage(Page<CoverWorkOperationDetail> page, CoverWorkOperationDetail coverWorkOperationDetail) {
		return super.findPage(page, coverWorkOperationDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverWorkOperationDetail coverWorkOperationDetail) {
		String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
		String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
		if(StringUtils.isNotEmpty(projectId)) {
			coverWorkOperationDetail.setProjectId(projectId);
		}
		if(StringUtils.isNotEmpty(projectName)) {
			coverWorkOperationDetail.setProjectName(projectName);
		}
		super.save(coverWorkOperationDetail);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverWorkOperationDetail coverWorkOperationDetail) {
		super.delete(coverWorkOperationDetail);
	}

	/**
	 *
	 * @param workOperationId  工单操作记录ID
	 * @return
	 */
	public List<CoverWorkOperationDetail> obtainDetail(String workOperationId){
		CoverWorkOperationDetail queryDetail=new CoverWorkOperationDetail();
		queryDetail.setCoverWorkOperationId(workOperationId);
		List<CoverWorkOperationDetail> detailList=super.findList(queryDetail);
		return detailList;
	}

	/**
	 *
	 * @param coverWorkId  工单ID
	 * @param operationType 操作类型（安装和维护）
	 * @return
	 */
	public List<CoverWorkOperationDetail> obtainDetailByWork(String coverWorkId,String operationType ){
		CoverWorkOperationDetail queryDetail=new CoverWorkOperationDetail();
		queryDetail.setCoverWorkId(coverWorkId);
		queryDetail.setOperation(operationType);
		List<CoverWorkOperationDetail> detailList=super.findList(queryDetail);
		if(null!=detailList&&detailList.size()>0){
			for(CoverWorkOperationDetail detail:detailList){
				detail=changeImage(detail);

			}

		}
		return detailList;
	}

	public CoverWorkOperationDetail changeImage(CoverWorkOperationDetail detail){
		String image=detail.getImage();
		List<String> imageList=new ArrayList<String>();
		SystemConfig entity = systemConfigService.get("1");;
		if(StringUtils.isNotEmpty(image)){
			String[] images=image.split(",");
			for(String s:images){
				if(null!=entity){
					StringBuffer sb=new StringBuffer(entity.getUrl());
					sb.append(s);
					imageList.add(sb.toString());

				}
			}
			detail.setImageList(imageList);
		}
		return detail;
	}
	
}
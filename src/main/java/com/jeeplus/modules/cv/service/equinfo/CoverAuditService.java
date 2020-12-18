/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.Date;
import java.util.List;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.entity.task.CoverTaskProcess;
import com.jeeplus.modules.cv.mapper.equinfo.CoverMapper;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.equinfo.CoverAudit;
import com.jeeplus.modules.cv.mapper.equinfo.CoverAuditMapper;

/**
 * 井盖审核信息Service
 * @author crj
 * @version 2019-04-24
 */
@Service
@Transactional(readOnly = true)
public class CoverAuditService extends CrudService<CoverAuditMapper, CoverAudit> {
	@Autowired
	private CoverMapper coverMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CoverAuditMapper coverAuditMapper;

	public CoverAudit get(String id) {
		//return super.get(id);
		CoverAudit p=super.get(id);
		if(null!=p){
			if(null!=p.getCover()&&StringUtils.isNotEmpty(p.getCover().getId())){
				p.setCover(coverMapper.get(p.getCover().getId()));
			}
			if(null!=p.getAuditUser()&&StringUtils.isNotEmpty(p.getAuditUser().getId())){
				p.setAuditUser(userMapper.get(p.getAuditUser().getId()));
			}
		}

		return p;
	}
	
	public List<CoverAudit> findList(CoverAudit coverAudit) {
		//return super.findList(coverAudit);
		List<CoverAudit> processList=super.findList(coverAudit);
		if(null!=processList&&processList.size()>0){
			for(CoverAudit p:processList){
				if(null!=p.getCover()&&StringUtils.isNotEmpty(p.getCover().getId())){
					p.setCover(coverMapper.get(p.getCover().getId()));
				}
				if(null!=p.getAuditUser()&&StringUtils.isNotEmpty(p.getAuditUser().getId())){
					p.setAuditUser(userMapper.get(p.getAuditUser().getId()));
				}
			}
		}
		return processList;
	}
	
	public Page<CoverAudit> findPage(Page<CoverAudit> page, CoverAudit coverAudit) {
		//return super.findPage(page, coverAudit);
		Page<CoverAudit> pageValue=super.findPage(page, coverAudit);
		List<CoverAudit> list=pageValue.getList();
		if(null!=list&&list.size()>0){
			for(CoverAudit p:list){
				if(null!=p.getCover()&&StringUtils.isNotEmpty(p.getCover().getId())){
					p.setCover(coverMapper.get(p.getCover().getId()));
				}
				if(null!=p.getAuditUser()&&StringUtils.isNotEmpty(p.getAuditUser().getId())){
					p.setAuditUser(userMapper.get(p.getAuditUser().getId()));
				}
			}
		}
		return pageValue;
	}
	
	@Transactional(readOnly = false)
	public void save(CoverAudit coverAudit) {
		super.save(coverAudit);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverAudit coverAudit) {
		super.delete(coverAudit);
	}
	@Transactional(readOnly = false)
	public String obtainCover(Area coverArea){
        String resultRerurn="";
	try {
	String name = "";//区域名称
	String type = "";// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县;5：街道）
	List<Cover> coverList = null;
	//cover.setDataScope("sql内容");
	if (null != coverArea) {
		name = coverArea.getName();
		type = coverArea.getType();
	}
	if (StringUtils.isNotEmpty(type) && type.equals("3")) {//地市
		coverList = coverMapper.findCoverForAudit("a.city", name);
	} else if (StringUtils.isNotEmpty(type) && type.equals("4")) {//区县
		coverList = coverMapper.findCoverForAudit("a.district", name);
	} else if (StringUtils.isNotEmpty(type) && type.equals("5")) {//街道
		coverList = coverMapper.findCoverForAudit("a.township", name);
	}else{
        coverList = coverMapper.findCoverForAudit("a.del_flag", 0);
    }
		System.out.println("*****************"+coverList.size());
	if (null != coverList && coverList.size() > 0) {
		for (Cover v : coverList) {
			int flag = coverMapper.updateForAudit(v.getId());//返回1更新成功，返回0更新失败
			logger.info("*********更新待审核井盖返回结果********" + flag);
			if (flag == 1) {
				//生成井盖审核记录
                CoverAudit coverAudit=coverApply(v);
                resultRerurn=coverAudit.getId();
				break;
			}


		}
	}
		}catch(Exception e){
		resultRerurn="";
			e.printStackTrace();
		}

return resultRerurn;
	}
	@Transactional(readOnly = false)
	public CoverAudit coverApply(Cover cover){

		User user = UserUtils.getUser();
		//生成井盖审核记录
		CoverAudit coverAudit=new CoverAudit();
		coverAudit.setId(IdGen.uuid());
		coverAudit.setDelFlag("0");
		coverAudit.setCreateBy(user);
		coverAudit.setCreateDate(new Date());
		coverAudit.setUpdateBy(user);
		coverAudit.setUpdateDate(new Date());

		//待审核
		if(StringUtils.isNotEmpty(cover.getCoverStatus())&&cover.getCoverStatus().equals(CodeConstant.COVER_STATUS.WAIT_AUDIT)){
			coverAudit.setApplyItem(CodeConstant.APPLY_ITEM.ADD);
		//待删除
		}else if(StringUtils.isNotEmpty(cover.getCoverStatus())&&cover.getCoverStatus().equals(CodeConstant.COVER_STATUS.DELETEING)){
			coverAudit.setApplyItem(CodeConstant.APPLY_ITEM.DELETE);
		}
		coverAudit.setAuditStatus(CodeConstant.AUDIT_STATUS.AUDITING);
		coverAudit.setCover(cover);
		coverAudit.setAuditUser(user);
		coverAudit.setProjectId(cover.getProjectId());
		coverAudit.setProjectName(cover.getProjectName());
		coverAuditMapper.insert(coverAudit);

		return coverAudit;
	}

	@Transactional(readOnly = false)
	public boolean auditCover(CoverAudit coverAudit){
		boolean flag=true;
		try {
			String applyItem = coverAudit.getApplyItem();// 申请事项
			String auditStatus = coverAudit.getAuditStatus();// 审核状态
			String auditResult = coverAudit.getAuditResult();    // 结果描述

			System.out.println("***********************" + coverAudit.getAuditStatus());
			System.out.println("***********************" + coverAudit.getAuditResult());
			CoverAudit newCoverAudit = coverAuditMapper.get(coverAudit.getId());
			newCoverAudit.setAuditResult(auditResult);
			newCoverAudit.setAuditTime(new Date());//审核时间
			//审核失败
			if (StringUtils.isNotEmpty(auditStatus) && auditStatus.equals(CodeConstant.AUDIT_STATUS.AUDIT_FAIL)) {
				newCoverAudit.setAuditStatus(CodeConstant.AUDIT_STATUS.AUDIT_FAIL);

				Cover cover=coverMapper.get(coverAudit.getCover().getId());
				cover.setCoverStatus(CodeConstant.COVER_STATUS.AUDIT_FAIL);
				cover.setUpdateDate(new Date());
				cover.setUpdateBy(coverAudit.getAuditUser());
				coverMapper.update(cover);
				//审核成功
			} else if (StringUtils.isNotEmpty(auditStatus) && auditStatus.equals(CodeConstant.AUDIT_STATUS.AUDIT_PASS)) {
				newCoverAudit.setAuditStatus(CodeConstant.AUDIT_STATUS.AUDIT_PASS);
				//根据申请事项，修改原始数据信息
				updateCoverByItem(coverAudit);
			}
			coverAuditMapper.update(newCoverAudit);
		}catch (Exception e){
			flag=false;
			e.printStackTrace();
		}
		return flag;
	}
	@Transactional(readOnly = false)
	public void updateCoverByItem(CoverAudit coverAudit) {
		String applyItem=coverAudit.getApplyItem();// 申请事项
		Cover cover=coverMapper.get(coverAudit.getCover().getId());
		if(StringUtils.isNotEmpty(applyItem)&&applyItem.equals(CodeConstant.APPLY_ITEM.ADD)){
			cover.setCoverStatus(CodeConstant.COVER_STATUS.AUDIT_PASS);

		}else if(StringUtils.isNotEmpty(applyItem)&&applyItem.equals(CodeConstant.APPLY_ITEM.DELETE)){//删除操作
			cover.setCoverStatus(CodeConstant.COVER_STATUS.AUDIT_PASS);
			cover.setDelFlag("1");
		}
		cover.setUpdateDate(new Date());
		cover.setUpdateBy(coverAudit.getAuditUser());
		coverMapper.update(cover);
	}


}
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
import com.jeeplus.modules.cv.mapper.equinfo.CoverMapper;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.User;
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
	private CoverAuditMapper coverAuditMapper;

	public CoverAudit get(String id) {
		return super.get(id);
	}
	
	public List<CoverAudit> findList(CoverAudit coverAudit) {
		return super.findList(coverAudit);
	}
	
	public Page<CoverAudit> findPage(Page<CoverAudit> page, CoverAudit coverAudit) {
		return super.findPage(page, coverAudit);
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
	public boolean obtainCover(Area coverArea){
		boolean resultRerurn=false;
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
		coverList = coverMapper.findCoverForAudit("city", name);
	} else if (StringUtils.isNotEmpty(type) && type.equals("4")) {//区县
		coverList = coverMapper.findCoverForAudit("district", name);
	} else if (StringUtils.isNotEmpty(type) && type.equals("5")) {//街道
		coverList = coverMapper.findCoverForAudit("township", name);
	}
		System.out.println("*****************"+coverList.size());
	if (null != coverList && coverList.size() > 0) {
		for (Cover v : coverList) {
			int flag = coverMapper.updateForAudit(v.getId());//返回1更新成功，返回0更新失败
			logger.info("*********更新待审核井盖返回结果********" + flag);
			if (flag == 1) {
				//生成井盖审核记录
				coverApply(v);
				resultRerurn=true;
				break;
			}


		}
	}
		}catch(Exception e){
		resultRerurn=false;
			e.printStackTrace();
		}

return resultRerurn;
	}
	@Transactional(readOnly = false)
	public void coverApply(Cover cover){
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
		coverAuditMapper.insert(coverAudit);
	}
	
}
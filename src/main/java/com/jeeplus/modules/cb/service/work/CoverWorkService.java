/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.work;

import java.util.List;

import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.mapper.equinfo.CoverBellMapper;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.mapper.work.CoverWorkMapper;

/**
 * 工单信息Service
 * @author crj
 * @version 2019-06-26
 */
@Service
@Transactional(readOnly = true)
public class CoverWorkService extends CrudService<CoverWorkMapper, CoverWork> {
	@Autowired
	private CoverBellMapper coverBellMapper;
	@Autowired
	private UserMapper userMapper;

	public CoverWork get(String id) {
		return super.get(id);
	}
	
	public List<CoverWork> findList(CoverWork coverWork) {
		return super.findList(coverWork);
	}
	
	public Page<CoverWork> findPage(Page<CoverWork> page, CoverWork coverWork) {
		return super.findPage(page, coverWork);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverWork coverWork) {
		 CoverBell bell=coverBellMapper.findUniqueByProperty("cover_id", coverWork.getCover().getId());
		 if(null!=bell){
			 coverWork.setCoverBellId(bell.getId());
		 }

		 //施工人员
		User conUser=coverWork.getConstructionUser();
		if(null!=conUser){//获取施工部门
			User conuser2=userMapper.get(conUser.getId());
			coverWork.setConstructionDepart(conuser2.getOffice());
		}



		User user = UserUtils.getUser();
		Office office=null;
		if (StringUtils.isNotBlank(user.getId())){
			office=user.getOffice();
		}
		if(null!=office){
			if (coverWork.getIsNewRecord()){
				coverWork.setCreateDepart(office.getId());//创建部门
				coverWork.setUpdateDepart(office.getId());//更新部门
			}else{
				coverWork.setUpdateDepart(office.getId());//更新部门
			}
		}

		super.save(coverWork);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverWork coverWork) {
		super.delete(coverWork);
	}
	
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;
import java.util.UUID;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.projectInfo.entity.ProjectInfo;
import com.jeeplus.modules.sys.constant.OfficeConstant;
import com.jeeplus.modules.sys.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.modules.sys.entity.Area;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.mapper.AreaMapper;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import com.jeeplus.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * @author jeeplus
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeMapper, Office> {



	@Autowired
	private OfficeMapper officeMapper;
	private AreaService areaService;
	
	public List<Office> findAll(){
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll){
		if (isAll != null && isAll){
			return UserUtils.getOfficeAllList();
		}else{
			return UserUtils.getOfficeList();
		}
	}
	
	@Transactional(readOnly = true)
	public List<Office> findList(Office office){
		office.setParentIds(office.getParentIds()+"%");
		return officeMapper.findByParentIdsLike(office);
	}
	
	@Transactional(readOnly = true)
	public Office getByCode(String code){
		return officeMapper.getByCode(code);
	}
	
	public List<Office> getChildren(String parentId){
		return officeMapper.getChildren(parentId);
	}
	
	@Transactional(readOnly = false)
	public void save(Office office) {
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}


	public List<Office> getAllChildren(String officeId){
		return officeMapper.getAllChildren(officeId);
	}


	public Office createProjectOffice(ProjectInfo projectInfo, User user) throws Exception {
		if (user == null) {
			throw new Exception("创建项目对应部门失败，用户不存在");
		}
		if (user.getOffice() == null) {
			throw new Exception("创建项目对应部门失败，用户部门不存在");
		}
		if (projectInfo == null) {
			throw new Exception("创建项目对应部门失败，项目为空");
		}
		Office office = user.getOffice();
		Office officeObj = new Office();
		officeObj.setName(projectInfo.getProjectName());
		officeObj.setCode(IdGen.getInfoCode(""));
		if (office.getGrade() != null) {
			officeObj.setGrade(String.valueOf(Integer.valueOf(office.getGrade()) + 1) );
		}
		officeObj.setParent(office);
		officeObj.setType(office.getType());
		officeObj.setUseable(Global.YES);
		officeObj.setType(OfficeConstant.OfficeType.DEPARTMENT);
		Area area=office.getArea();
		if(null==area){
			officeObj.setArea(areaService.get("a9beb8c645ff448d89e71f96dc97bc09"));
		}else{
			officeObj.setArea(office.getArea());
		}

		this.save(officeObj);
		return officeObj;
	}

	@Transactional(readOnly = true)
	public boolean checkOfficeName(String name){
		boolean flag=false;
		Office office=new Office();
		office.setName(name);
		List<Office> officeList=officeMapper.queryList(office);
		if(null!=officeList&&officeList.size()>0){
			flag=true;
		}

		return flag;
	}

	public Office createDownOffice(Office parentOffice) throws Exception {
		if (parentOffice == null) {
			throw new Exception("创建下级部门失败，父级部门不存在");
		}

		Office officeObj = new Office();
		officeObj.setName("业务部门");
		officeObj.setCode(IdGen.getInfoCode(""));
		if (parentOffice.getGrade() != null) {
			officeObj.setGrade(String.valueOf(Integer.valueOf(parentOffice.getGrade()) + 1) );
		}
		officeObj.setParent(parentOffice);
		officeObj.setType(parentOffice.getType());
		officeObj.setUseable(Global.YES);
		officeObj.setType(OfficeConstant.OfficeType.DEPARTMENT);
		Area area=parentOffice.getArea();
		if(null==area){
			officeObj.setArea(areaService.get("a9beb8c645ff448d89e71f96dc97bc09"));
		}else{
			officeObj.setArea(parentOffice.getArea());
		}
		//关联项目
		officeObj.setProjectId(parentOffice.getProjectId());
		officeObj.setProjectName(parentOffice.getProjectName());
		this.save(officeObj);
		return officeObj;
	}
}

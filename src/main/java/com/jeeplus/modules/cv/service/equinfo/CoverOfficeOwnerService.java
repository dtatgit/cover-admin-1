/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.equinfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.sys.entity.DictType;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.equinfo.CoverOfficeOwner;
import com.jeeplus.modules.cv.mapper.equinfo.CoverOfficeOwnerMapper;

/**
 * 井盖维护单位配置Service
 * @author crj
 * @version 2019-11-07
 */
@Service
@Transactional(readOnly = true)
public class CoverOfficeOwnerService extends CrudService<CoverOfficeOwnerMapper, CoverOfficeOwner> {
	@Autowired
	private CoverCollectStatisMapper coverCollectStatisMapper;
	@Autowired
	private OfficeService officeService;

	public CoverOfficeOwner get(String id) {
		return super.get(id);
	}
	
	public List<CoverOfficeOwner> findList(CoverOfficeOwner coverOfficeOwner) {
		return super.findList(coverOfficeOwner);
	}
	
	public Page<CoverOfficeOwner> findPage(Page<CoverOfficeOwner> page, CoverOfficeOwner coverOfficeOwner) {
		return super.findPage(page, coverOfficeOwner);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverOfficeOwner coverOfficeOwner) {
		super.save(coverOfficeOwner);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverOfficeOwner coverOfficeOwner) {
		super.delete(coverOfficeOwner);
	}


	/**
	 * 权属单位数据处理
	 */
	@Transactional(readOnly = false)
	public boolean office0wnerHandle(){
		boolean flag=true;
		try {
			StringBuffer lineSQL=new StringBuffer("select count(a.id) AS amount ,a.owner_depart AS ownerDepart  from cover a ");
			lineSQL.append("  group by a.owner_depart order by count(a.id) desc ");
			String coverSQL=lineSQL.toString();
			List<Map<String, Object>> ownerDepartList = coverCollectStatisMapper.selectBySql(coverSQL);
			if(null!=ownerDepartList&&ownerDepartList.size()>0){

				for(int i=0;i<ownerDepartList.size();i++){
					Map<String, Object> map=ownerDepartList.get(i);
					//Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
					String ownerName=String.valueOf(map.get("ownerDepart"));
					if(StringUtils.isNotEmpty(ownerName)&&!checkOwnerDepart(ownerName)){
						CoverOfficeOwner coverOfficeOwner=new CoverOfficeOwner();
						coverOfficeOwner.setOwnerDepart(ownerName);
						coverOfficeOwner.setProjectId(UserUtils.getUser().getOffice().getProjectId());
						coverOfficeOwner.setProjectId(UserUtils.getUser().getOffice().getProjectName());
						super.save(coverOfficeOwner);
					}
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
			flag=false;
		}
		return flag;
		}

	public boolean checkOwnerDepart(String ownerDepart){
		boolean flag=false;
		CoverOfficeOwner coverOfficeOwner=new CoverOfficeOwner();
		coverOfficeOwner.setOwnerDepart(ownerDepart);
		List<CoverOfficeOwner> list=super.findList(coverOfficeOwner);
		if(null!=list&&list.size()>0){
			flag=true;
		}
		return flag;
	}

	public Office findOfficeByOwner(String ownerName){
		Office office=null;
		CoverOfficeOwner coverOfficeOwner=new CoverOfficeOwner();
		coverOfficeOwner.setOwnerDepart(ownerName);
		List<CoverOfficeOwner> ownerList=super.findList(coverOfficeOwner);
		if(null!=ownerList&&ownerList.size()>0){
			CoverOfficeOwner officeOwner=ownerList.get(0);
			if(StringUtils.isNotEmpty(officeOwner.getOffice().getId())){
				office=officeService.get(officeOwner.getOffice().getId());
			}
		}
		return office;
	}

}
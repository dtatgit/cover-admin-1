/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.equinfo;

import java.util.Date;
import java.util.List;

import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.equinfo.CoverBellOperation;
import com.jeeplus.modules.cb.mapper.equinfo.CoverBellOperationMapper;

/**
 * 井铃操作记录Service
 * @author crj
 * @version 2019-06-24
 */
@Service
@Transactional(readOnly = true)
public class CoverBellOperationService extends CrudService<CoverBellOperationMapper, CoverBellOperation> {
	@Autowired
	private CoverBellService coverBellService;
	@Autowired
	private OfficeService officeService;

	public CoverBellOperation get(String id) {
		return super.get(id);
	}
	
	public List<CoverBellOperation> findList(CoverBellOperation coverBellOperation) {
		return super.findList(coverBellOperation);
	}
	
	public Page<CoverBellOperation> findPage(Page<CoverBellOperation> page, CoverBellOperation coverBellOperation) {
		Page<CoverBellOperation> pageResult=super.findPage(page, coverBellOperation);
		List<CoverBellOperation> recordList=pageResult.getList();
		if(null!=recordList&&recordList.size()>0){
			for(CoverBellOperation record:recordList){
				Office office=officeService.get(record.getCreateDepart());
				record.setCreateDepart(office.getName());
			}
		}
		return super.findPage(page, coverBellOperation);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverBellOperation coverBellOperation) {
		super.save(coverBellOperation);
	}
	

	public void delete(CoverBellOperation coverBellOperation) {
		super.delete(coverBellOperation);
	}


	@Transactional(readOnly = false)
	public void genRecord(String operationType,String bellNo){
		CoverBell coverBell=coverBellService.findUniqueByProperty("a.bell_no",bellNo );
		CoverBellOperation coverBellOperation=new CoverBellOperation();
		User user = UserUtils.getUser();
		Office office=null;
		if (StringUtils.isNotBlank(user.getId())){
			office=user.getOffice();
		}
		if(null!=office){
			coverBellOperation.setCreateDepart(office.getId());// 操作部门
		}
		if(null!=coverBell){
			coverBellOperation.setCoverBellId(coverBell.getId());// 井铃ID
			coverBellOperation.setCoverId(coverBell.getCoverId());// 井盖ID
			coverBellOperation.setOperationType(operationType);// 操作类型
			super.save(coverBellOperation);
		}
	}
	
}
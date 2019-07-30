/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.service.equinfo;

import java.util.List;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.api.pojo.Result;
import com.jeeplus.modules.api.service.DeviceService;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.mapper.equinfo.CoverBellMapper;

/**
 * 井铃设备信息Service
 * @author crj
 * @version 2019-06-24
 */
@Service
@Transactional(readOnly = true)
public class CoverBellService extends CrudService<CoverBellMapper, CoverBell> {
	@Autowired
	private CoverService coverService;
	@Autowired
	private DeviceService deviceService;
	public CoverBell get(String id) {
		CoverBell bell=super.get(id);
		if(StringUtils.isNotEmpty(bell.getCoverId())){
			bell.setCover(coverService.get(bell.getCoverId()));
		}
		return bell;
	}
	
	public List<CoverBell> findList(CoverBell coverBell) {
		return super.findList(coverBell);
	}
	
	public Page<CoverBell> findPage(Page<CoverBell> page, CoverBell coverBell) {
		return super.findPage(page, coverBell);
	}
	
	@Transactional(readOnly = false)
	public void save(CoverBell coverBell) {
		Cover cover=coverBell.getCover();
		if(null!=cover){
			coverBell.setCoverId(cover.getId());
			coverBell.setCoverNo(cover.getNo());
		}
		super.save(coverBell);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverBell coverBell) {
		super.delete(coverBell);
	}

	/**
	 * 井卫设备设防撤防操作
	 * @param coverBell
	 * @param defenseStatus
	 * @return
	 */
	@Transactional(readOnly = false)
	public  Result  setDefense(CoverBell coverBell,String defenseStatus){
		Result result = deviceService.setDefense(coverBell.getBellNo(), defenseStatus);
		if(null!=result){
			String success=result.getSuccess();
			if(StringUtils.isNotEmpty(success)&&success.equals("true")){
				coverBell.setDefenseStatus(defenseStatus);
				super.save(coverBell);
			}
		}

		return result;
	}

	
}
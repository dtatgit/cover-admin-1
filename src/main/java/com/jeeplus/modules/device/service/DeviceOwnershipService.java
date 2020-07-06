/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.device.entity.DeviceOwnership;
import com.jeeplus.modules.device.mapper.DeviceOwnershipMapper;

/**
 * 设备归属管理Service
 * @author ffy
 * @version 2020-06-19
 */
@Service
@Transactional(readOnly = true)
public class DeviceOwnershipService extends CrudService<DeviceOwnershipMapper, DeviceOwnership> {

	public DeviceOwnership get(String id) {
		return super.get(id);
	}
	
	public List<DeviceOwnership> findList(DeviceOwnership deviceOwnership) {
		return super.findList(deviceOwnership);
	}
	
	public Page<DeviceOwnership> findPage(Page<DeviceOwnership> page, DeviceOwnership deviceOwnership) {
		return super.findPage(page, deviceOwnership);
	}
	
	@Transactional(readOnly = false)
	public void save(DeviceOwnership deviceOwnership) {
		super.save(deviceOwnership);
	}
	
	@Transactional(readOnly = false)
	public void delete(DeviceOwnership deviceOwnership) {
		super.delete(deviceOwnership);
	}
	
}
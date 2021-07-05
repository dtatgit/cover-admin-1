/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.SysUpload;
import com.jeeplus.modules.sys.mapper.SysUploadMapper;

/**
 * 文件管理Service
 * @author ffy
 * @version 2021-07-05
 */
@Service
@Transactional(readOnly = true)
public class SysUploadService extends CrudService<SysUploadMapper, SysUpload> {

	public SysUpload get(String id) {
		return super.get(id);
	}
	
	public List<SysUpload> findList(SysUpload sysUpload) {
		return super.findList(sysUpload);
	}
	
	public Page<SysUpload> findPage(Page<SysUpload> page, SysUpload sysUpload) {
		return super.findPage(page, sysUpload);
	}
	
	@Transactional(readOnly = false)
	public void save(SysUpload sysUpload) {
		super.save(sysUpload);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysUpload sysUpload) {
		super.delete(sysUpload);
	}
	
}
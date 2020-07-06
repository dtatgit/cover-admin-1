/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.device.entity.ServerUrl;
import com.jeeplus.modules.device.mapper.ServerUrlMapper;

/**
 * 服务器管理Service
 * @author ffy
 * @version 2020-06-19
 */
@Service
@Transactional(readOnly = true)
public class ServerUrlService extends CrudService<ServerUrlMapper, ServerUrl> {

	public ServerUrl get(String id) {
		return super.get(id);
	}
	
	public List<ServerUrl> findList(ServerUrl serverUrl) {
		return super.findList(serverUrl);
	}
	
	public Page<ServerUrl> findPage(Page<ServerUrl> page, ServerUrl serverUrl) {
		return super.findPage(page, serverUrl);
	}
	
	@Transactional(readOnly = false)
	public void save(ServerUrl serverUrl) {
		super.save(serverUrl);
	}
	
	@Transactional(readOnly = false)
	public void delete(ServerUrl serverUrl) {
		super.delete(serverUrl);
	}
	
}
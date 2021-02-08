/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.MsgPushConfig;
import com.jeeplus.modules.sys.mapper.MsgPushConfigMapper;

/**
 * 消息推送配置Service
 * @author crj
 * @version 2021-02-08
 */
@Service
@Transactional(readOnly = true)
public class MsgPushConfigService extends CrudService<MsgPushConfigMapper, MsgPushConfig> {

	public MsgPushConfig get(String id) {
		return super.get(id);
	}
	
	public List<MsgPushConfig> findList(MsgPushConfig msgPushConfig) {
		return super.findList(msgPushConfig);
	}
	
	public Page<MsgPushConfig> findPage(Page<MsgPushConfig> page, MsgPushConfig msgPushConfig) {
		return super.findPage(page, msgPushConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(MsgPushConfig msgPushConfig) {
		super.save(msgPushConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(MsgPushConfig msgPushConfig) {
		super.delete(msgPushConfig);
	}
	
}
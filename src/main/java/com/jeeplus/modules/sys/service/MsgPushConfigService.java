/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private SystemService systemService;
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

	/**
	 *
	 * @param noticeOfficeId 消息推送部门id
	 * @param noticeType   消息推送类型
	 */
	public void pushMsg(String noticeOfficeId,String noticeType){
		MsgPushConfig msgPushConfig=new MsgPushConfig();
		msgPushConfig.setNoticeOfficeId(noticeOfficeId);
		msgPushConfig.setNoticeType(noticeType);
		List<MsgPushConfig> msgPushConfigList=super.findList(msgPushConfig);
		if(CollectionUtil.isNotEmpty(msgPushConfigList)){
			for(MsgPushConfig pushConfig:msgPushConfigList){
				String pushMode=pushConfig.getPushMode();// 推送方式
				User noticePerson=pushConfig.getNoticePerson();// 通知人员
				if(StringUtils.isNotEmpty(pushMode)&&pushMode.equals(CodeConstant.push_mode.message)){//短息推送
					User user=systemService.getUser(noticePerson.getId());
					user.getMobile();

				}else if(StringUtils.isNotEmpty(pushMode)&&pushMode.equals(CodeConstant.push_mode.mobile)){//手机APP推送

				}

			}

		}
	}
	
}
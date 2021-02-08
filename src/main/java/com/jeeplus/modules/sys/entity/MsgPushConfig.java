/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;

import com.jeeplus.modules.sys.entity.User;
import java.util.Date;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 消息推送配置Entity
 * @author crj
 * @version 2021-02-08
 */
public class MsgPushConfig extends DataEntity<MsgPushConfig> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 通知标题
	private String state;		// 使用状态
	private String noticeType;		// 通知类型
	private String pushMode;		// 推送方式
	private User noticePerson;		// 通知人员
	private String createUserName;		// 创建用户
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public MsgPushConfig() {
		super();
	}

	public MsgPushConfig(String id){
		super(id);
	}

	@ExcelField(title="通知标题", align=2, sort=5)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="使用状态", dictType="on_off", align=2, sort=6)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@ExcelField(title="通知类型", dictType="notice_Type", align=2, sort=7)
	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	
	@ExcelField(title="推送方式", dictType="push_Mode", align=2, sort=8)
	public String getPushMode() {
		return pushMode;
	}

	public void setPushMode(String pushMode) {
		this.pushMode = pushMode;
	}
	
	@ExcelField(title="通知人员", fieldType=User.class, value="noticePerson.name", align=2, sort=9)
	public User getNoticePerson() {
		return noticePerson;
	}

	public void setNoticePerson(User noticePerson) {
		this.noticePerson = noticePerson;
	}
	
	@ExcelField(title="创建用户", align=2, sort=10)
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
		
}
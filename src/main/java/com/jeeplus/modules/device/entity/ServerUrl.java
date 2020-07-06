/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 服务器管理Entity
 * @author ffy
 * @version 2020-06-19
 */
public class ServerUrl extends DataEntity<ServerUrl> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String url;		// url地址
	
	public ServerUrl() {
		super();
	}

	public ServerUrl(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="url地址", align=2, sort=2)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
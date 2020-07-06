/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.device.entity;

import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 设备归属管理Entity
 * @author ffy
 * @version 2020-06-19
 */
public class DeviceOwnership extends DataEntity<DeviceOwnership> {
	
	private static final long serialVersionUID = 1L;
	private String devId;		// 设备id
	private String dtype;		// 设备类型
	private String serverUrlId;		// url地址(表server_url的主键)
	
	public DeviceOwnership() {
		super();
	}

	public DeviceOwnership(String id){
		super(id);
	}

	@ExcelField(title="设备id", align=2, sort=1)
	public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}
	
	@ExcelField(title="设备类型", dictType="", align=2, sort=2)
	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public String getServerUrlId() {
		return serverUrlId;
	}

	public void setServerUrlId(String serverUrlId) {
		this.serverUrlId = serverUrlId;
	}
}
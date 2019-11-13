/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.equinfo;

import com.jeeplus.modules.sys.entity.Office;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井盖维护单位配置Entity
 * @author crj
 * @version 2019-11-07
 */
public class CoverOfficeOwner extends DataEntity<CoverOfficeOwner> {
	
	private static final long serialVersionUID = 1L;
	private Office office;		// 部门
	private String ownerDepart;		// 权属单位
	
	public CoverOfficeOwner() {
		super();
	}

	public CoverOfficeOwner(String id){
		super(id);
	}

	@ExcelField(title="部门", fieldType=Office.class, value="office.name", align=2, sort=5)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@ExcelField(title="权属单位", align=2, sort=6)
	public String getOwnerDepart() {
		return ownerDepart;
	}

	public void setOwnerDepart(String ownerDepart) {
		this.ownerDepart = ownerDepart;
	}
	
}
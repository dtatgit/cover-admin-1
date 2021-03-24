/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.equinfo;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井盖病害Entity
 * @author crj
 * @version 2019-04-28
 */
public class CoverDamage extends DataEntity<CoverDamage> {
	
	private static final long serialVersionUID = 1L;
	private String coverId;		// cover_id
	private String damage;		// damage
	private String status;		// status
	
	public CoverDamage() {
		super();
	}

	public CoverDamage(String id){
		super(id);
	}

	@ExcelField(title="cover_id", align=2, sort=1)
	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}
	
	@ExcelField(title="damage", dictType="cover_damage", align=2, sort=2)
	public String getDamage() {
		return damage;
	}

	public void setDamage(String damage) {
		this.damage = damage;
	}
	
	@ExcelField(title="status", align=2, sort=5)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
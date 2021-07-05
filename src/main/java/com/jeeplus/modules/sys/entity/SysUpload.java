/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 文件管理Entity
 * @author ffy
 * @version 2021-07-05
 */
public class SysUpload extends DataEntity<SysUpload> {
	
	private static final long serialVersionUID = 1L;
	private String alias;		// alias
	private String category;		// category
	private String entityId;		// entity_id
	private String originName;		// origin_name
	private String mime;		// mime
	private String path;		// path
	private String uploadBy;		// upload_by
	private Date uploadDate;		// upload_date
	private Date expireDate;		// expire_date
	
	public SysUpload() {
		super();
	}

	public SysUpload(String id){
		super(id);
	}

	@ExcelField(title="alias", align=2, sort=1)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@ExcelField(title="category", align=2, sort=2)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@ExcelField(title="entity_id", align=2, sort=3)
	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	@ExcelField(title="origin_name", align=2, sort=4)
	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}
	
	@ExcelField(title="mime", align=2, sort=5)
	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}
	
	@ExcelField(title="path", align=2, sort=6)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	@ExcelField(title="upload_by", align=2, sort=7)
	public String getUploadBy() {
		return uploadBy;
	}

	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="upload_date", align=2, sort=8)
	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="expire_date", align=2, sort=9)
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
}
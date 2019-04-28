/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.equinfo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井盖图片信息Entity
 * @author crj
 * @version 2019-04-28
 */
public class CoverImage extends DataEntity<CoverImage> {
	
	private static final long serialVersionUID = 1L;
	private String coverId;		// cover_id
	private String uploadid;		// uploadid
	private String url;		// url
	private String uploadBy;		// upload_by
	private Date uploadDate;		// upload_date
	private String status;		// status
	
	public CoverImage() {
		super();
	}

	public CoverImage(String id){
		super(id);
	}

	@ExcelField(title="cover_id", align=2, sort=1)
	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}
	
	@ExcelField(title="uploadid", align=2, sort=2)
	public String getUploadid() {
		return uploadid;
	}

	public void setUploadid(String uploadid) {
		this.uploadid = uploadid;
	}
	
	@ExcelField(title="url", align=2, sort=3)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@ExcelField(title="upload_by", align=2, sort=4)
	public String getUploadBy() {
		return uploadBy;
	}

	public void setUploadBy(String uploadBy) {
		this.uploadBy = uploadBy;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="upload_date不能为空")
	@ExcelField(title="upload_date", align=2, sort=5)
	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	@ExcelField(title="status", align=2, sort=6)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
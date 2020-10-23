/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.exceptionReport;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 异常上报Entity
 * @author Peter
 * @version 2020-10-19
 */
public class ExceptionReport extends DataEntity<ExceptionReport> {
	
	private static final long serialVersionUID = 1L;
	private String coverWorkId;		// 工单号
	private String address;		// 所在地区
	private String createByName; //上报人名称
	private String checkBy;		// 审核人
	private String checkByName; //审核人名称
	private String checkStatus;		// 审核状态
	private Date checkDate;		// 审核时间
	private String reason; //异常原因
	private String passNotReason; //不通过原因
	private String imageIds;//上报图片id
	private List<String> imageList;// 上传图片list

	public ExceptionReport() {
		super();
	}

	public ExceptionReport(String id){
		super(id);
	}

	@ExcelField(title="工单号", align=2, sort=1)
	public String getCoverWorkId() {
		return coverWorkId;
	}

	public void setCoverWorkId(String coverWorkId) {
		this.coverWorkId = coverWorkId;
	}
	
	@ExcelField(title="所在地区", align=2, sort=2)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="审核人", align=2, sort=5)
	public String getCheckBy() {
		return checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}
	
	@ExcelField(title="审核状态", dictType="del_flag", align=2, sort=6)
	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审核时间", align=2, sort=7)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getCheckByName() {
		return checkByName;
	}

	public void setCheckByName(String checkByName) {
		this.checkByName = checkByName;
	}

	public String getPassNotReason() {
		return passNotReason;
	}

	public void setPassNotReason(String passNotReason) {
		this.passNotReason = passNotReason;
	}

	public String getImageIds() {
		return imageIds;
	}

	public void setImageIds(String imageIds) {
		this.imageIds = imageIds;
	}

	public List<String> getImageList() {
		if (StringUtils.isNotBlank(this.imageIds)) {
			return Arrays.asList(this.imageIds.split(","));
		}
		return null;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}
}
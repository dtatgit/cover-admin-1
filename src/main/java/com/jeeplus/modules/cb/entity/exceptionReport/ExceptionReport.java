/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.exceptionReport;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 异常上报Entity
 * @author Peter
 * @version 2020-10-19
 */
public class ExceptionReport extends DataEntity<ExceptionReport> {
	
	private static final long serialVersionUID = 1L;
	private String coverWorkId;		// 工单id
	private String coverWorkNo; //工单号
	private String address;		// 所在地区
	private String createByName; //上报人名称
	private String checkBy;		// 审核人
	private String checkByName; //审核人名称
	private String checkStatus;		// 审核状态
	private Date checkDate;		// 审核时间
	private String reason; //异常原因
	private String passNotReason; //不通过原因
	private String imageIds;//上报图片id
	private String workType; //生成工单类型
	private List<String> imageList;// 上传图片list
	private List<Office> officeList;//有效查询的单位
	private Date beginCreateDate; //创建开始时间
	private Date endCreateDate; //创建结束时间

	private Date beginCheckDate; //创建开始时间
	private Date endCheckDate; //创建结束时间
	private BigDecimal lng;
	private BigDecimal lat;

	private String ids; //生成工单ids


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

	public String getCoverWorkNo() {
		return coverWorkNo;
	}

	public void setCoverWorkNo(String coverWorkNo) {
		this.coverWorkNo = coverWorkNo;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public List<Office> getOfficeList() {
		return officeList;
	}

	public void setOfficeList(List<Office> officeList) {
		this.officeList = officeList;
	}


	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
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

	public Date getBeginCheckDate() {
		return beginCheckDate;
	}

	public void setBeginCheckDate(Date beginCheckDate) {
		this.beginCheckDate = beginCheckDate;
	}

	public Date getEndCheckDate() {
		return endCheckDate;
	}

	public void setEndCheckDate(Date endCheckDate) {
		this.endCheckDate = endCheckDate;
	}


	public BigDecimal getLng() {
		return lng;
	}

	public void setLng(BigDecimal lng) {
		this.lng = lng;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public void setLat(BigDecimal lat) {
		this.lat = lat;
	}
}
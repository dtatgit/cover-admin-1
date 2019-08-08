/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.work;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单操作记录明细Entity
 * @author crj
 * @version 2019-08-07
 */
public class CoverWorkOperationDetail extends DataEntity<CoverWorkOperationDetail> {
	
	private static final long serialVersionUID = 1L;
	private String coverWorkOperationId;		// 工单操作记录
	private String coverWorkId;		// 工单信息
	private String operation;		// 操作类型
	private String isOwnerDepart;		// 井盖权属
	private String isPurpose;		// 井盖用途
	private String isSituation;		// 地理场合
	private String isDamaged;		// 损坏形式
	private String image;		// 现场图片
	private String image1;		// 现场图片
	private String image2;		// 现场图片
	private String image3;		// 现场图片
	
	public CoverWorkOperationDetail() {
		super();
	}

	public CoverWorkOperationDetail(String id){
		super(id);
	}

	@ExcelField(title="工单操作记录", align=2, sort=7)
	public String getCoverWorkOperationId() {
		return coverWorkOperationId;
	}

	public void setCoverWorkOperationId(String coverWorkOperationId) {
		this.coverWorkOperationId = coverWorkOperationId;
	}
	
	@ExcelField(title="工单信息", align=2, sort=8)
	public String getCoverWorkId() {
		return coverWorkId;
	}

	public void setCoverWorkId(String coverWorkId) {
		this.coverWorkId = coverWorkId;
	}
	
	@ExcelField(title="操作类型", dictType="record_type", align=2, sort=9)
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	@ExcelField(title="井盖权属", dictType="boolean", align=2, sort=10)
	public String getIsOwnerDepart() {
		return isOwnerDepart;
	}

	public void setIsOwnerDepart(String isOwnerDepart) {
		this.isOwnerDepart = isOwnerDepart;
	}
	
	@ExcelField(title="井盖用途", dictType="boolean", align=2, sort=11)
	public String getIsPurpose() {
		return isPurpose;
	}

	public void setIsPurpose(String isPurpose) {
		this.isPurpose = isPurpose;
	}
	
	@ExcelField(title="地理场合", dictType="boolean", align=2, sort=12)
	public String getIsSituation() {
		return isSituation;
	}

	public void setIsSituation(String isSituation) {
		this.isSituation = isSituation;
	}
	
	@ExcelField(title="损坏形式", dictType="boolean", align=2, sort=13)
	public String getIsDamaged() {
		return isDamaged;
	}

	public void setIsDamaged(String isDamaged) {
		this.isDamaged = isDamaged;
	}
	
	@ExcelField(title="现场图片", align=2, sort=14)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@ExcelField(title="现场图片", align=2, sort=15)
	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}
	
	@ExcelField(title="现场图片", align=2, sort=16)
	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}
	
	@ExcelField(title="现场图片", align=2, sort=17)
	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}
	
}
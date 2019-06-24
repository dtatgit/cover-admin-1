/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.equinfo;

import java.util.Date;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井铃操作记录Entity
 * @author crj
 * @version 2019-06-24
 */
public class CoverBellOperation extends DataEntity<CoverBellOperation> {
	
	private static final long serialVersionUID = 1L;
	private String coverBellId;		// 井铃ID
	private String coverId;		// 井盖ID
	private String operationType;		// 操作类型
	private String createDepart;		// 操作部门
	private Date beginCreateDate;		// 开始 操作时间
	private Date endCreateDate;		// 结束 操作时间
	
	public CoverBellOperation() {
		super();
	}

	public CoverBellOperation(String id){
		super(id);
	}

	@ExcelField(title="井铃ID", align=2, sort=4)
	public String getCoverBellId() {
		return coverBellId;
	}

	public void setCoverBellId(String coverBellId) {
		this.coverBellId = coverBellId;
	}
	
	@ExcelField(title="井盖ID", align=2, sort=5)
	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}
	
	@ExcelField(title="操作类型", dictType="operation_type", align=2, sort=6)
	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	@ExcelField(title="操作部门", align=2, sort=9)
	public String getCreateDepart() {
		return createDepart;
	}

	public void setCreateDepart(String createDepart) {
		this.createDepart = createDepart;
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
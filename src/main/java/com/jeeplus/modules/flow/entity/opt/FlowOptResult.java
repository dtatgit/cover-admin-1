/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.entity.opt;

import com.jeeplus.modules.flow.entity.opt.FlowOpt;
import javax.validation.constraints.NotNull;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单流程操作结果定义Entity
 * @author crj
 * @version 2019-11-21
 */
public class FlowOptResult extends DataEntity<FlowOptResult> {
	
	private static final long serialVersionUID = 1L;
	private FlowOpt optId;		// 操作
	private String result;		// 结果
	private String optOrder;		// 排序
	
	public FlowOptResult() {
		super();
	}

	public FlowOptResult(String id){
		super(id);
	}

	@NotNull(message="操作不能为空")
	@ExcelField(title="操作", fieldType=FlowOpt.class, value="optId.optCode", align=2, sort=1)
	public FlowOpt getOptId() {
		return optId;
	}

	public void setOptId(FlowOpt optId) {
		this.optId = optId;
	}
	
	@ExcelField(title="结果", align=2, sort=2)
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@ExcelField(title="排序", align=2, sort=3)
	public String getOptOrder() {
		return optOrder;
	}

	public void setOptOrder(String optOrder) {
		this.optOrder = optOrder;
	}
	
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.entity.opt;

import com.jeeplus.modules.flow.entity.base.FlowProc;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单流程操作定义Entity
 * @author crj
 * @version 2019-11-21
 */
public class FlowOpt extends DataEntity<FlowOpt> {
	
	private static final long serialVersionUID = 1L;
	private FlowProc flowId;		// 流程
	private String fromState;		// 适用状态
	private String optOrder;		// 顺序
	private String optType;		// 操作类型
	private String optName;		// 操作名称
	private String optCode;		// 操作代码
	private String result;		// 结果状态
	private String target;		// 流转目标
	
	public FlowOpt() {
		super();
	}

	public FlowOpt(String id){
		super(id);
	}

	@ExcelField(title="流程", fieldType=FlowProc.class, value="flowId.flowNo", align=2, sort=1)
	public FlowProc getFlowId() {
		return flowId;
	}

	public void setFlowId(FlowProc flowId) {
		this.flowId = flowId;
	}
	
	@ExcelField(title="适用状态", align=2, sort=2)
	public String getFromState() {
		return fromState;
	}

	public void setFromState(String fromState) {
		this.fromState = fromState;
	}
	
	@ExcelField(title="顺序", align=2, sort=3)
	public String getOptOrder() {
		return optOrder;
	}

	public void setOptOrder(String optOrder) {
		this.optOrder = optOrder;
	}
	
	@ExcelField(title="操作类型", dictType="flow_opt_type", align=2, sort=4)
	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}
	
	@ExcelField(title="操作名称", align=2, sort=5)
	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}
	
	@ExcelField(title="操作代码", align=2, sort=6)
	public String getOptCode() {
		return optCode;
	}

	public void setOptCode(String optCode) {
		this.optCode = optCode;
	}
	
	@ExcelField(title="结果状态", align=2, sort=7)
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@ExcelField(title="流转目标", align=2, sort=8)
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
}
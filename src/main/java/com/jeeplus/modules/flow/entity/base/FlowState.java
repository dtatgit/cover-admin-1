/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.entity.base;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单流程状态Entity
 * @author crj
 * @version 2019-11-21
 */
public class FlowState extends DataEntity<FlowState> {
	
	private static final long serialVersionUID = 1L;
	private String stateName;		// 状态名称
	private String stateCode;		// 状态编码
	private String isStart;		// 是否初始状态
	private String isEnd;		// 是否结束状态
	
	public FlowState() {
		super();
	}

	public FlowState(String id){
		super(id);
	}

	@ExcelField(title="状态名称", align=2, sort=1)
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	@ExcelField(title="状态编码", align=2, sort=2)
	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	
	@ExcelField(title="是否初始状态", dictType="boolean", align=2, sort=3)
	public String getIsStart() {
		return isStart;
	}

	public void setIsStart(String isStart) {
		this.isStart = isStart;
	}
	
	@ExcelField(title="是否结束状态", dictType="boolean", align=2, sort=4)
	public String getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}
	
}
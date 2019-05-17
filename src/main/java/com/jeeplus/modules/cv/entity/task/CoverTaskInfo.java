/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.entity.task;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 井盖数据处理任务Entity
 * @author crj
 * @version 2019-05-16
 */
public class CoverTaskInfo extends DataEntity<CoverTaskInfo> {
	
	private static final long serialVersionUID = 1L;
	private String taskNo;		// 任务编号
	private String taskName;		// 任务名称
	private String taskStatus;		// 任务状态
	private String taskType;		// 任务类型
	private String taskNum;		// 任务数量
	private String taskContent;		// 任务内容
	private String spare;		// 备用
	
	public CoverTaskInfo() {
		super();
	}

	public CoverTaskInfo(String id){
		super(id);
	}

	@ExcelField(title="任务编号", align=2, sort=4)
	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	
	@ExcelField(title="任务名称", align=2, sort=5)
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	@ExcelField(title="任务状态", dictType="task_status", align=2, sort=6)
	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	@ExcelField(title="任务类型", align=2, sort=7)
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	@ExcelField(title="任务数量", align=2, sort=8)
	public String getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}
	
	@ExcelField(title="任务内容", align=2, sort=9)
	public String getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}
	
	@ExcelField(title="备用", align=2, sort=11)
	public String getSpare() {
		return spare;
	}

	public void setSpare(String spare) {
		this.spare = spare;
	}
	
}
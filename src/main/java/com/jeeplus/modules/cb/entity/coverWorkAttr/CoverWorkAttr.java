/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.entity.coverWorkAttr;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工单属性Entity
 * @author Peter
 * @version 2021-02-20
 */
public class CoverWorkAttr extends DataEntity<CoverWorkAttr> {
	
	private static final long serialVersionUID = 1L;
	private String coverWorkId;		// 工单ID
	private String title;		// 标题
	private String name;		// 名称
	private String value;		// 值
	private String dataType;		// 数据类型
	private Integer no;		// 序号

	public CoverWorkAttr() {
		super();
	}

	public CoverWorkAttr(String id){
		super(id);
	}

	@ExcelField(title="工单ID", align=2, sort=1)
	public String getCoverWorkId() {
		return coverWorkId;
	}

	public void setCoverWorkId(String coverWorkId) {
		this.coverWorkId = coverWorkId;
	}
	
	@ExcelField(title="标题", align=2, sort=2)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="名称", align=2, sort=3)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="值", align=2, sort=4)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@ExcelField(title="数据类型", align=2, sort=5)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public CoverWorkAttr(String coverWorkId, String title, String name, String value, String dataType, Integer no) {
		this.coverWorkId = coverWorkId;
		this.title = title;
		this.name = name;
		this.value = value;
		this.dataType = dataType;
		this.no = no;
	}
}
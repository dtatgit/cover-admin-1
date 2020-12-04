/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.service.task;

import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.task.CoverTaskInfo;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.sys.mapper.OfficeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.task.CoverTableField;
import com.jeeplus.modules.cv.mapper.task.CoverTableFieldMapper;

/**
 * 井盖任务数据权限Service
 * @author crj
 * @version 2019-05-17
 */
@Service
@Transactional(readOnly = true)
public class CoverTableFieldService extends CrudService<CoverTableFieldMapper, CoverTableField> {
	@Autowired
	private CoverCollectStatisMapper coverCollectStatisMapper;
	@Autowired
	private OfficeMapper officeMapper;
	public CoverTableField get(String id) {
		CoverTableField field=super.get(id);
		if(null!=field){
			if(null!=field.getOffice()&&StringUtils.isNotEmpty(field.getOffice().getId())){
				field.setOffice(officeMapper.get(field.getOffice().getId()));
			}
		}

		return field;
	}
	
	public List<CoverTableField> findList(CoverTableField coverTableField) {
		//return super.findList(coverTableField);
		List<CoverTableField> fieldList=super.findList(coverTableField);
		if(null!=fieldList&&fieldList.size()>0){
			for(CoverTableField field:fieldList){
				if(null!=field.getOffice()&&StringUtils.isNotEmpty(field.getOffice().getId())){
					field.setOffice(officeMapper.get(field.getOffice().getId()));
				}

			}
		}
		return fieldList;
	}
	
	public Page<CoverTableField> findPage(Page<CoverTableField> page, CoverTableField coverTableField) {
		//return super.findPage(page, coverTableField);
		Page<CoverTableField> pageValue=super.findPage(page, coverTableField);
		List<CoverTableField> list=pageValue.getList();
		if(null!=list&&list.size()>0){
			for(CoverTableField field:list){
				if(null!=field.getOffice()&&StringUtils.isNotEmpty(field.getOffice().getId())){
					field.setOffice(officeMapper.get(field.getOffice().getId()));
				}
			}
		}
		//return super.findPage(page, cover);
		return pageValue;
	}
	
	@Transactional(readOnly = false)
	public void save(CoverTableField coverTableField) {
		super.save(coverTableField);
	}
	
	@Transactional(readOnly = false)
	public void delete(CoverTableField coverTableField) {
		super.delete(coverTableField);
	}

	/**
	 * 生成任务数据字段权限控制
	 * @param coverTaskInfo 任务信息
	 * @param tableName 表的名称
	 * @param tableTitle 表的中文名称
	 */
	@Transactional(readOnly = false)
	public void generateTaskField(CoverTaskInfo coverTaskInfo, String tableName, String tableTitle){
		StringBuffer lineSQL=new StringBuffer("SELECT table_name,column_name,column_comment FROM information_schema. COLUMNS");
		if(StringUtils.isNotEmpty(tableName)){
			lineSQL.append(" WHERE TABLE_NAME = '").append(tableName).append("'");
		}
		String coverSQL=lineSQL.toString();
		List<Map<String, Object>> coverFieldList = coverCollectStatisMapper.selectBySql(coverSQL);
		if(null!=coverFieldList&&coverFieldList.size()>0){
			for(int i=0;i<coverFieldList.size();i++){
				Map<String, Object> map=coverFieldList.get(i);
				//String table_name=String.valueOf(map.get("table_name"));
				String column_name=String.valueOf(map.get("COLUMN_NAME"));
				String column_comment=String.valueOf(map.get("COLUMN_COMMENT"));
				CoverTableField field=new CoverTableField();
				field.setCoverTaskInfo(coverTaskInfo);
				field.setTableName(tableName);
				field.setTableTitle(tableTitle);
				field.setFieldName(column_name);
				field.setFieldTitle(column_comment);
				field.setIsEditField(CodeConstant.BOOLEAN.YES);
				field.setIsListField(CodeConstant.BOOLEAN.YES);
				field.setProjectId(coverTaskInfo.getProjectId());
				field.setProjectName(coverTaskInfo.getProjectName());
				super.save(field);
			}
		}
	}

	/**
	 * 根据任务获取字段是否显示
	 * @param coverTaskInfo
	 * @return
	 */
	public List<CoverTableField> obtainEditFieldsByTaskInfo(CoverTaskInfo coverTaskInfo,String tableName){
		//根据任务获取相应的字段权限
		CoverTableField field=new CoverTableField();
		field.setTableName(tableName);
		field.setCoverTaskInfo(coverTaskInfo);
		field.setIsEditField(CodeConstant.BOOLEAN.YES);
		List<CoverTableField> fieldList=super.findList(field);
		return fieldList;
	}
	
}
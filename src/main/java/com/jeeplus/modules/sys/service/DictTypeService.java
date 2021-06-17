/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.collection.CollectionUtil;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.cv.utils.EntityUtils;
import com.jeeplus.modules.projectInfo.entity.ProjectInfo;
import com.jeeplus.modules.sys.entity.DictType;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.mapper.DictTypeMapper;
import com.jeeplus.modules.sys.mapper.DictValueMapper;
import com.jeeplus.modules.sys.utils.DictUtils;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据字典Service
 * @author lgf
 * @version 2017-01-16
 */
@Service
@Transactional(readOnly = true)
public class DictTypeService extends CrudService<DictTypeMapper, DictType> {

	@Autowired
	private DictValueMapper dictValueMapper;
	@Autowired
	private DictTypeMapper dictTypeMapper;
	@Autowired
	private CoverCollectStatisMapper coverCollectStatisMapper;
	
	public DictType get(String id) {
		DictType dictType = super.get(id);
		dictType.setDictValueList(dictValueMapper.findList(new DictValue(dictType)));
		return dictType;
	}
	
	public DictValue getDictValue(String id) {
		return dictValueMapper.get(id);
	}
	
	public List<DictType> findList(DictType dictType) {
		return super.findList(dictType);
	}

	public List<DictType> findListByNames(String[] names) {
		List<DictType> listByNames = mapper.findListByNames(names);
		if(listByNames==null){
			return null;
		}
		List<DictType> collect = listByNames.stream().map(item -> {
			String id = item.getId();
			List<DictValue> list = dictValueMapper.findList(new DictValue(new DictType(id)));
			item.setDictValueList(list);
			return item;
		}).collect(Collectors.toList());

		return collect;
	}

	public Page<DictType> findPage(Page<DictType> page, DictType dictType) {
		return super.findPage(page, dictType);
	}
	
	@Transactional(readOnly = false)
	public void save(DictType dictType) {
		String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
		String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
		if(StringUtils.isNotEmpty(projectId)) {
			dictType.setProjectId(projectId);
		}
		if(StringUtils.isNotEmpty(projectName)) {
			dictType.setProjectName(projectName);
		}
		super.save(dictType);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void saveDictValue(DictValue dictValue) {
		if (StringUtils.isBlank(dictValue.getId())){
			dictValue.preInsert();
			String projectId= UserUtils.getProjectId();//获取当前登录用户的所属项目
			String projectName= UserUtils.getProjectName();//获取当前登录用户的所属项目
			if(StringUtils.isNotEmpty(projectId)) {
				dictValue.setProjectId(projectId);
			}
			if(StringUtils.isNotEmpty(projectName)) {
				dictValue.setProjectName(projectName);
			}
			dictValueMapper.insert(dictValue);
		}else{
			dictValue.preUpdate();
			dictValueMapper.update(dictValue);
		}
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void deleteDictValue(DictValue dictValue) {
		dictValueMapper.delete(dictValue);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void delete(DictType dictType) {
		super.delete(dictType);
		dictValueMapper.delete(new DictValue(dictType));
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	/**
	 * 权属单位数据处理
	 */
	@Transactional(readOnly = false)
	public void coverOwnerHandle(){
		DictType dictType=new DictType();
		dictType.setType("cover_owner_depart");
		List<DictType> ownerList= dictTypeMapper.findList(dictType);
		if(null!=ownerList&&ownerList.size()>0){
			dictType=ownerList.get(0);
			//select count(o.id) AS amount ,o.owner_name AS ownerName  from cover_owner o group by o.owner_name order by count(o.id) desc
			StringBuffer lineSQL=new StringBuffer("select count(a.id) AS amount ,a.owner_name AS ownerName  from cover_owner a");
			lineSQL.append("  where 1=1 ");
			lineSQL.append("  group by a.owner_name order by count(a.id) desc ");
			String coverSQL=lineSQL.toString();
			List<Map<String, Object>> coverPurposeList = coverCollectStatisMapper.selectBySql(coverSQL);
			if(null!=coverPurposeList&&coverPurposeList.size()>0){
				//先删除权属单位字典项
				dictValueMapper.delete(new DictValue(dictType));
				for(int i=0;i<coverPurposeList.size();i++){
					Map<String, Object> map=coverPurposeList.get(i);
					//Integer amount=Integer.parseInt(String.valueOf(map.get("amount")));
					String ownerName=String.valueOf(map.get("ownerName"));
					DictValue value=new DictValue();
					value.setDictType(dictType);
					value.setLabel(ownerName);
					value.setValue(ownerName);
					value.setSort(i+"");
					value.setId(IdGen.uuid());
					value.setCreateDate(new Date());
					//value.preInsert();
					dictValueMapper.insert(value);
				}
			}

		}


	}
	public List<DictValue> checkFindList(DictValue dictValue) {
		dataRuleFilter(dictValue);
		return dictValueMapper.checkFindList(dictValue);
	}

	public void synData(String standardProjectId,ProjectInfo projectInfo){
		//获取所有字典类型（字典类型为公用，没有通过项目区分）
		List<DictType> dictTypeList= super.findList(new DictType());
		if(CollectionUtil.isNotEmpty(dictTypeList)){
			for(DictType dictType:dictTypeList){
				DictValue queryValue=new DictValue();
				queryValue.setDictType(dictType);
				queryValue.setProjectId(standardProjectId);
				List<DictValue> dictValueList =dictValueMapper.checkFindList(queryValue);
				if(CollectionUtil.isNotEmpty(dictValueList)){
					for(DictValue oldDictValue:dictValueList){
						DictValue newDictValue=EntityUtils.copyData(oldDictValue,DictValue.class);
						newDictValue.setId(IdGen.uuid());
						newDictValue.setProjectId(projectInfo.getId());
						newDictValue.setProjectName(projectInfo.getProjectName());
						newDictValue.setCreateDate(new Date());
						newDictValue.setCreateBy(projectInfo.getCreateBy());
						dictValueMapper.insert(newDictValue);
					}
				}
			}

		}

	}
	@Transactional(readOnly = true)
	public boolean checkDictType(String type){
		boolean flag=false;
		DictType dictType=new DictType();
		dictType.setType(type);
		List<DictType> typeList=super.findList(dictType);
		if(null!=typeList&&typeList.size()>0){
			flag=true;
		}

		return flag;
	}
}
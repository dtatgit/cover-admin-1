/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.cv.vo.CollectionStatisVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.DictType;
import com.jeeplus.modules.sys.entity.DictValue;
import com.jeeplus.modules.sys.mapper.DictTypeMapper;
import com.jeeplus.modules.sys.mapper.DictValueMapper;
import com.jeeplus.modules.sys.utils.DictUtils;

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
	
	public Page<DictType> findPage(Page<DictType> page, DictType dictType) {
		return super.findPage(page, dictType);
	}
	
	@Transactional(readOnly = false)
	public void save(DictType dictType) {
		super.save(dictType);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void saveDictValue(DictValue dictValue) {
		if (StringUtils.isBlank(dictValue.getId())){
			dictValue.preInsert();
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
			StringBuffer lineSQL=new StringBuffer("select count(o.id) AS amount ,o.owner_name AS ownerName  from cover_owner o");
			//lineSQL.append("  where del_flag='0' and data_source !='import' ");
			lineSQL.append("  group by o.owner_name order by count(o.id) desc ");
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
	
}
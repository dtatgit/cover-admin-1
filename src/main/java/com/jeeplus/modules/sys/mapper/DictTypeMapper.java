/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.DictType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据字典MAPPER接口
 * @author lgf
 * @version 2017-01-16
 */
@MyBatisMapper
public interface DictTypeMapper extends BaseMapper<DictType> {


    List<DictType> findListByNames(@Param(value="names") String[] names);
}
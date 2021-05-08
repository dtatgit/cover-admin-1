/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.mapper.statis;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cv.entity.statis.OfficeOwnerStatis;

import java.util.List;

/**
 * 维护部门权属单位统计MAPPER接口
 * @author crj
 * @version 2021-03-22
 */
@MyBatisMapper
public interface OfficeOwnerStatisMapper extends BaseMapper<OfficeOwnerStatis> {

    public List<OfficeOwnerStatis> findOfficeList(OfficeOwnerStatis entity);

    public List<OfficeOwnerStatis> findOwnerDepartList(OfficeOwnerStatis entity);
}
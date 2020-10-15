/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.mapper.work;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cb.entity.work.CoverWork;

import java.util.List;
import java.util.Map;

/**
 * 工单信息MAPPER接口
 * @author crj
 * @version 2019-06-26
 */
@MyBatisMapper
public interface CoverWorkMapper extends BaseMapper<CoverWork> {

    public List<CoverWork> queryByParam(Map<String, Object> map);
}
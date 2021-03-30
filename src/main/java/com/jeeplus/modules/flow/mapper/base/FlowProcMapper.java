/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.mapper.base;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.flow.entity.base.FlowProc;

import java.util.List;

/**
 * 工单流程定义MAPPER接口
 * @author crj
 * @version 2019-11-21
 */
@MyBatisMapper
public interface FlowProcMapper extends BaseMapper<FlowProc> {
    public List<FlowProc> checkFindList(FlowProc entity);
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.flow.mapper.base;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.flow.entity.base.FlowUserOrg;

/**
 * 用户组织关系配置MAPPER接口
 * @author crj
 * @version 2019-11-21
 */
@MyBatisMapper
public interface FlowUserOrgMapper extends BaseMapper<FlowUserOrg> {
	
}
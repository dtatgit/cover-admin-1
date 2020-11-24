/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.mapper.coverBizAlarm;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cb.entity.coverBizAlarm.CoverBizAlarm;

import java.util.List;
import java.util.Map;

/**
 * 井盖业务报警MAPPER接口
 * @author Peter
 * @version 2020-10-13
 */
@MyBatisMapper
public interface CoverBizAlarmMapper extends BaseMapper<CoverBizAlarm> {

    public List<CoverBizAlarm> queryByParam(Map<String, Object> map);

}
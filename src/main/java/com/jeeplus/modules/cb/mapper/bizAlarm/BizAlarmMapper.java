/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.mapper.bizAlarm;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cb.entity.bizAlarm.BizAlarm;
import com.jeeplus.modules.cv.web.statis.BizAlarmParam;
import com.jeeplus.modules.cv.web.statis.BizAlarmStatis;
import com.jeeplus.modules.cv.web.statis.BizAlarmStatisBo;

import java.util.List;
import java.util.Map;

/**
 * 业务报警MAPPER接口
 * @author Peter
 * @version 2020-10-13
 */
@MyBatisMapper
public interface BizAlarmMapper extends BaseMapper<BizAlarm> {

  public List<BizAlarmStatisBo> statisByParam(BizAlarmParam param);

}
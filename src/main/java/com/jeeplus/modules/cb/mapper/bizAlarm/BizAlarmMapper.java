/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.mapper.bizAlarm;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cb.entity.bizAlarm.BizAlarm;
import com.jeeplus.modules.cv.entity.statis.BizAlarmParam;
import com.jeeplus.modules.cv.entity.statis.BizAlarmStatisBo;

import java.util.List;

/**
 * 业务报警MAPPER接口
 * @author Peter
 * @version 2020-10-13
 */
@MyBatisMapper
public interface BizAlarmMapper extends BaseMapper<BizAlarm> {

  public List<BizAlarmStatisBo> statisByParam(BizAlarmParam param);

}
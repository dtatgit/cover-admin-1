/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.mapper.statis;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cv.entity.statis.CoverCollectStatis;
import java.util.List;
import java.util.Map;
/**
 * 窨井盖采集统计MAPPER接口
 * @author crj
 * @version 2019-04-22
 */
@MyBatisMapper
public interface CoverCollectStatisMapper extends BaseMapper<CoverCollectStatis> {
    List<Map<String,Object>> collectStatis(Map<String,Object> map);
}
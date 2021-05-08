/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.mapper.statis;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cv.entity.statis.ConstructionStatistics;
import com.jeeplus.modules.cv.entity.statis.CoverStatis;
import com.jeeplus.modules.cv.entity.statis.CoverWorkStatisBo;
import com.jeeplus.modules.cv.vo.CoverStatisVO;

import java.util.List;

/**
 * 井盖相关统计MAPPER接口
 * @author crj
 * @version 2021-02-08
 */
@MyBatisMapper
public interface CoverStatisMapper extends BaseMapper<CoverStatis> {
    public List<CoverStatisVO> queryStatisData(CoverStatis param);
}
/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.mapper.work;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cv.entity.statis.ConstructionStatistics;
import com.jeeplus.modules.cv.entity.statis.CoverWorkParam;
import com.jeeplus.modules.cv.entity.statis.CoverWorkStatisBo;
import com.jeeplus.modules.cv.vo.CountVo;
import org.apache.ibatis.annotations.Param;

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

    public List<CoverWorkStatisBo> coverWorkTableStatistic(CoverWorkParam param);

    public List<CoverWorkStatisBo> coverWorkLineStatistic(CoverWorkParam param);

    public List<CoverWorkStatisBo> constructionStatis(ConstructionStatistics param);

    int countByBellIdNoComplete(@Param(value="bellId")String bellId);

    int countTotal();

    List<CountVo> lifeCycleCountSql();

    List<CountVo> workTypeCountSql();

    int countOfCompleteNoFilter(@Param(value="userId")String userId);

    int countOfCompleteByCoverIdNoFilter(@Param(value="coverId")String coverId);

    CoverWork getByCoverId(@Param(value="coverId")String coverId);

    int countByWorkType(@Param(value="workType")String workType);

    int countByWorkTypeAll(@Param(value="workType")String workType);
}
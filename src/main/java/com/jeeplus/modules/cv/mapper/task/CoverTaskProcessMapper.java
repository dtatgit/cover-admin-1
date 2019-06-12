/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.mapper.task;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cv.entity.task.CoverTaskProcess;

import java.util.List;

/**
 * 任务处理明细MAPPER接口
 * @author crj
 * @version 2019-05-16
 */
@MyBatisMapper
public interface CoverTaskProcessMapper extends BaseMapper<CoverTaskProcess> {

    public int updateForProcess(String id);

    public List<CoverTaskProcess> findListForTask(CoverTaskProcess entity);
}
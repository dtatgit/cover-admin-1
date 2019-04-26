/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.mapper.equinfo;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 井盖基础信息MAPPER接口
 * @author crj
 * @version 2019-04-19
 */
@MyBatisMapper
public interface CoverMapper extends BaseMapper<Cover> {
    /**
     * 根据实体名称和字段名称和字段值获取唯一记录
     *
     * @param propertyName
     * @param value
     * @return
     */
    public List<Cover> findCoverForAudit(@Param(value="propertyName")String propertyName, @Param(value="value")Object value);


    public int updateForAudit(String id);
}
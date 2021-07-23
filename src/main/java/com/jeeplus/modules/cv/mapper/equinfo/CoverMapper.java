/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cv.mapper.equinfo;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.vo.CountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    List<Map<String, Object>> selectBySql(String sql);

    void updateGwoById(@Param(value="id")String id, @Param(value="isGwo")String state);

    public List<Cover> checkFindList(Cover entity);

    public List<Cover> findAllCovers(Cover cover);

    void updateWorkStatus(@Param(value="id")String id, @Param(value="workStatus")String state);

    void updateStatus(@Param(value="id")String id, @Param(value="coverStatus")String status);

    int selectCountOfStatus(@Param(value="status")String status);

    int countByStatusAndUserIdNoFilter(@Param(value="status")String status, @Param(value="userId")String userId);

    int countByIsDamaged(@Param(value="isdamaged")String isdamaged);

    List<CountVo> countSql();

    void updateJcStatus(@Param(value="id")String id, @Param(value="status")String status);
}
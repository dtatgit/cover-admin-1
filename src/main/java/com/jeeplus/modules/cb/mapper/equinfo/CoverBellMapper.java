/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.cb.mapper.equinfo;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.api.pojo.DeviceSimpleParam;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 井卫设备信息MAPPER接口
 * @author crj
 * @version 2019-06-24
 */
@MyBatisMapper
public interface CoverBellMapper extends BaseMapper<CoverBell> {

    void updateByDevNo(DeviceSimpleParam deviceSimpleParam);

    int selCountByDevNo(@Param(value="devNo")String devNo);

    public CoverBell queryCoverBell(Map<String, Object> map);

    List<CoverBell> getByCoverId(@Param(value="coverId")String coverId);

    void updateState(@Param(value="id")String id, @Param(value="bellStatus")String bellStatus);

    void updateWorkStatus(@Param(value="id")String id, @Param(value="workStatus")String workStatus);

    public List<CoverBell> checkFindList(CoverBell entity);

    List<CoverBell> getByCoverIdAndStatus(@Param(value="coverId")String coverId,@Param(value="workStatus")String workStatus);
}
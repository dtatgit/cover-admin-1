package com.jeeplus.modules.cv.service.statis;

import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.cv.entity.statis.CoverCollectStatis;
import com.jeeplus.modules.cv.entity.statis.CoverWorkStatisVo;
import com.jeeplus.modules.cv.mapper.statis.CoverCollectStatisMapper;
import com.jeeplus.modules.cv.mapper.statis.CoverWorkStatisMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoverWorkStatisService extends CrudService<CoverWorkStatisMapper, CoverWorkStatisVo> {


}

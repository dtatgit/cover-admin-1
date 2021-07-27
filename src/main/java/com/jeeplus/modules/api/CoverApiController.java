package com.jeeplus.modules.api;


import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.api.vo.CoverResVo;
import com.jeeplus.modules.cb.entity.equinfo.CoverBell;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.constant.CodeConstant;
import com.jeeplus.modules.cv.entity.equinfo.Cover;
import com.jeeplus.modules.cv.service.equinfo.CoverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 目前提供给大屏的接口(不直接连硬件管理平台，在这里做个中转)
 */
@RestController
@RequestMapping("/api")
public class CoverApiController {

    private static Logger logger = LoggerFactory.getLogger(CoverApiController.class);

    @Autowired
    private CoverService coverService;
    @Autowired
    private CoverBellService coverBellService;
    @Autowired
    private CoverWorkService coverWorkService;

    @PostMapping("/cover/list")
    public AppResult coverList() {

        AppResult result = new AppResult();
        Cover cover = new Cover();
        cover.setCoverStatus(CodeConstant.COVER_STATUS.AUDIT_PASS);//只展示审核通过的数据
        List<Cover> list = coverService.findList2(cover);


        List<CoverResVo> collect = list.stream().map(item -> {

            CoverResVo coverResVo = new CoverResVo();
            coverResVo.setNo(item.getNo());
            coverResVo.setLatitude(item.getLatitude());
            coverResVo.setLongitude(item.getLongitude());

            String coverId = item.getId();

            String temp = "";
            //1.先判断工单
            //获取未完成的工单
            CoverWork coverWork = coverWorkService.getByCoverId(coverId);
            if (coverWork != null && StringUtils.isNotBlank(coverWork.getWorkType())) {
                String workType = coverWork.getWorkType();
                if (workType.equals(CodeConstant.WORK_TYPE.BIZ_ALARM)) {
                    temp = "2";
                } else if (workType.equals(CodeConstant.WORK_TYPE.MAINTAIN)) {
                    temp = "3";
                }
            }
            //2.再判断离线，如果有工单状态，那就不判断离线状态了
            if (StringUtils.isBlank(temp)) {
                //井盖下的井卫
                List<CoverBell> cbList = coverBellService.getByCoverId(coverId);
                if (cbList != null && !cbList.isEmpty()) {
                    boolean flag = true;
                    for (CoverBell coverBell : cbList) {
                        String workStatus = coverBell.getWorkStatus();
                        if (workStatus.equals(CodeConstant.BELL_WORK_STATUS.OFF)) {
                            temp = "1";
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        temp = "0";
                    }
                }
            }

            coverResVo.setStatus(temp);

            return coverResVo;

        }).collect(Collectors.toList());


        result.setData(collect);

        return result;
    }


    @RequestMapping("/device/filterAlarmDevice")
    public AppResult filterAlarmDevice(@RequestParam("list") List<String> list){
        AppResult result = new AppResult();



        return result;
    }


}

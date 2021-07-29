package com.jeeplus.modules.api;


import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.api.vo.CoverResVo;
import com.jeeplus.modules.api.vo.StatsResVo;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 目前提供给大屏的接口(不直接连硬件管理平台，在这里做个中转)
 */
@RestController
@RequestMapping("${adminPath}/coverApi")
public class CoverApiController {

    private static Logger logger = LoggerFactory.getLogger(CoverApiController.class);

    @Autowired
    private CoverService coverService;
    @Autowired
    private CoverBellService coverBellService;
    @Autowired
    private CoverWorkService coverWorkService;


    @RequestMapping("/cover/list")
    public AppResult coverList() {

        AppResult result = new AppResult();

        Cover cover = new Cover();
        cover.setCoverStatus(CodeConstant.COVER_STATUS.AUDIT_PASS);//只展示审核通过的数据
        List<Cover> listNew = coverService.findListNew(cover);

        List<CoverResVo> resultList = new ArrayList<>();

        if(listNew!=null && !listNew.isEmpty()){
            for (Cover item : listNew){

                String temp = "";
                //1.先判断工单
                //获取未完成的工单
                CoverWork coverWork = null;
                List<CoverWork> cwList = item.getCwList();
                if(!cwList.isEmpty()){
                    List<CoverWork> collect = cwList.stream().sorted(Comparator.comparing(CoverWork::getCreateDate).reversed()).collect(Collectors.toList());

                    coverWork = collect.get(0);
                }

                if (coverWork != null && StringUtils.isNotBlank(coverWork.getWorkType())) {
                    String workType = coverWork.getWorkType();
                    if (workType.equals(CodeConstant.WORK_TYPE.BIZ_ALARM)) {
                        temp = "2";
                    } else if (workType.equals(CodeConstant.WORK_TYPE.MAINTAIN)) {
                        //维护工单和安装工单都是 维护状态
                        temp = "3";
                    }else if (workType.equals(CodeConstant.WORK_TYPE.INSTALL)) {
                        //维护工单和按住工单都是 维护状态
                        temp = "3";
                    }
                }
                //2.再判断离线，如果有工单状态，那就不判断离线状态了
                if (StringUtils.isBlank(temp)) {
                    //井盖下的井卫
                    List<CoverBell> cbList = item.getCbList();
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

                CoverResVo coverResVo = new CoverResVo();
                coverResVo.setNo(item.getNo());
                coverResVo.setLatitude(item.getLatitude());
                coverResVo.setLongitude(item.getLongitude());
                coverResVo.setWgs84X(item.getWgs84X());
                coverResVo.setWgs84Y(item.getWgs84Y());
                coverResVo.setStatus(temp);

                resultList.add(coverResVo);
            }
        }

        result.setData(resultList);

        return result;
    }


    @RequestMapping("/cover/stats")
    public AppResult filterAlarmDevice(){
        AppResult result = new AppResult();

        //普查井盖数量
        int coverCount = coverService.selectCountOfStatus(CodeConstant.COVER_STATUS.AUDIT_PASS);

        //井卫数量
        int bellCount = coverBellService.coverCount();



        Cover cover = new Cover();
        cover.setCoverStatus(CodeConstant.COVER_STATUS.AUDIT_PASS);//只展示审核通过的数据
        List<Cover> listNew = coverService.findListNew(cover);

        //在线井盖
        int onCount = 0;
        //离线井盖
        int offCount = 0;

        if(listNew!=null && !listNew.isEmpty()){
            for (Cover c : listNew) {

                List<CoverBell> cbList = c.getCbList();

                if (cbList != null && !cbList.isEmpty()) {
                    boolean flag = true;
                    for (CoverBell coverBell : cbList) {
                        String workStatus = coverBell.getWorkStatus();
                        if (workStatus.equals(CodeConstant.BELL_WORK_STATUS.OFF)) {
                            offCount++;
                            flag = false;
                            break;
                        }
                    }

                    if(flag){
                        onCount++;
                    }
                }
            }
        }

        //排障工单
        int pzCount = coverWorkService.countByWorkType(CodeConstant.WORK_TYPE.BIZ_ALARM);

        //维护工单(维护和安装都 属于维护)
        int whCount = coverWorkService.countByWorkType(CodeConstant.WORK_TYPE.MAINTAIN);
        int whCount2 = coverWorkService.countByWorkType(CodeConstant.WORK_TYPE.INSTALL);

        
        //报警总计
        int pzCountAll = coverWorkService.countByWorkTypeAll(CodeConstant.WORK_TYPE.BIZ_ALARM);

        //维护总计(维护和安装都 属于维护)
        int whCountAll = coverWorkService.countByWorkTypeAll(CodeConstant.WORK_TYPE.MAINTAIN);
        int whCountAll2 = coverWorkService.countByWorkTypeAll(CodeConstant.WORK_TYPE.INSTALL);

        StatsResVo vo = new StatsResVo();
        vo.setCoverCount(coverCount);
        vo.setBellCount(bellCount);
        vo.setOnCount(onCount);
        vo.setOffCount(offCount);
        vo.setPzCount(pzCount);
        vo.setWhCount(whCount+whCount2);
        vo.setPzCountAll(pzCountAll);
        vo.setWhCountAll(whCountAll+whCountAll2);

        result.setData(vo);
        return result;
    }


}

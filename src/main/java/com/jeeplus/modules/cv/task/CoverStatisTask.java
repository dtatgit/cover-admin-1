package com.jeeplus.modules.cv.task;
import com.jeeplus.modules.cv.service.statis.CoverStatisService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;
import com.jeeplus.modules.monitor.entity.Task;

import java.util.Date;

/**
 * 大兴井盖数据统计任务
 */
@DisallowConcurrentExecution
public class CoverStatisTask  extends Task {


    Logger logger = Logger.getLogger(CoverTaskDataJob.class);

    @Autowired
    private CoverStatisService coverStatisService;
    @Override
    public void run() {

        logger.info("================大兴井盖数据统计任务定时任务开始执行===================");
        logger.info("大兴井盖数据统计任务开始时间：" + new Date());
        try{
            coverStatisService.statisCoverNew();//优化之后的统计
            //coverStatisService.statisCover();
            //coverStatisService.deleteCoverStatis();
        }catch(Exception e){
            e.printStackTrace();
            logger.info("================大兴井盖数据统计任务异常==================="+e.getMessage());
        }

         logger.info("大兴井盖数据统计任务结束时间：" + new Date());
        logger.info("================大兴井盖数据统计任务执行结束===================");

    }

}

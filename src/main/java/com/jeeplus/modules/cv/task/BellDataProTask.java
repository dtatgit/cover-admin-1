package com.jeeplus.modules.cv.task;

import com.jeeplus.modules.cb.service.equinfo.CoverBellService;
import com.jeeplus.modules.monitor.entity.Task;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@DisallowConcurrentExecution
public class BellDataProTask  extends Task {

    Logger logger = Logger.getLogger(BellDataProTask.class);
    @Autowired
    CoverBellService coverBellService;
    @Override
    public void run() {
        logger.info("================井卫状态数据同步定时任务开始执行===================");
        logger.info("井卫状态数据同步定时任务开始时间：" + new Date());
        try{
            coverBellService.synBellState(null);
        }catch(Exception e){
            logger.info("************井卫状态数据同步异常***************************"+e.getMessage());
        }


        logger.info("井卫状态数据同步定时任务结束时间：" + new Date());
        logger.info("================井卫状态数据同步定时任务执行结束===================");

    }
}

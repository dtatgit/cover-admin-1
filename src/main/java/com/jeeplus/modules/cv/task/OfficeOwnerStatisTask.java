package com.jeeplus.modules.cv.task;
import com.jeeplus.modules.cv.service.statis.OfficeOwnerStatisService;
import com.jeeplus.modules.monitor.entity.Task;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@DisallowConcurrentExecution
public class OfficeOwnerStatisTask extends Task {

    Logger logger = Logger.getLogger(WorkOverTimeTask.class);
    @Autowired
    private OfficeOwnerStatisService officeOwnerStatisService;
    @Override
    public void run() {

        logger.info("================工单排名定时任务开始执行===================");
        logger.info("工单排名定时任务开始时间：" + new Date());
        officeOwnerStatisService.statisWorkRanking();
        logger.info("工单排名定时任务结束时间：" + new Date());

        logger.info("================工单排名定时任务执行结束===================");

    }

}

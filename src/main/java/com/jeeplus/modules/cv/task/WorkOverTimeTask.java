package com.jeeplus.modules.cv.task;

import com.jeeplus.modules.cb.service.work.CoverWorkOvertimeService;
import com.jeeplus.modules.monitor.entity.Task;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@DisallowConcurrentExecution
public class WorkOverTimeTask  extends Task {
    Logger logger = Logger.getLogger(WorkOverTimeTask.class);
    @Autowired
    private CoverWorkOvertimeService coverWorkOvertimeService;
    @Override
    public void run() {


        logger.info("================超时工单定时任务开始执行===================");
        logger.info("超时工单定时任务开始时间：" + new Date());

        coverWorkOvertimeService.workOverTimeTask();


        logger.info("超时工单定时任务结束时间：" + new Date());


        logger.info("================超时工单定时任务执行结束===================");

    }

}

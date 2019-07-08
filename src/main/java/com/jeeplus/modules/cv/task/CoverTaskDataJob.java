package com.jeeplus.modules.cv.task;

import com.jeeplus.modules.cv.service.task.CoverTaskInfoService;
import com.jeeplus.modules.monitor.entity.Task;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@DisallowConcurrentExecution
public class CoverTaskDataJob  extends Task {

    Logger logger = Logger.getLogger(CoverTaskDataJob.class);
    @Autowired
    private CoverTaskInfoService coverTaskInfoService;
    @Override
    public void run() {
        //对已经确权的井盖，仍然存在未完成的任务明细数据进行清洗

        logger.info("================确权井盖任务明细数据清洗定时任务开始执行===================");
        logger.info("确权井盖任务明细数据清洗开始时间：" + new Date());

        coverTaskInfoService.coverTaskDataClear();


        logger.info("确权井盖任务明细数据清洗结束时间：" + new Date());





        logger.info("================确权井盖任务明细数据清洗定时任务执行结束===================");

    }

}

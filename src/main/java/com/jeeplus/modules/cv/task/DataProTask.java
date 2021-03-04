package com.jeeplus.modules.cv.task;

import com.jeeplus.modules.cb.service.work.CoverWorkService;
import com.jeeplus.modules.cv.service.statis.CoverStatisService;
import com.jeeplus.modules.cv.service.task.CoverTaskInfoService;
import com.jeeplus.modules.monitor.entity.Task;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@DisallowConcurrentExecution
public class DataProTask  extends Task {

    Logger logger = Logger.getLogger(CoverTaskDataJob.class);
    @Autowired
    private CoverWorkService coverWorkService;
    @Autowired
    private CoverStatisService coverStatisService;
    @Override
    public void run() {
        logger.info("================数据处理定时任务开始执行===================");
        logger.info("数据处理定时任务开始时间：" + new Date());
       // String createDate=coverWorkService.queryMinCreateDate();//获取工单最早的创建时间
        coverStatisService.deleteCoverStatis();
        logger.info("数据处理定时任务结束时间：" + new Date());





        logger.info("================数据处理定时任务定时任务执行结束===================");

    }
}

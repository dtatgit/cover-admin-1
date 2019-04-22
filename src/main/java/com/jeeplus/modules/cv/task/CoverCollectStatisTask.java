package com.jeeplus.modules.cv.task;

import com.jeeplus.modules.cv.service.statis.CoverCollectStatisService;
import com.jeeplus.modules.monitor.entity.Task;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
import java.text.SimpleDateFormat;

@DisallowConcurrentExecution
public class CoverCollectStatisTask extends Task {
    @Autowired
    private CoverCollectStatisService coverCollectStatisService;

    Logger logger = Logger.getLogger(CoverCollectStatisTask.class);

    @Override
    public void run() {
        //统计昨天昨天的井盖采集信息
        Date date = new Date();
        logger.info("================井盖采集信息统计定时任务开始执行===================");
        logger.info("执行开始时间：" + new Date());
        //获取昨天时间
        Date backupTime= DateUtils.addDays(date, -1);
        SimpleDateFormat sdfBegin = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("yyyy-MM-dd 23:59:59");

        logger.info("营收统计时间为开始时间为：" +sdfBegin.format(backupTime));
        logger.info("营收统计时间为结束时间为：" +sdfEnd.format(backupTime));
        coverCollectStatisService.collectStatisTask(sdfBegin.format(backupTime),sdfEnd.format(backupTime));

        logger.info("执行结束时间：" + new Date());





        logger.info("================井盖采集信息统计定时任务执行结束===================");

    }
}

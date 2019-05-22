package com.jeeplus.modules.cv.task;

import com.jeeplus.modules.monitor.entity.Task;
import com.jeeplus.modules.sys.entity.DictType;
import com.jeeplus.modules.sys.service.DictTypeService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;

/**
 * 权属单位字典项维护
 */
@DisallowConcurrentExecution
public class CoverOwnerDictTask  extends Task {
    @Autowired
    private DictTypeService dictTypeService;
    Logger logger = Logger.getLogger(CoverOwnerDictTask.class);
    @Override
    public void run() {
        //统计昨天昨天的井盖采集信息

        logger.info("================权属单位字典项维护定时任务开始执行===================");
        logger.info("权属单位字典项维护执行开始时间：" + new Date());
/*        DictType dictType=new DictType();
        dictType.setType("cover_owner_depart");
        List<DictType> ownerList= dictTypeService.findList(dictType);
        if(null!=ownerList&&ownerList.size()>0){*/
            dictTypeService.coverOwnerHandle();
       // }



        logger.info("权属单位字典项维护执行结束时间：" + new Date());





        logger.info("================井盖采集信息统计定时任务执行结束===================");

    }

}

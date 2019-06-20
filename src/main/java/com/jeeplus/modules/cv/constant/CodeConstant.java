package com.jeeplus.modules.cv.constant;

/**
 * 字典项常量类
 */
public interface CodeConstant {


    /**井盖记录状态*/
    interface COVER_STATUS {
        String INIT = "init"; //初始化
        String WAIT_AUDIT = "wait_audit";  //待审核
        String DELETEING = "deleteing";  //待删除
        String AUDIT_PASS = "audit_pass";  //审核通过
        String AUDIT_FAIL = "audit_fail";  //审核失败
    }

    /**井盖审核申请-申请事项*/
    interface APPLY_ITEM {
        String ADD = "add"; //新增
        String DELETE = "delete";  //删除
    }

    /**井盖审核-申请状态*/
    interface AUDIT_STATUS {
        String AUDITING = "auditing"; //审核中
        String AUDIT_PASS = "audit_pass";  //审核通过
        String AUDIT_FAIL = "audit_fail";  //审核失败
    }

    /**井盖-损坏形式cover_damage*/
    interface COVER_DAMAGE {
        String GOOD = "0"; //完好0
        String DEFECT = "1";  //井盖缺失1
        String DESTROY = "2";  //井盖破坏2
        String RIFT = "3";  //井周沉降、龟裂3
        String OWNER = "4";  //井筒本身破坏4
        String OTHER = "9";  //其他9
    }

    /**井盖-任务状态task_status*/
    interface TASK_STATUS {
        String ASSIGN = "assign"; //已分配
        String PROCESSING = "processing";  //处理中
        String COMPLETE = "complete";  //已完成
        String CANCEL = "cancel";  //已关闭

    }
    /**布尔值boolean*/
    interface BOOLEAN {
        String YES = "Y"; //是
        String NO = "N";  //否
    }

    /**历史记录来源source*/
    interface SOURCE {
        String REPAIR = "repair"; //信息修复
        String TASK = "task";  //任务处理
    }

}

package com.jeeplus.modules.cv.constant;

/**
 * 字典项常量类
 */
public interface CodeConstant {


    /**
     * 井盖记录状态
     */
    interface COVER_STATUS {
        String INIT = "init"; //初始化
        String WAIT_AUDIT = "wait_audit";  //待审核
        String DELETEING = "deleteing";  //待删除
        String AUDIT_PASS = "audit_pass";  //审核通过
        String AUDIT_FAIL = "audit_fail";  //审核失败
    }

    /**
     * 井盖审核申请-申请事项
     */
    interface APPLY_ITEM {
        String ADD = "add"; //新增
        String DELETE = "delete";  //删除
    }

    /**
     * 井盖审核-申请状态
     */
    interface AUDIT_STATUS {
        String AUDITING = "auditing"; //审核中
        String AUDIT_PASS = "audit_pass";  //审核通过
        String AUDIT_FAIL = "audit_fail";  //审核失败
    }

    /**
     * 井盖-损坏形式cover_damage
     */
    interface COVER_DAMAGE {
        String GOOD = "0"; //完好0
        String DEFECT = "1";  //井盖缺失1
        String DESTROY = "2";  //井盖破坏2
        String RIFT = "3";  //井周沉降、龟裂3
        String OWNER = "4";  //井筒本身破坏4
        String OTHER = "9";  //其他9
    }

    /**
     * 井盖-任务状态task_status
     */
    interface TASK_STATUS {
        String ASSIGN = "assign"; //已分配
        String PROCESSING = "processing";  //处理中
        String COMPLETE = "complete";  //已完成
        String CANCEL = "cancel";  //已关闭

    }

    /**
     * 布尔值boolean
     */
    interface BOOLEAN {
        String YES = "Y"; //是
        String NO = "N";  //否
    }

    /**
     * 历史记录来源source
     */
    interface SOURCE {
        String REPAIR = "repair"; //信息修复
        String TASK = "task";  //任务处理
    }


    /**井铃字典项：引入工作流之前的 工单状态：work_status*/
/*    interface WORK_STATUS {
        String INIT="init";//初始化
        //String WAIT_ASSIGN = "wait_assign"; //待派单
        //String WAIT_RECEIVE = "wait_receive"; //已指派(原为：待接收)
        String ASSIGN = "assign"; //已指派(原为：待接收)
        String PROCESSING = "processing";  //处理中
        String PROCESS_COMPLETE = "process_complete";  //处理完成
        String PROCESS_FAIL = "process_fail";  //处理失败
        String COMPLETE = "complete";  //结束（原为：完成）
        String SCRAP = "scrap"; //废弃
    }*/

    /**
     * 井铃字典项：引入工作流之后的 工单状态：work_status
     */
    interface WORK_STATUS {
        String INIT = "S0";//初始化
        //String WAIT_ASSIGN = "wait_assign"; //待派单
        //String WAIT_RECEIVE = "wait_receive"; //已指派(原为：待接收)
        String ASSIGN = "S12"; //已指派(原为：待接收)
      /*  String PROCESSING = "processing";  //处理中
        String PROCESS_COMPLETE = "process_complete";  //处理完成
        String PROCESS_FAIL = "process_fail";  //处理失败
        String COMPLETE = "complete";  //结束（原为：完成）
        String SCRAP = "scrap"; //废弃*/
    }

    /**
     * 井铃字典项： 工单类型：work_type
     */
    interface WORK_TYPE {
        String CHECK = "check"; //巡检工单
        String SWITCH_LOCK = "switch_lock";  //开关锁工单
        String ALARM = "alarm";  //报警工单
        String INSTALL = "install";  //安装工单
        String BIZ_ALARM = "biz_alarm"; //业务报警
        String SPOT_CHECK = "spot_check"; //抽检工单
        String MAINTAIN = "maintain"; //维护工单
        String EXCEPTION_REPORT = "exception_report"; //异常上报工单
    }

    /**
     * 井铃字典项： 工单操作类型：work_operation_Type
     */
    interface WORK_OPERATION_TYPE {
        String CREATE = "create"; //生成
        String ASSIGN = "assign"; //指派
        String RECEIVE = "receive";  //接收
        String HANDLE = "handle";  //处理
        String AUDIT = "audit";  //审核

    }

    /**
     * 井铃字典项： 工单操作状态： work_operation_status
     */
    interface WORK_OPERATION_STATUS {
        String SUCCESS = "success"; //成功
        String FAIL = "fail"; //失败
        String AUDIT_PASS = "audit_pass";  //审核通过
        String AUDIT_FAIL = "audit_fail";  //审核失败
    }


    /**
     * 井铃字典项： 设防类型：defense_status
     */
    interface DEFENSE_STATUS {
        String FORTIFY = "fortify"; //设防
        String REVOKE = "revoke"; //撤防

    }

    /**
     * 井铃字典项： 井铃工作状态： bell_work_status
     */
    interface BELL_WORK_STATUS {
        String OFF = "off"; //离线
        String ON = "on"; //在线

    }

    /**
     * 井铃字典项： 井铃生命周期：  bell_status
     */
    interface BELL_STATUS {
        String init = "init"; //初始化
        String in = "in"; //入库
        String out = "out"; //出库
        String install = "install"; //安装
        String remove = "remove"; //拆卸
        String factory = "factory"; //返厂
        String scrap = "scrap"; //报废


    }

    /**
     * 井铃字典项： 井铃操作类型：operation_type
     */
    interface operation_type {

        String in = "in"; //入库
        String out = "out"; //出库
        String install = "install"; //安装
        String remove = "remove"; //拆卸
        String factory = "factory"; //返厂
        String scrap = "scrap"; //报废
        String fortify = "fortify"; //设防
        String revoke = "revoke"; //撤防
    }

    /**
     * 井铃字典项： 工单明细类型： record_type
     */
    interface record_type {

        String install = "install"; //安装
        String repair = "repair"; //维护

    }

    /**
     * 井盖安装工单状态
     */
    interface cover_gwo {
        String install = "Y"; //已安装
        String not_install = "N";  //未安装
        String handle = "Z";  //安装中
    }

    /**
     * 工单紧急程度
     */
    interface work_level {
        String normal = "normal"; //常规
        String urgent = "urgent";  //紧急
        String extra = "extra";  //特急
    }

    /**
     * 井盖类型
     */
    interface cover_type {
        String normal = "普通井盖"; //普通井盖
        String smart = "智能化井盖";  //智能化井盖
        String unify = "一体化井盖";  //一体化井盖
    }

    /**
     * 工单生命周期
     */
    interface lifecycle {
        String init = "init"; //初始化
        String processing = "processing";  //处理中
        String hangup = "hangup";  //挂起
        String complete= "complete";  //已结束
        String discard= "discard";  //废弃
    }

    interface GUARD_TOPIC {
        final String ALARM = "/guard/alarm";
        final String BIZ_ALARM = "/guard/bizAlarm";
        final String ONLINE = "/guard/online";
    }

    interface exceptionReportCheckStatus {
        String toCheck = "0"; //待审批
        String pass = "1"; //已通过
        String notPass = "2"; //未通过
        String discard = "3"; //废弃
    }
    //用户来源
    interface user_source {
        String SYS = "sys"; //系统用户
        String AUTH = "auth"; //认证用户

    }

}
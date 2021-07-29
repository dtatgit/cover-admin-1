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


    /**井卫字典项：引入工作流之前的 工单状态：work_status*/
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
     * 井卫字典项：引入工作流之后的 工单状态：work_status
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
     * 井卫字典项： 工单类型：work_type
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
     * 井卫字典项： 工单操作类型：work_operation_Type
     */
    interface WORK_OPERATION_TYPE {
        String CREATE = "create"; //生成
        String ASSIGN = "assign"; //指派
        String RECEIVE = "receive";  //接收
        String HANDLE = "handle";  //处理
        String AUDIT = "audit";  //审核

    }

    /**
     * 井卫字典项： 工单操作状态： work_operation_status
     */
    interface WORK_OPERATION_STATUS {
        String SUCCESS = "success"; //成功
        String FAIL = "fail"; //失败
        String AUDIT_PASS = "audit_pass";  //审核通过
        String AUDIT_FAIL = "audit_fail";  //审核失败
    }


    /**
     * 井卫字典项： 设防类型：defense_status
     */
    interface DEFENSE_STATUS {
        String FORTIFY = "fortify"; //设防
        String REVOKE = "revoke"; //撤防

    }

    /**
     * 井卫字典项： 井卫工作状态： bell_work_status
     */
    interface BELL_WORK_STATUS {
        String OFF = "off"; //离线
        String ON = "on"; //在线
        String INIT = "init"; //初始状态

    }

    /**
     * 井卫字典项： 井卫生命周期：  bell_status   未注册、未安装、已安装、报废。
     */
    interface BELL_STATUS {
        String init = "init"; //初始化
        String in = "in"; //入库
        String out = "out"; //出库
        String unregistered = "unregistered";  //未注册
        String notinstalled = "notinstalled";  //未安装
        String installed = "installed"; //已安装
        String remove = "remove"; //拆卸
        String factory = "factory"; //返厂
        String scrap = "scrap"; //报废


    }

    /**
     * 井卫字典项： 井卫操作类型：operation_type
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
     * 井卫字典项： 工单明细类型： record_type
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
        String init = "init"; //初始化(未接单)
        String processing = "processing";  //进行中
        String hangup = "hangup";  //挂起
        String complete= "complete";  //已完成
        String discard= "discard";  //废弃
        String waitAudit = "waitAudit"; //待审核
        String cancel = "cancel"; //作废
        String expire = "expire"; //过期
        String refuse = "refuse"; //拒绝
    }


    /**
     * 工单生命周期     此版本为最新的版本，现在使用这版本,对应数据字典：lifecycle    2021-7-1
     */
    interface WorkLifecycle {
        String notAssign = "notAssign"; //未接单
        String processing = "processing";  //进行中
        String waitAudit = "waitAudit"; //待审核
        String complete= "complete";  //已完成
        String refuse = "refuse"; //已驳回
        String expire = "expire"; //已超期
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

    interface COVER_DATA_SOURCE {  //井盖数据来源
        final String GATHER = "gather"; //采集
        final String TEST = "test"; //测试
    }

    //消息推送方式
    interface push_mode {
        String message = "message"; //短信推送
        String mobile = "mobile"; //移动端推送

    }

    /**
     * 启用禁用on_off,1启用，0禁用
     */
    interface on_off {
        String one = "1"; //启用
        String zero = "0";  //禁用
    }
    /**
     * 组织机构类型
     */
    interface sys_office_type {
        String company = "1"; //公司
        String agent = "2";  //代理商
        String customer = "3";  //子域客户
        String department = "4";  //部门
    }


}
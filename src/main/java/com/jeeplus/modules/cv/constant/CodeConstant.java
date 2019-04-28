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
}

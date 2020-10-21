package com.jeeplus.modules.cb.constant.coverWork;

/**
 * 工单状态
 * @author crj
 * @date 2019-08-13
 */
public enum WorkStatus {
    /**
     * 初始化
     */
    init("init", "初始化"),
    /**
     * 已指派，已经被部门或者个人所接收
     * wait_receive("wait_receive", "已指派"),
     */
    assign("assign", "已指派"),

    /**
     * 处理中，进入工单处理环节
     */
    processing("processing", "处理中"),
    /**
     * 处理完成，待审核
     */
    process_complete("process_complete", "处理完成"),
    /**
     * 处理失败，待审核
     */
    process_fail("process_fail", "处理失败"),
    /**
     * 工单结束
     */
    complete("process_complete", "结束"),


    hang_up("hang_up","挂起"),
    /**
     * 工单废弃
     */
    scrap("scrap", "废弃");

    public final String code;
    public final String title;

    WorkStatus(String code, String title) {
        this.code = code;
        this.title = title;
    }
}

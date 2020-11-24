package com.jeeplus.modules.cb.constant.coverWork;

public interface WorkConstant {
    public interface WorkStatus {
        String INIT = "init"; //初始化
        String ASSIGN = "assign"; //已指派
        String PROCESSING = "processing"; //处理中
        String PROCESS_COMPLETE = "process_complete"; //处理完成
        String PROCESS_FAIL = "process_fail"; //处理失败
        String COMPLETE = "process_complete"; //结束
        String HANG_UP = "hang_up"; //挂起
        String SCRAP = "scrap"; //废弃
    }


}

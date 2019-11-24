package com.jeeplus.modules.message;

import com.antu.common.context.Context;
import com.antu.common.utils.CommonUtils;
import com.antu.message.Message;
import com.antu.message.dispatch.annotation.*;
import com.antu.mq.core.MQClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeplus.modules.cb.entity.work.CoverWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 工单工作流消息监听
 *
 * @author rushman
 * @date 2019-11-24
 */
@MessageController
public class WorkflowMessageController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper;

    public WorkflowMessageController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 解析监听的工单信息
     *
     * @param context 消息上下文
     * @param bytes JSON字节数组
     * @return {@code Message<CoverWork>} 工单信息
     * @throws IOException JSON解析错误
     */
    @Subscribe(channel = "/workflow/{cmd}", type = byte[].class)
    public Message<?> decode(
            @Param("cmd") String cmd,
            @MessageContext Context context,
            @MessageContent byte[] bytes
    ) throws IOException {
        logger.debug("## receive message: {}", CommonUtils.bytes2hex(bytes));
        CoverWork coverWork = objectMapper.readValue(bytes, CoverWork.class);
        return Message.of("/" + cmd, Message.of(coverWork, context));
    }

    @Subscribe(channel = "/workflow/create", type=CoverWork.class)
    public byte[] onCreate(CoverWork coverWork) throws JsonProcessingException {
        logger.debug("## create Cover-Work: {}", coverWork);
        return objectMapper.writeValueAsBytes(coverWork);
    }
}

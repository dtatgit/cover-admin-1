package com.jeeplus.modules.message;

import com.antu.common.context.Context;
import com.antu.common.utils.CommonUtils;
import com.antu.message.Message;
import com.antu.message.dispatch.annotation.*;
import com.antu.mq.core.AsyncMQClient;
import com.antu.mq.core.SimpleMQMessage;
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
@MessageProcessor
public class WorkflowMessageController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ObjectMapper objectMapper;
    private final AsyncMQClient mqClient;
    private final MessageTopicMapper messageTopicMapper;

    public WorkflowMessageController(AsyncMQClient mqClient, MessageTopicMapper messageTopicMapper, ObjectMapper objectMapper) {
        this.mqClient = mqClient;
        this.messageTopicMapper = messageTopicMapper;
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
            String topic,
            @Param("cmd") String cmd,
            @MessageContext Context context,
            @MessageContent byte[] bytes
    ) throws IOException {
        logger.debug("## receive message: [{}] {}", cmd, new String(bytes));
        WorkBillVo coverWork = objectMapper.readValue(bytes, WorkBillVo.class);
        return Message.of(topic, coverWork, context);
    }

    /**
     * 当创建工单时，通知其他应用
     *
     * @param context 消息上下文（如果存在）
     * @param topic 消息主题
     * @param coverWork 工单数据
     * @throws JsonProcessingException JSON转换异常
     */
    @Subscribe(channel = "/workflow/create", type=CoverWork.class)
    public void onCreate(Context context, String topic, CoverWork coverWork) throws JsonProcessingException {
        WorkBillVo bill = WorkBillVo.of(coverWork);
        logger.debug("## create Cover-Work: {}", bill);
        String json = objectMapper.writeValueAsString(bill);
        byte[] bytes = json.getBytes();
        String publishTopic = messageTopicMapper.toExternal(topic);
        logger.debug("## dispatch message: [{}] {}", publishTopic, CommonUtils.bytes2hex(bytes));
        mqClient.publish(publishTopic, new SimpleMQMessage(publishTopic, null, bytes));
    }

    /**
     * 当工单操作发生后，执行相应的业务处理
     *
     * @param context 消息上下文
     * @param bill 接收的工单信息
     */
    @Subscribe(channel = "/workflow/exec")
    public void afterExec(Context context, @MessageContent WorkBillVo bill) {
        // TODO 执行相应的业务处理
    }
}

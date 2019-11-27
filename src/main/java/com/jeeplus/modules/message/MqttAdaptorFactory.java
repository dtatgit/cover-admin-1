package com.jeeplus.modules.message;

import com.antu.message.dispatch.MessageDispatcher;
import com.antu.mq.core.MQEvent;
import com.antu.mq.mqtt.AsyncMqttAdapter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Mqtt适配器工厂
 *
 * @author rushman
 * @date 2019-11-24
 */
@Component
public class MqttAdaptorFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${mq.client.id}")
    private String clientId;
    @Value("${mq.mqtt.url}")
    private String serverUrl;
    @Value("${mq.connection.timeout:1200000}")
    private int timeout;
    @Value("${mq.topic.prefix}")
    private String topicPrefix;
    @Value("${message.dispatch.prefix}")
    private String localPrefix;

    private final MessageDispatcher messageDispatcher;
    private final MessageTopicMapper messageTopicMapper;

    public MqttAdaptorFactory(MessageDispatcher messageDispatcher, MessageTopicMapper messageTopicMapper) {
        this.messageDispatcher = messageDispatcher;
        this.messageTopicMapper = messageTopicMapper;
    }

    public AsyncMqttAdapter createAdapter() {
        logger.debug("Create MQClient adapter: {} -- {}", clientId, serverUrl);
        AsyncMqttAdapter adapter = new AsyncMqttAdapter(clientId + "-" + new Date().getTime(), serverUrl);

        MqttConnectOptions options = new MqttConnectOptions();
//        options.setUserName(user);
//        options.setPassword(password);
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setKeepAliveInterval(timeout);
        String willTopic = StringUtils.join(topicPrefix, "/manage/disconnected");
        options.setWill(willTopic, ("{\"message\":\"MQ Client disconnected\",\"clientId\":\"" + clientId + "\"}").getBytes(), 1, false);
        adapter.setOptions(options);

        adapter.addEventListener(MQEvent.connect, context -> logger.debug("#### Connect to MQ server {}", serverUrl));
        adapter.addEventListener(MQEvent.connection_lost, context -> logger.warn("#### Lost MQ connection {}", serverUrl));
        adapter.addEventListener(MQEvent.connect_done, context -> logger.debug("#### Connected to MQ server {}", serverUrl));
        adapter.addEventListener(MQEvent.connect_fail, context -> logger.error("#### Connect to MQ server failed " + serverUrl, context.getException()));

        // 自动重连
        adapter.addEventListener(MQEvent.connection_lost, c -> {
            if (!adapter.isConnected()) {
                logger.debug("#### reconnect to MQ server {}", serverUrl);
                adapter.connect();
            }
        });

        // 消息转发至messageDispatcher
        adapter.subscribe(StringUtils.join(topicPrefix, "/#"));
        adapter.addListener(topicPrefix, (topic, message) -> messageDispatcher.publish(messageTopicMapper.toLocal(topic), message));

        adapter.connect();

        return adapter;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public String getLocalPrefix() {
        return localPrefix;
    }

    public void setLocalPrefix(String localPrefix) {
        this.localPrefix = localPrefix;
    }
}

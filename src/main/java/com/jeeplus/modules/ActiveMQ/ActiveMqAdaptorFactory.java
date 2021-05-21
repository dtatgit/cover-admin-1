package com.jeeplus.modules.ActiveMQ;

import com.antu.message.dispatch.MessageDispatcher;
import com.antu.mq.activemq.ActivemqAdapter;
import com.antu.mq.activemq.ActivemqConfig;
import com.antu.mq.core.MQEvent;
import com.antu.mq.mqtt.AsyncMqttAdapter;
import com.jeeplus.modules.message.MessageTopicMapper;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * ActiveMq适配器工厂（统一门户认证用户）
 */

public class ActiveMqAdaptorFactory {

    private static final Logger logger = LoggerFactory.getLogger(ActiveMqAdaptorFactory.class);

    public static ActivemqAdapter Mqtt(String serverUrl , String username, String password,String subscribeTopic){
        ActivemqConfig config=new ActivemqConfig();
        config.setUrl(serverUrl);
        config.setUser(username);
        config.setPassword(password);
        ActivemqAdapter adapter= ActivemqAdapter.of(config);

        adapter.addEventListener(MQEvent.connect, context -> logger.debug("#### Connect to activemq (internal) server {}", serverUrl));
        adapter.addEventListener(MQEvent.connection_lost, context -> logger.warn("#### Lost activemq (internal) connection {}", serverUrl));
        adapter.addEventListener(MQEvent.connect_done, context -> logger.debug("#### Connected to activemq (internal) server {}", serverUrl));
        adapter.addEventListener(MQEvent.connect_fail, context -> logger.error("#### Connect to activemq (internal) server failed " + serverUrl, context.getException()));

        try{
            adapter.connect();
            adapter.subscribe(subscribeTopic);
        }catch (Exception e){
            logger.error("#### 统一门户（互联网区）消息监听异常", e);
        }
        return adapter;
    }

}

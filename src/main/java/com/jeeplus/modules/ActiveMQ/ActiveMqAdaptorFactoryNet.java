package com.jeeplus.modules.ActiveMQ;

import com.antu.mq.activemq.ActivemqAdapter;
import com.antu.mq.activemq.ActivemqConfig;
import com.antu.mq.core.MQEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ActiveMq适配器工厂(互联网认证用户)
 */
public class ActiveMqAdaptorFactoryNet {


    private static final Logger logger = LoggerFactory.getLogger(ActiveMqAdaptorFactoryNet.class);

    public static ActivemqAdapter Mqtt(String serverUrl , String username, String password, String subscribeTopic){
        ActivemqConfig config=new ActivemqConfig();
        config.setUrl(serverUrl);
        config.setUser(username);
        config.setPassword(password);
        ActivemqAdapter adapter= ActivemqAdapter.of(config);

        adapter.addEventListener(MQEvent.connect, context -> logger.debug("#### Connect to activemq (external) server {}", serverUrl));
        adapter.addEventListener(MQEvent.connection_lost, context -> logger.warn("#### Lost activemq (external) connection {}", serverUrl));
        adapter.addEventListener(MQEvent.connect_done, context -> logger.debug("#### Connected to activemq (external) server {}", serverUrl));
        adapter.addEventListener(MQEvent.connect_fail, context -> logger.error("#### Connect to activemq (external) server failed " + serverUrl, context.getException()));

        try{
            adapter.connect();
            adapter.subscribe(subscribeTopic);
        }catch (Exception e){
            logger.error("#### 统一门户（政务外网）消息监听异常", e);
        }
        return adapter;
    }
}

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
 * ActiveMq适配器工厂
 */

public class ActiveMqAdaptorFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static ActivemqAdapter Mqtt(String serverUrl , String username, String password,String subscribeTopic){
        ActivemqConfig config=new ActivemqConfig();
        config.setUrl(serverUrl);
        config.setUser(username);
        config.setPassword(password);
        ActivemqAdapter adapter= ActivemqAdapter.of(config);
        try{
            adapter.connect();
            adapter.subscribe(subscribeTopic);
        }catch (Exception e){
            e.printStackTrace();
        }
        return adapter;
    }

}

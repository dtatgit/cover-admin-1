package com.jeeplus.modules.ActiveMQ;

import com.antu.mq.activemq.ActivemqAdapter;
import com.antu.mq.activemq.ActivemqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ActiveMq适配器工厂(互联网认证用户)
 */
public class ActiveMqAdaptorFactoryNet {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static ActivemqAdapter Mqtt(String serverUrl , String username, String password, String subscribeTopic){
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

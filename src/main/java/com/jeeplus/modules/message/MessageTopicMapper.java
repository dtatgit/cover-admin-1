package com.jeeplus.modules.message;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消息主题映射
 *
 * @author rushman
 * @date 2019-11-24
 */
@Component
public class MessageTopicMapper {
    private final String externalPrefix;
    private final String localPrefix;

    public MessageTopicMapper(
            @Value("${mq.topic.prefix}") String externalPrefix,
            @Value("${message.dispatch.prefix}") String localPrefix
    ) {
        this.externalPrefix = externalPrefix;
        this.localPrefix = localPrefix;
    }

    public String toLocal(String topic) {
        return StringUtils.startsWith(topic, this.externalPrefix) ? this.localPrefix + "/" + topic.substring(this.externalPrefix.length()) : null;
    }

    public String toExternal(String topic) {
        return StringUtils.startsWith(topic, this.localPrefix) ? this.externalPrefix + "/" + topic.substring(this.localPrefix.length()) : null;
    }
}

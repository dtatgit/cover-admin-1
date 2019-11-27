package com.jeeplus.modules.message;

import org.apache.commons.lang3.StringUtils;

/**
 * 消息主题映射
 *
 * @author rushman
 * @date 2019-11-24
 */
public class MessageTopicMapper {
    private final String externalPrefix;
    private final String localPrefix;

    public MessageTopicMapper(String externalPrefix, String localPrefix) {
        this.externalPrefix = externalPrefix;
        this.localPrefix = localPrefix;
    }

    public String toLocal(String topic) {
        return StringUtils.startsWith(topic, this.externalPrefix) ? this.localPrefix + "/" + topic.substring(this.externalPrefix.length()).replaceFirst("^/", "") : null;
    }

    public String toExternal(String topic) {
        return StringUtils.startsWith(topic, this.localPrefix) ? this.externalPrefix + "/" + topic.substring(this.localPrefix.length()).replaceFirst("^/", "") : null;
    }

    public String getExternalPrefix() {
        return externalPrefix;
    }

    public String getLocalPrefix() {
        return localPrefix;
    }
}

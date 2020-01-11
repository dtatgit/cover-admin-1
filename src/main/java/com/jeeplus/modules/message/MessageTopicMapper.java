package com.jeeplus.modules.message;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 消息主题映射
 *
 * @author rushman
 * @date 2019-11-24
 *
 * @history 2020-01-11 rushman -- rebuild, extend mapping rules
 */
public class MessageTopicMapper {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<UnaryOperator<String>> inboundRules;
    private final List<UnaryOperator<String>> outboundRules;

    public MessageTopicMapper() {
        this.inboundRules = new ArrayList<>();
        this.outboundRules = new ArrayList<>();
    }

    public MessageTopicMapper(String declare) {
        this();
        this.register(declare);
    }

    public MessageTopicMapper(String externalPrefix, String localPrefix) {
        this();
        this.register(externalPrefix, localPrefix);
    }

    public MessageTopicMapper registerInbound(Predicate<String> filter, UnaryOperator<String> mapper) {
        this.inboundRules.add((topic) -> (filter.test(topic)) ? mapper.apply(topic) : null);
        return this;
    }

    public MessageTopicMapper registerOutbound(Predicate<String> filter, UnaryOperator<String> mapper) {
        this.outboundRules.add((topic) -> (filter.test(topic)) ? mapper.apply(topic) : null);
        return this;
    }

    /**
     * register Mapping Rules, with specified declare string.
     * <pre>
     * format: {@code <externalPrefix>,<localPrefix>[;<externalPrefix>,<localPrefix>]...}
     * </pre>
     *
     * @param declare mapping rule declare
     * @return current topic mapper
     */
    public MessageTopicMapper register(String declare) {
        logger.debug("register mapping rules: {}", declare);
        String[] rules = declare.split("\\s*;\\s*");
        for (String rule : rules) {
            String[] pair = rule.split("\\s*,\\s*", 2);
            if (pair.length < 2) {
                logger.warn("invalid mapping rule: '{}', DISCARD.", rule);
                continue;
            }
            this.register(pair[0], pair[1]);
        }
        return this;
    }

    /**
     * register Mapping rule, with external/local pair(s)
     * <pre>
     * external/local pair(s) format: {@code <prefix>[,<prefix>]...}
     * </pre>
     * <p>
     * NOTE: external/local pair(s) <em>NEED</em> same size, otherwise some config will <em>DISCARD</em>.
     * </p>
     *
     * @param externalDeclare external topic prefix(s)
     * @param localDeclare local topic prefix(s)
     * @return current topic mapper
     */
    public MessageTopicMapper register(String externalDeclare, String localDeclare) {
        logger.debug("register mapping rule: {} -- {}", externalDeclare, localDeclare);
        String[] externalPrefixes = externalDeclare.split("\\s*,\\s*");
        String[] localPrefixes = localDeclare.split("\\s*,\\s*");
        int count = Math.min(externalPrefixes.length, localPrefixes.length);
        for (int i = 0; i < count; i++) {
            String externalPrefix = externalPrefixes[i];
            String localPrefix = localPrefixes[i];
            logger.trace("register mapping rule: {} -- {}", externalPrefix, localPrefix);
            this.registerInbound(
                    topic -> StringUtils.startsWith(topic, externalPrefix),
                    topic -> localPrefix + "/" + topic.substring(externalPrefix.length()).replaceFirst("^/", "")
            );
            this.registerOutbound(
                    topic -> StringUtils.startsWith(topic, localPrefix),
                    topic -> externalPrefix + "/" + topic.substring(localPrefix.length()).replaceFirst("^/", "")
            );
        }
        return this;
    }

    public Optional<String> toLocal(String topic) {
        return this.applyRules(topic, this.inboundRules);
    }

    public Optional<String> toExternal(String topic) {
        return this.applyRules(topic, this.outboundRules);
    }

    private Optional<String> applyRules(String topic, Collection<UnaryOperator<String>> rules) {
        Optional<String> optional = rules.stream().map(rule -> rule.apply(topic))
                .filter(StringUtils::isNotBlank)
                .findFirst();
        logger.trace("apply mapping rule: '{}' -> '{}'", topic, optional.orElse("<EMPTY>"));
        return optional;
    }
}

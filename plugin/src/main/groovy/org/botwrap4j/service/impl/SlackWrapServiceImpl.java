package org.botwrap4j.service.impl;

import org.bot4j.slack.common.Slack4j;
import org.bot4j.slack.model.builder.SlackConnectionBuilder;
import org.botwrap4j.common.BotWrap4j;
import org.botwrap4j.model.request.SlackClustersRequest;
import org.botwrap4j.model.request.TelegramClustersRequest;
import org.botwrap4j.service.BotWrapService;
import org.botwrap4j.service.SlackWrapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unify4j.common.Time4j;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"FieldCanBeLocal"})
@Service
public class SlackWrapServiceImpl implements SlackWrapService {
    protected static final Logger logger = LoggerFactory.getLogger(SlackWrapServiceImpl.class);
    protected final BotWrapService botWrapService;

    @Autowired
    public SlackWrapServiceImpl(BotWrapService botWrapService) {
        this.botWrapService = botWrapService;
    }

    /**
     * @param key     - the key for slack cluster configs
     * @param message - the message will be sent
     * @return new instance Slack.4j, class {@link Slack4j}
     */
    @Override
    public Slack4j valueOf(String key, Map<String, Object> message) {
        Optional<SlackClustersRequest> node = botWrapService.findSlackNode(key);
        return node.map(e -> this.transform(e, message)).orElse(null);
    }

    /**
     * @param key       - the key for slack cluster configs
     * @param message   - the message will be sent
     * @param clusterId - the cluster_id
     * @return new instance Slack.4j, class {@link Slack4j}
     */
    @Override
    public Slack4j valueOf(String key, String clusterId, Map<String, Object> message) {
        Optional<SlackClustersRequest> node = botWrapService.findSlackNode(key, clusterId);
        return node.map(e -> this.transform(e, message)).orElse(null);
    }

    /**
     * @param conf    - the slack node, class {@link SlackClustersRequest}
     * @param message - the message will be sent
     * @return new instance Slack.4j, class {@link Slack4j}
     */
    @Override
    public Slack4j transform(SlackClustersRequest conf, Map<String, Object> message) {
        if (conf == null) {
            return null;
        }
        if (conf.isDebugging()) {
            logger.info("{}", conf);
        }
        return new Slack4j.Builder()
                .channel(conf.getChannelId())
                .token(conf.getToken())
                .message(message)
                .logger(logger)
                .connection(new SlackConnectionBuilder()
                        .debugging(conf.isDebugging())
                        .skip(!conf.isEnabled()))
                .build();
    }

    /**
     * @param key     - the key for slack cluster configs
     * @param message - the message will be sent, class {@link Map}
     */
    @Override
    public void sendMessageSilent(String key, Map<String, Object> message) {
        if (!botWrapService.isEnabled()) {
            return;
        }
        Slack4j slack4j = this.valueOf(key, message);
        if (slack4j == null) {
            return;
        }
        slack4j.requestId(BotWrap4j.getCurrentSessionId()).sendMessageSilent();
    }

    /**
     * @param key     - the key for slack cluster configs
     * @param seconds - the second before sending
     * @param message - the message will be sent, class {@link Map}
     */
    @Override
    public void sendMessageSilentAfterWaitSec(String key, long seconds, Map<String, Object> message) {
        if (!botWrapService.isEnabled()) {
            return;
        }
        new Thread(() -> {
            try {
                Optional<TelegramClustersRequest> node = botWrapService.findTelegramNode(key);
                if (node.isPresent()) {
                    if (node.get().isDebugging()) {
                        logger.info("Schedule '{}' to run {} second(s) before sending message: {}", node.get().getDesc(), seconds, message);
                    }
                    Thread.sleep(Time4j.fromSecondToMillis(seconds)); // Wait for N seconds before sending
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Schedule to run {} second(s) before sending message to Slack failure: {} by key: {}", seconds, e.getMessage(), key, e);
            }
            this.sendMessageSilent(key, message);
        }).start();
    }

    /**
     * @param key       - the key for slack cluster
     * @param clusterId - the cluster_id
     * @param message   - the message will be sent, class {@link Map}
     */
    @Override
    public void sendMessageSilent(String key, String clusterId, Map<String, Object> message) {
        if (!botWrapService.isEnabled()) {
            return;
        }
        Slack4j slack4j = this.valueOf(key, clusterId, message);
        if (slack4j == null) {
            return;
        }
        slack4j.requestId(BotWrap4j.getCurrentSessionId()).sendMessageSilent();
    }

    /**
     * @param key       - the key for slack cluster
     * @param clusterId - the cluster_id
     * @param seconds   - the second waiting before sending
     * @param message   - the message will be sent, class {@link Map}
     */
    @Override
    public void sendMessageSilentAfterWaitSec(String key, String clusterId, long seconds, Map<String, Object> message) {
        if (!botWrapService.isEnabled()) {
            return;
        }
        new Thread(() -> {
            try {
                Optional<TelegramClustersRequest> node = botWrapService.findTelegramNode(key, clusterId);
                if (node.isPresent()) {
                    if (node.get().isDebugging()) {
                        logger.info("Schedule '{}' to run {} second(s) before sending message: {}", node.get().getDesc(), seconds, message);
                    }
                    Thread.sleep(Time4j.fromSecondToMillis(seconds)); // Wait for N seconds before sending
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Schedule to run {} second(s) before sending message to Slack failure: {} by key: {}", seconds, e.getMessage(), key, e);
            }
            this.sendMessageSilent(key, clusterId, message);
        }).start();
    }
}

package org.botwrap4j.service.impl;

import org.bot4j.telegram.common.Telegram4j;
import org.bot4j.telegram.message.MessageBuilder;
import org.bot4j.telegram.model.builder.TelegramConnectionBuilder;
import org.botwrap4j.common.BotWrap4j;
import org.botwrap4j.model.request.TelegramClustersRequest;
import org.botwrap4j.service.BotWrapService;
import org.botwrap4j.service.TelegramWrapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unify4j.common.Class4j;
import org.unify4j.common.Json4j;
import org.unify4j.common.String4j;
import org.unify4j.common.Time4j;

import java.util.Optional;

@SuppressWarnings({"FieldCanBeLocal"})
@Service
public class TelegramWrapServiceImpl implements TelegramWrapService {
    protected static final Logger logger = LoggerFactory.getLogger(TelegramWrapServiceImpl.class);
    protected final BotWrapService botWrapService;

    @Autowired
    public TelegramWrapServiceImpl(BotWrapService botWrapService) {
        this.botWrapService = botWrapService;
    }

    /**
     * @param key     - the key
     * @param message - the message will be sent
     * @return new instance telegram, class {@link Telegram4j}
     */
    @Override
    public Telegram4j valueOf(String key, String message) {
        Optional<TelegramClustersRequest> node = botWrapService.findTelegramNode(key);
        return node.map(e -> this.transform(e, message)).orElse(null);
    }

    /**
     * @param key       - the key
     * @param clusterId - the cluster_id
     * @param message   - the message will be sent
     * @return new instance telegram, class {@link Telegram4j}
     */
    @Override
    public Telegram4j valueOf(String key, String clusterId, String message) {
        Optional<TelegramClustersRequest> node = botWrapService.findTelegramNode(key, clusterId);
        return node.map(e -> this.transform(e, message)).orElse(null);
    }

    /**
     * @param conf    - the telegram node, class {@link TelegramClustersRequest}
     * @param message - the message will be sent
     * @return new instance telegram, class {@link Telegram4j}
     */
    @Override
    public Telegram4j transform(TelegramClustersRequest conf, String message) {
        if (conf == null) {
            return null;
        }
        if (conf.isDebugging()) {
            logger.info("{}", conf);
        }
        return new Telegram4j.Builder()
                .text(message)
                .parseMode(conf.getType())
                .botToken(conf.getToken())
                .chatId(conf.getChatId())
                .logger(logger)
                .connection(new TelegramConnectionBuilder()
                        .debugging(conf.isDebugging())
                        .skip(!conf.isEnabled()))
                .build();
    }

    /**
     * @param key     - the key configured for telegram clusters,
     * @param message - the message will be sent
     */
    @Override
    public void sendMessageSilent(String key, String message) {
        if (!botWrapService.isEnabled()) {
            return;
        }
        Telegram4j telegram4j = this.valueOf(key, message);
        if (telegram4j == null) {
            return;
        }
        telegram4j.requestId(BotWrap4j.getCurrentSessionId()).sendMessageSilent();
    }

    /**
     * @param key     - the key configured for telegram clusters
     * @param message - the message builder, class {@link MessageBuilder}
     */
    @Override
    public void sendMessageSilent(String key, MessageBuilder<?> message) {
        if (!botWrapService.isEnabled() || message == null) {
            return;
        }
        this.sendMessageSilent(key, message.toString());
    }

    /**
     * @param key     - the key configured for telegram clusters,
     * @param seconds - the second after waiting
     * @param message - the message will be sent
     */
    @Override
    public void sendMessageSilentAfterWaitSec(String key, long seconds, String message) {
        if (!botWrapService.isEnabled() || seconds < 0 || String4j.isEmpty(message)) {
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
                logger.error("Schedule to run {} second(s) before sending message to Telegram failure: {} by key: {}", seconds, e.getMessage(), key, e);
            }
            this.sendMessageSilent(key, message);
        }).start();
    }

    /**
     * @param key     - the key configured for telegram clusters,
     * @param seconds - the second after waiting
     * @param message - the message will be sent, class {@link MessageBuilder}
     */
    @Override
    public void sendMessageSilentAfterWaitSec(String key, long seconds, MessageBuilder<?> message) {
        if (!botWrapService.isEnabled() || seconds < 0 || message == null) {
            return;
        }
        this.sendMessageSilentAfterWaitSec(key, seconds, message.toString());
    }

    /**
     * @param key     - the key configured for telegram clusters,
     * @param message - the message will be sent
     */
    @Override
    public void sendMessageSilent(String key, StringBuilder message) {
        this.sendMessageSilent(key, message.toString());
    }

    /**
     * @param key     - the key configured for telegram clusters,
     * @param message - the message will be sent
     */
    @Override
    public void sendMessageSilent(String key, Object message) {
        this.sendMessageSilent(key, Class4j.isPrimitive(message.getClass()) ? message.toString() : Json4j.toJson(message));
    }

    /**
     * @param key       - the key configured for telegram clusters,
     * @param message   - the message will be sent
     * @param clusterId - the cluster_id
     */
    @Override
    public void sendMessageSilent(String key, String clusterId, String message) {
        if (!botWrapService.isEnabled() || String4j.isEmpty(clusterId)) {
            return;
        }
        Telegram4j telegram4j = this.valueOf(key, clusterId, message);
        if (telegram4j == null) {
            return;
        }
        telegram4j.requestId(BotWrap4j.getCurrentSessionId()).sendMessageSilent();
    }

    /**
     * @param key       - the key configured for telegram clusters,
     * @param message   - the message will be sent
     * @param clusterId - the cluster_id
     */
    @Override
    public void sendMessageSilent(String key, String clusterId, MessageBuilder<?> message) {
        if (!botWrapService.isEnabled() || String4j.isEmpty(clusterId) || message == null) {
            return;
        }
        this.sendMessageSilent(key, clusterId, message.toString());
    }

    /**
     * @param key       - the key configured for telegram clusters,
     * @param message   - the message will be sent
     * @param clusterId - the cluster_id
     * @param seconds   - the second after waiting
     */
    @Override
    public void sendMessageSilentAfterWaitSec(String key, String clusterId, long seconds, String message) {
        if (!botWrapService.isEnabled() || String4j.isEmpty(clusterId) || seconds < 0) {
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
                logger.error("Schedule to run {} second(s) before sending message to Telegram failure: {} by key: {}", seconds, e.getMessage(), key, e);
            }
            this.sendMessageSilent(key, clusterId, message);
        }).start();
    }

    /**
     * @param key       - the key configured for telegram clusters,
     * @param message   - the message will be sent
     * @param clusterId - the cluster_id
     * @param seconds   - the second after waiting
     */
    @Override
    public void sendMessageSilentAfterWaitSec(String key, String clusterId, long seconds, MessageBuilder<?> message) {
        if (!botWrapService.isEnabled() || String4j.isEmpty(clusterId) || seconds < 0 || message == null) {
            return;
        }
        this.sendMessageSilentAfterWaitSec(key, clusterId, seconds, message.toString());
    }

    /**
     * @param key       - the key configured for telegram clusters,
     * @param message   - the message will be sent
     * @param clusterId - the cluster_id
     */
    @Override
    public void sendMessageSilent(String key, String clusterId, StringBuilder message) {
        this.sendMessageSilent(key, clusterId, message.toString());
    }

    /**
     * @param key       - the key configured for telegram clusters,
     * @param message   - the message will be sent
     * @param clusterId - the cluster_id
     */
    @Override
    public void sendMessageSilent(String key, String clusterId, Object message) {
        this.sendMessageSilent(key, clusterId, Class4j.isPrimitive(message.getClass()) ? message.toString() : Json4j.toJson(message));
    }
}

package org.botwrap4j.service.impl;

import org.bot4j.telegram.common.Telegram4j;
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
        if (!botWrapService.isEnabled()) {
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

package org.botwrap4j.service;

import org.bot4j.telegram.common.Telegram4j;
import org.botwrap4j.model.request.TelegramClustersRequest;

public interface TelegramWrapService {

    Telegram4j valueOf(String key, String message);

    Telegram4j valueOf(String key, String clusterId, String message);

    Telegram4j transform(TelegramClustersRequest conf, String message);

    void sendMessageSilent(String key, String message);

    void sendMessageSilent(String key, StringBuilder message);

    void sendMessageSilent(String key, Object message);

    void sendMessageSilent(String key, String clusterId, String message);

    void sendMessageSilent(String key, String clusterId, StringBuilder message);

    void sendMessageSilent(String key, String clusterId, Object message);
}

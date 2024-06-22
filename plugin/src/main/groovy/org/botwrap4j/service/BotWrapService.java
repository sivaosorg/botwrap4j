package org.botwrap4j.service;

import org.botwrap4j.model.request.SlackClustersRequest;
import org.botwrap4j.model.request.TelegramClustersRequest;

import java.util.Optional;

public interface BotWrapService {

    boolean isEnabled();

    Optional<TelegramClustersRequest> findTelegramNode(String key);

    Optional<TelegramClustersRequest> findTelegramNode(String key, String clusterId);

    Optional<SlackClustersRequest> findSlackNode(String key);

    Optional<SlackClustersRequest> findSlackNode(String key, String clusterId);
}

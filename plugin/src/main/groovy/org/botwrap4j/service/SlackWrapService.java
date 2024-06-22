package org.botwrap4j.service;

import org.bot4j.slack.common.Slack4j;
import org.botwrap4j.model.request.SlackClustersRequest;

import java.util.Map;

public interface SlackWrapService {

    Slack4j valueOf(String key, Map<String, Object> message);

    Slack4j valueOf(String key, String clusterId, Map<String, Object> message);

    Slack4j transform(SlackClustersRequest conf, Map<String, Object> message);

    void sendMessageSilent(String key, Map<String, Object> message);

    void sendMessageSilentAfterWaitSec(String key, long seconds, Map<String, Object> message);

    void sendMessageSilent(String key, String clusterId, Map<String, Object> message);

    void sendMessageSilentAfterWaitSec(String key, String clusterId, long seconds, Map<String, Object> message);
}

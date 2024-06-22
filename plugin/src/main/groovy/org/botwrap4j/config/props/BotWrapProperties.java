package org.botwrap4j.config.props;

import org.botwrap4j.model.request.SlackClustersRequest;
import org.botwrap4j.model.request.TelegramClustersRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.unify4j.common.Json4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SpellCheckingInspection"})
@Component
@ConfigurationProperties(prefix = "spring.botwrap4j")
public class BotWrapProperties implements Serializable {
    public BotWrapProperties() {
        super();
    }

    private boolean enabled = false;
    private List<TelegramClustersRequest> telegramClusters = new ArrayList<>(); // telegram_clusters
    private List<SlackClustersRequest> slackClusters = new ArrayList<>(); // slack_clusters

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<TelegramClustersRequest> getTelegramClusters() {
        return telegramClusters;
    }

    public void setTelegramClusters(List<TelegramClustersRequest> telegramClusters) {
        this.telegramClusters = telegramClusters;
    }

    public List<SlackClustersRequest> getSlackClusters() {
        return slackClusters;
    }

    public void setSlackClusters(List<SlackClustersRequest> slackClusters) {
        this.slackClusters = slackClusters;
    }

    @Override
    public String toString() {
        return String.format("BotWrap4j { enabled: %s, telegram_clusters: %s, slack_clusters: %s }",
                enabled, Json4j.toJson(telegramClusters), Json4j.toJson(slackClusters));
    }
}

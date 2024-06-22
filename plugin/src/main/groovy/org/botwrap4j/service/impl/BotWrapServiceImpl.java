package org.botwrap4j.service.impl;

import org.botwrap4j.config.props.BotWrapProperties;
import org.botwrap4j.model.request.SlackClustersRequest;
import org.botwrap4j.model.request.TelegramClustersRequest;
import org.botwrap4j.service.BotWrapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unify4j.common.Collection4j;
import org.unify4j.common.String4j;

import java.util.Optional;

@SuppressWarnings({"FieldCanBeLocal"})
@Service
public class BotWrapServiceImpl implements BotWrapService {
    protected static final Logger logger = LoggerFactory.getLogger(BotWrapServiceImpl.class);

    protected final BotWrapProperties properties;

    @Autowired
    public BotWrapServiceImpl(BotWrapProperties properties) {
        this.properties = properties;
    }

    /**
     * @return true if the botWrap4j already enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return properties.isEnabled();
    }

    /**
     * @param key - the key configured for telegram clusters,
     * @return the telegram node, class {@link TelegramClustersRequest}
     */
    @Override
    public Optional<TelegramClustersRequest> findTelegramNode(String key) {
        if (String4j.isEmpty(key) || String4j.isBlank(key) || Collection4j.isEmpty(properties.getTelegramClusters())) {
            return Optional.empty();
        }
        return properties.getTelegramClusters().stream().filter(e -> e.getKey().equals(key)).findFirst();
    }

    /**
     * @param key       - the key configured for telegram clusters,
     * @param clusterId - the cluster_id configured for telegram cluster
     * @return the telegram node, class {@link TelegramClustersRequest}
     */
    @Override
    public Optional<TelegramClustersRequest> findTelegramNode(String key, String clusterId) {
        if (String4j.isEmpty(key) || String4j.isBlank(key) || String4j.isEmpty(clusterId) || String4j.isBlank(clusterId) || Collection4j.isEmpty(properties.getTelegramClusters())) {
            return Optional.empty();
        }
        return properties.getTelegramClusters().stream().filter(e -> e.getKey().equals(key) && e.getClusterId().equals(clusterId)).findFirst();
    }

    /**
     * @param key - the key configured for slack clusters
     * @return the slack node, class {@link SlackClustersRequest}
     */
    @Override
    public Optional<SlackClustersRequest> findSlackNode(String key) {
        if (String4j.isEmpty(key) || String4j.isBlank(key) || Collection4j.isEmpty(properties.getSlackClusters())) {
            return Optional.empty();
        }
        return properties.getSlackClusters().stream().filter(e -> e.getKey().equals(key)).findFirst();
    }

    /**
     * @param key       - the key configured for slack clusters
     * @param clusterId - the cluster_id configured for slack cluster
     * @return the slack node, class {@link SlackClustersRequest}
     */
    @Override
    public Optional<SlackClustersRequest> findSlackNode(String key, String clusterId) {
        if (String4j.isEmpty(key) || String4j.isBlank(key) || String4j.isEmpty(clusterId) || String4j.isBlank(clusterId) ||
                Collection4j.isEmpty(properties.getSlackClusters())) {
            return Optional.empty();
        }
        return properties.getSlackClusters().stream().filter(e -> e.getKey().equals(key) && e.getClusterId().equals(clusterId)).findFirst();
    }
}

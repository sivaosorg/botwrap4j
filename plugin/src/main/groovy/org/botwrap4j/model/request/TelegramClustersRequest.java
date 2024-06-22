package org.botwrap4j.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.bot4j.telegram.model.enums.TelegramTextMode;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TelegramClustersRequest implements Serializable {
    public TelegramClustersRequest() {
        super();
    }

    private String key;
    private boolean enabled = false;
    private boolean debugging = false;
    private String desc;
    private String type = TelegramTextMode.Markdown.name();
    private long chatId;
    private String token;
    private String clusterId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDebugging() {
        return debugging;
    }

    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    @Override
    public String toString() {
        return String.format("Telegram node { key: %s, desc: %s, enabled: %s, debugging: %s, type: %s, chat_id: %d, token: %s, cluster_id: %s }",
                key, desc, enabled, debugging, type, chatId, token, clusterId);
    }
}

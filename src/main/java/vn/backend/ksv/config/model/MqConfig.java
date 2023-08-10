package vn.backend.ksv.config.model;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 2:18 PM
 */
public class MqConfig {
    private String host;
    private String user;
    private Integer port;
    private String password;
    private String virtualHost;
    private Integer connectionTimeout;
    private Integer requestedHeartbeat;
    private Integer handshakeTimeout;
    private Integer requestedChannelMax;
    private Integer networkRecoveryInterval;
    private Boolean automaticRecoveryEnabled;
    private Integer basicQos;
    private Integer timeToLive;

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public Integer getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public Integer getRequestedHeartbeat() {
        return requestedHeartbeat;
    }

    public Integer getHandshakeTimeout() {
        return handshakeTimeout;
    }

    public Integer getRequestedChannelMax() {
        return requestedChannelMax;
    }

    public Integer getNetworkRecoveryInterval() {
        return networkRecoveryInterval;
    }

    public Boolean getAutomaticRecoveryEnabled() {
        return automaticRecoveryEnabled;
    }

    public Integer getBasicQos() {
        return basicQos;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }
}

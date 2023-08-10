package vn.backend.ksv.config.model;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 4:12 PM
 */
public class DbConfig {

    private String jdbcDriver;
    private String dataSourceClassName;
    private String host;
    private Integer port;
    private Integer maxPoolSize;
    private String username;
    private String password;
    private String database;
    private Boolean debugInfoOnStackTrace = false;
    private Boolean executeLogging = false;

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public String getDataSourceClassName() {
        return dataSourceClassName;
    }

    public DbConfig setDataSourceClassName(String dataSourceClassName) {
        this.dataSourceClassName = dataSourceClassName;
        return this;
    }

    public String getHost() {
        return host;
    }

    public DbConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public DbConfig setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public DbConfig setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DbConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DbConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getDatabase() {
        return database;
    }

    public DbConfig setDatabase(String database) {
        this.database = database;
        return this;
    }

    public Boolean getDebugInfoOnStackTrace() {
        return debugInfoOnStackTrace;
    }

    public DbConfig setDebugInfoOnStackTrace(Boolean debugInfoOnStackTrace) {
        this.debugInfoOnStackTrace = debugInfoOnStackTrace;
        return this;
    }

    public Boolean getExecuteLogging() {
        return executeLogging;
    }

    public DbConfig setExecuteLogging(Boolean executeLogging) {
        this.executeLogging = executeLogging;
        return this;
    }
}

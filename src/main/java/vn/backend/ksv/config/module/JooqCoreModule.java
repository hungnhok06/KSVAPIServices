package vn.backend.ksv.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.SQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.backend.ksv.common.Configuration;
import vn.backend.ksv.config.model.DbConfig;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 4:17 PM
 */
public class JooqCoreModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(JooqCoreModule.class);
    private DbConfig config;
    private HikariDataSource dataSource;

    public JooqCoreModule(DbConfig config, Map<String, DataSource> dataSourceMap) {
        this.config = config;
        this.dataSource = createOracleConnection(this.config);
        dataSourceMap.put(this.config.getDatabase(), dataSource);
    }

    public JooqCoreModule(DbConfig config) {
        this.config = config;
        this.dataSource = createConnection(this.config);
    }

    public JooqCoreModule(DbConfig config, Boolean isOracle) {
        this.config = config;
        if (isOracle) {
            this.dataSource = createOracleConnection(this.config);
        } else {
            this.dataSource = createConnection(this.config);
        }
    }

    @Provides
    @Singleton
    public HikariDataSource getDataSource() {
        return dataSource;
    }

    @Provides
    @Singleton
    public DbConfig getDbConfig() {
        return this.config;
    }

    @Override
    protected void configure() {
    }


    /**
     * initialize sql connection
     *
     * @param config
     */
    public static HikariDataSource createConnection(DbConfig config) {
        LOGGER.info("Start sql connection");

        Configuration.checkValidation(config);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceClassName(config.getDataSourceClassName());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.addDataSourceProperty("serverName", config.getHost());
        hikariConfig.addDataSourceProperty("port", config.getPort());
        hikariConfig.addDataSourceProperty("databaseName", config.getDatabase());
        hikariConfig.addDataSourceProperty("characterEncoding", StandardCharsets.UTF_8);
        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.setConnectionTimeout(10000);
        hikariConfig.addDataSourceProperty("serverTimezone", "Asia/Ho_Chi_Minh");
        hikariConfig.addDataSourceProperty("useLegacyDatetimeCode", "false");
        hikariConfig.addDataSourceProperty("autoReconnect", true);
        hikariConfig.setAutoCommit(true);
        if (config.getMaxPoolSize() != null) {
            hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
            hikariConfig.setMinimumIdle(4);
        } else {
            hikariConfig.setMaximumPoolSize(8);
            hikariConfig.setMinimumIdle(4);
        }
        LOGGER.info("Sql connection in successful");
        return new HikariDataSource(hikariConfig);
    }

    /**
     * initialize sql connection
     *
     * @param config
     */
//    public static HikariDataSource createConnection(DbConfig config) {
//        LOGGER.info("Start sql connection");
//
//        Configuration.checkValidation(config);
//
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setDriverClassName(config.getJdbcDriver());
//        hikariConfig.setJdbcUrl("jdbc:sqlserver://" + config.getHost() + ":" + config.getPort() + ";databaseName=" + config.getDatabase());
//        hikariConfig.setUsername(config.getUsername());
//        hikariConfig.setPassword(config.getPassword());
//        hikariConfig.setSchema(config.getDatabase());
//        hikariConfig.setConnectionTimeout(60000);
//        if (config.getMaxPoolSize() != null) {
//            hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
//            hikariConfig.setMinimumIdle(4);
//        } else {
//            hikariConfig.setMaximumPoolSize(8);
//            hikariConfig.setMinimumIdle(4);
//        }
//        LOGGER.info("Sql connection in successful");
//        return new HikariDataSource(hikariConfig);
//    }

    public static HikariDataSource createOracleConnection(DbConfig config) {
        LOGGER.info("Start create oracle connection");
        Configuration.checkValidation(config);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        hikariConfig.setJdbcUrl("jdbc:oracle:thin:@" + config.getHost() + ":" + config.getPort() + ":ORCLCDB");
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setSchema(config.getDatabase());
        hikariConfig.setConnectionTimeout(60000);
        if (config.getMaxPoolSize() != null) {
            hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
            hikariConfig.setMinimumIdle(4);
        } else {
            hikariConfig.setMaximumPoolSize(8);
            hikariConfig.setMinimumIdle(4);
        }
        LOGGER.info("Sql connection in successful");
        return new HikariDataSource(hikariConfig);
    }
}

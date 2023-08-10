package vn.backend.ksv.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.SQLDialect;
import vn.backend.ksv.common.Configuration;
import vn.backend.ksv.common.constant.staticEnum.StaticEnum;
import vn.backend.ksv.config.module.*;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/8/23
 * Time: 4:01 PM
 */
public class ModuleConfig  extends AbstractModule {

    private static final Logger LOGGER = LogManager.getLogger(ModuleConfig.class);

    private Context context;
    private Vertx vertx;
    private Gson gson;
    private Config config;

    public ModuleConfig(Context context) {
        this.context = context;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.config = Configuration.loadProperties(context.config().encode(), Config.class);
        //this.config = this.gson.fromJson(context.config().encodePrettily(), Config.class);
        this.vertx = Vertx.vertx(new VertxOptions()
                .setWorkerPoolSize(config.getWorkerPoolSize())
                .setMaxWorkerExecuteTime(config.getWorkerMaxExecuteTime()));
    }

    @Provides
    @Singleton
    public Context getContext() {
        return context;
    }

    @Provides
    @Singleton
    public Vertx getVertx() {
        return vertx;
    }

    @Provides
    @Singleton
    public Gson getGson() {
        return gson;
    }

    @Provides
    @Singleton
    private Config getConfig() {
        return config;
    }

    @Provides
    @Singleton
    public Map<StaticEnum.TokenType, JwtBuilder> getJwtBuilderMap() throws Exception {
        Map<StaticEnum.TokenType, JwtBuilder> map = new HashMap<>();
        map.put(StaticEnum.TokenType.ADMIN_LOGIN, getJwtBuilderAdmin());
        return map;
    }

    public JwtBuilder getJwtBuilderAdmin() throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Configuration.decodeByteFile(getConfig().getTokenConfig().getRsaPrivateFile()));
        return Jwts.builder()
                .signWith(KeyFactory.getInstance("RSA").generatePrivate(spec));
    }

    @Provides
    @Singleton
    public Cache<String, String> getContractCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(5 * 60, TimeUnit.SECONDS) // 5 minutes
                .build();
    }


    @Override
    protected void configure() {
        install(new AuthModule(this.vertx, this.config.getTokenConfig()));

        install(new ConsumerModule());
        install(new JooqCoreModule(this.config.getDbConfig()));
        install(new HandlerModule());
        install(new ServiceModule());
        install(new RepositoryModule());
        install(new ValidationModule());
    }
}

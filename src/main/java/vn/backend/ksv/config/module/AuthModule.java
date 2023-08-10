package vn.backend.ksv.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.backend.ksv.common.Configuration;
import vn.backend.ksv.common.auth.IJwtAuth;
import vn.backend.ksv.common.auth.JwtAuth;
import vn.backend.ksv.common.auth.multisession.IJwtAuthNonCache;
import vn.backend.ksv.common.auth.multisession.JwtAuthNonCache;
import vn.backend.ksv.common.constant.staticEnum.StaticEnum;
import vn.backend.ksv.config.model.TokenConfig;

import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 11:34 AM
 */
public class AuthModule extends AbstractModule {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Vertx vertx;
    private TokenConfig tokenConfig;
    private String rsa = null;

    public AuthModule(Vertx vertx,
                      TokenConfig tokenConfig) {
        this.vertx = vertx;
        this.tokenConfig = tokenConfig;
    }

    public AuthModule(Vertx vertx,
                      String rsa) {
        this.vertx = vertx;
        this.rsa = rsa;
    }

    @Provides
    @Singleton
    public Map<StaticEnum.TokenType, JwtParser> getJwtParserMap() throws Exception {
        Map<StaticEnum.TokenType, JwtParser> map = new HashMap<>();
        map.put(StaticEnum.TokenType.ADMIN_LOGIN, getJwtParserBackOffice());
        return map;
    }

    private JwtParser getJwtParserBackOffice() throws Exception {
        X509EncodedKeySpec keySpec;
        if (rsa != null) {
            keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsa));
        } else {
            keySpec = new X509EncodedKeySpec(Configuration.decodeByteFile(tokenConfig.getRsaPublicFile()));
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return Jwts.parser()
                .setSigningKey(keyFactory.generatePublic(keySpec));
    }

    @Override
    protected void configure() {
        LOGGER.info("Start binding jwt auth");
        bind(IJwtAuthNonCache.class).to(JwtAuthNonCache.class).in(Scopes.SINGLETON);
        bind(IJwtAuth.class).to(JwtAuth.class).in(Scopes.SINGLETON);
    }
}

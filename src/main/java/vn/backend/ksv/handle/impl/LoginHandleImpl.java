package vn.backend.ksv.handle.impl;

import com.google.inject.Inject;
import vn.backend.ksv.common.auth.IJwtAuth;
import vn.backend.ksv.common.auth.IJwtProvider;
import vn.backend.ksv.common.auth.TokenAuthInfo;
import vn.backend.ksv.common.auth.TokenUserInfo;
import vn.backend.ksv.common.auth.multisession.IJwtAuthNonCache;
import vn.backend.ksv.common.constant.staticEnum.StaticEnum;
import vn.backend.ksv.common.reponse.LoginRes;
import vn.backend.ksv.common.util.DateTimes;
import vn.backend.ksv.config.Config;
import vn.backend.ksv.handle.ILoginHandle;
import vn.backend.ksv.repository.generator.tables.records.UserRecord;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 4:03 PM
 */
public class LoginHandleImpl implements ILoginHandle {
    private Config config;
    private IJwtAuthNonCache JwtAuthNonCache;
    private IJwtAuth jwtAuth;
    private IJwtProvider jwtProvider;

    @Inject
    public LoginHandleImpl(Config config,
                           IJwtAuthNonCache JwtAuthNonCache,
                           IJwtAuth jwtAuth,
                           IJwtProvider jwtProvider){
        this.config = config;
        this.jwtAuth = jwtAuth;
        this.JwtAuthNonCache = JwtAuthNonCache;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public LoginRes generateBackofficeToken(UserRecord record, List<String> decentralizationList) throws InterruptedException {
        Long timeToLive = TimeUnit.MINUTES.toMillis(config.getTokenConfig().getTimeToLive());
        Long activeTime = TimeUnit.SECONDS.toMillis(config.getTokenConfig().getActiveTime());
        Long expiredAt = DateTimes.dateAdd(DateTimes.getCurrentTimeMillis(), timeToLive);
        Long refreshAt = DateTimes.dateAdd(DateTimes.getCurrentTimeMillis(), activeTime);
        TokenAuthInfo tokenAuthInfo = new TokenAuthInfo()
                .setUserId(record.getUserid())
                .setActiveTime(refreshAt)
                .setExpireAt(expiredAt)
                .setCreatedAt(DateTimes.getCurrentTimeMillis());
        TokenUserInfo tokenUserInfo = new TokenUserInfo()
                .setUserId(record.getUserid())
                .setUsername(record.getUsername())
                .setDecentralizationList(decentralizationList);
        jwtAuth.expireToken(tokenAuthInfo.getUserId(), StaticEnum.TokenType.ADMIN_LOGIN);
        return new LoginRes().setToken(jwtProvider.generateToken(tokenAuthInfo, tokenUserInfo, StaticEnum.TokenType.ADMIN_LOGIN, config.getTokenConfig().getTimeToLive()*60))
                .setExpireAt(expiredAt)
                .setRefreshAt(refreshAt);
    }
}

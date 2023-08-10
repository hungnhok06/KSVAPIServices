package vn.backend.ksv.common.auth.multisession;

import com.google.inject.Inject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.auth.JWTKey;
import vn.backend.ksv.common.auth.TokenAuthInfo;
import vn.backend.ksv.common.auth.TokenUserInfo;
import vn.backend.ksv.common.constant.staticEnum.StaticEnum;
import vn.backend.ksv.common.exception.CommonException;
import vn.backend.ksv.common.pojo.common.HeaderInternalRequest;
import vn.backend.ksv.common.util.DateTimes;
import vn.backend.ksv.common.util.JsonConverter;

import java.util.Map;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 11:14 AM
 */
public class JwtAuthNonCache implements IJwtAuthNonCache{

    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());

    private Map<StaticEnum.TokenType, JwtParser> jwtParserMap;

    @Inject
    JwtAuthNonCache(Map<StaticEnum.TokenType, JwtParser> jwtParserMap) {
        this.jwtParserMap = jwtParserMap;
    }

    @Override
    public HeaderInternalRequest validateToken(String token, String collationId, StaticEnum.TokenType tokenType) throws CommonException.TokenExpiredByLoginException, CommonException.TokenExpiredByTimeoutException, CommonException.ValidationError {
        try {
            Jws<Claims> model = jwtParserMap.get(tokenType)
                    .parseClaimsJws(token);
            String jsonHeader = JsonConverter.toJson(model.getHeader().get(JWTKey.AUTH));
            String jsonBody = JsonConverter.toJson(model.getBody().get(JWTKey.USERID));
            TokenAuthInfo tokenAuthInfo = JsonConverter.fromJson(jsonHeader, TokenAuthInfo.class);
            TokenUserInfo tokenUserInfo = JsonConverter.fromJson(jsonBody, TokenUserInfo.class);
            if (!StaticEnum.TokenType.safeValuesOf(tokenAuthInfo.getTokenType()).equals(tokenType)) {
                throw new CommonException.ValidationError("Wrong token type");
            }
//            checkExpireToken(tokenAuthInfo.getExpireAt());
//            checkActiveToken(tokenAuthInfo.getActiveTime());
            return new HeaderInternalRequest()
                    .setUserId(tokenUserInfo.getUserId())
                    .setUsername(tokenUserInfo.getUsername())
                    .setEmail(tokenUserInfo.getEmail())
                    .setPhone(tokenUserInfo.getPhone())
                    .setDeviceId(tokenUserInfo.getDeviceId())
                    .setAccountType(tokenUserInfo.getAccountType())
                    .setLanguage(tokenUserInfo.getLanguage())
                    .setCustomerName(tokenUserInfo.getCustomerName())
                    .setSex(tokenUserInfo.getSex())
                    .setToken(token)
                    .setCollationId(collationId)
                    .setCompanyId(tokenAuthInfo.getCompanyId())
                    .setProjectId(tokenAuthInfo.getProjectId())
                    .setAppId(tokenAuthInfo.getAppId())
                    .setLevelId(tokenAuthInfo.getLevelId())
                    .setDecentralizationList(tokenUserInfo.getDecentralizationList());
        } catch (CommonException.RequireRefreshTokenException e) {
            throw e;
        } catch (CommonException.TokenExpiredByTimeoutException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unknown error on check token cause by {}", e.getMessage());
            throw new CommonException.ValidationError("Unknown error on check token");
        }
    }

    @Override
    public HeaderInternalRequest validateRefreshToken(String token, StaticEnum.TokenType tokenType) throws CommonException.TokenExpiredByLoginException, CommonException.TokenExpiredByTimeoutException, CommonException.ValidationError {
        try {
            Jws<Claims> model = jwtParserMap.get(tokenType)
                    .parseClaimsJws(token);
            String jsonHeader = JsonConverter.toJson(model.getHeader().get(JWTKey.AUTH));
            String jsonBody = JsonConverter.toJson(model.getBody().get(JWTKey.USERID));
            TokenAuthInfo tokenAuthInfo = JsonConverter.fromJson(jsonHeader, TokenAuthInfo.class);
            TokenUserInfo tokenUserInfo = JsonConverter.fromJson(jsonBody, TokenUserInfo.class);
            if (!StaticEnum.TokenType.safeValuesOf(tokenAuthInfo.getTokenType()).equals(tokenType)) {
                throw new CommonException.ValidationError("Wrong token type");
            }
            checkExpireToken(tokenAuthInfo.getExpireAt());
            return new HeaderInternalRequest()
                    .setUserId(tokenUserInfo.getUserId())
                    .setUsername(tokenUserInfo.getUsername())
                    .setEmail(tokenUserInfo.getEmail())
                    .setPhone(tokenUserInfo.getPhone())
                    .setDeviceId(tokenUserInfo.getDeviceId())
                    .setAccountType(tokenUserInfo.getAccountType())
                    .setLanguage(tokenUserInfo.getLanguage())
                    .setCustomerName(tokenUserInfo.getCustomerName())
                    .setSex(tokenUserInfo.getSex())
                    .setToken(token)
                    .setProjectId(tokenAuthInfo.getProjectId())
                    .setAppId(tokenAuthInfo.getAppId())
                    .setExpireAt(tokenAuthInfo.getExpireAt());
        } catch (CommonException.TokenExpiredByTimeoutException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Unknown error on check token cause by {}", e.getMessage());
            throw new CommonException.ValidationError("Unknown error on check token");
        }
    }

    private void checkExpireToken(Long expireToken) throws CommonException.TokenExpiredByTimeoutException {
        try {
            DateTimes.checkExpiredTime(expireToken);
        } catch (CommonException.TimeoutException e) {
            throw new CommonException.TokenExpiredByTimeoutException("Token expire by timeout");
        }
    }

    private void checkActiveToken(Long activeTime) throws CommonException.RequireRefreshTokenException {
        try {
            DateTimes.checkExpiredTime(activeTime);
        } catch (CommonException.TimeoutException e) {
            throw new CommonException.RequireRefreshTokenException("Token require to refresh");
        }
    }
}

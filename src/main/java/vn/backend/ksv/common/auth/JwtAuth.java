package vn.backend.ksv.common.auth;

import com.google.inject.Inject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.constant.staticEnum.StaticEnum;
import vn.backend.ksv.common.exception.CommonException;
import vn.backend.ksv.common.exception.DataException;
import vn.backend.ksv.common.pojo.common.HeaderInternalRequest;
import vn.backend.ksv.common.util.DateTimes;
import vn.backend.ksv.common.util.JsonConverter;

import java.util.Map;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 10:37 AM
 */
public class JwtAuth implements IJwtAuth{

    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());

    private Map<StaticEnum.TokenType, JwtParser> jwtParserMap;


    @Inject
    JwtAuth(Map<StaticEnum.TokenType, JwtParser> jwtParserMap) {
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
            checkToken(tokenAuthInfo.getUserId(), token, tokenAuthInfo.getTokenType());
            DateTimes.checkExpiredTime(tokenAuthInfo.getExpireAt());
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
                    .setProjectId(tokenAuthInfo.getProjectId())
                    .setAppId(tokenAuthInfo.getAppId())
                    .setCompanyId(tokenAuthInfo.getCompanyId())
                    .setDecentralizationList(tokenUserInfo.getDecentralizationList());
        } catch (DataException.NotFoundException e) {
            throw new CommonException.TokenExpiredByTimeoutException("Not found token, token expired");
        } catch (CommonException.TimeoutException e) {
            throw new CommonException.TokenExpiredByTimeoutException("Token expired by time out");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Unknown error on check token cause by {}", e.getMessage());
            throw new CommonException.ValidationError("Unknown error on check token");
        }
    }

    @Override
    public HeaderInternalRequest decodeToken(String token, String collationId, StaticEnum.TokenType tokenType) throws CommonException.TokenExpiredByLoginException, CommonException.TokenExpiredByTimeoutException, CommonException.ValidationError {
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
                    .setProjectId(tokenAuthInfo.getProjectId())
                    .setAppId(tokenAuthInfo.getAppId())
                    .setDecentralizationList(tokenUserInfo.getDecentralizationList());
        } catch (Exception e) {
            LOGGER.error("Unknown error on check token cause by {}", e.getMessage());
            throw new CommonException.ValidationError("Unknown error on check token");
        }
    }

    @Override
    public void expireToken(String cif, StaticEnum.TokenType tokenType) throws InterruptedException {

    }

    @Override
    public void expireToken(String token) throws InterruptedException {

    }

    @Override
    public void expireTokenByCif(String cif) throws InterruptedException {

    }

    @Override
    public void checkToken(String cif, String token, Integer tokenType) {

    }

    @Override
    public void checkDevice(Integer status) {

    }

    @Override
    public void validateRootToken(String token) {

    }
}

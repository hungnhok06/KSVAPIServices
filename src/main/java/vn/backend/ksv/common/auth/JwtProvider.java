package vn.backend.ksv.common.auth;

import com.google.inject.Inject;
import io.jsonwebtoken.JwtBuilder;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.constant.staticEnum.StaticEnum;
import vn.backend.ksv.common.exception.CommonException;
import vn.backend.ksv.common.util.Generator;
import vn.backend.ksv.common.util.IValidationTool;
import vn.backend.ksv.common.util.JsonConverter;

import java.util.Map;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/10/23
 * Time: 4:09 PM
 */
public class JwtProvider implements IJwtProvider{
    private final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());

    private IValidationTool validationTool;
    private Map<StaticEnum.TokenType, JwtBuilder> map;
//    private IRedisRepo redisRepo;

    @Inject
    JwtProvider(IValidationTool validationTool,
                Map<StaticEnum.TokenType, JwtBuilder> map
//            ,
//                IRedisRepo redisRepo
    ) {
        this.validationTool = validationTool;
        this.map = map;
//        this.redisRepo = redisRepo;
    }

    @Override
    public String generateToken(TokenAuthInfo tokenAuthInfo, TokenUserInfo tokenUserInfo, StaticEnum.TokenType tokenType, Integer timeToLive) {
        try {
            validationTool.checkNotNullWithAnnotation(tokenAuthInfo);
            String token = map.get(tokenType)
                    .setId(Generator.generate())
                    .setHeaderParam(JWTKey.AUTH, JsonConverter.toMultiMap(tokenAuthInfo
                            .setTokenType(tokenType.getCode())))
                    .claim(JWTKey.USERID, JsonConverter.toMultiMap(tokenUserInfo))
                    .compact();
//            saveToken(tokenAuthInfo.getUserId(), token, tokenType, timeToLive);
            clean(tokenType);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("Error on generate token {}", e.getMessage());
            throw new CommonException.GenerateException("Algorithm error failed");
        }
    }


//    private void saveToken(String cif, String token, StaticEnum.TokenType tokenType, Integer timeToLive) throws InterruptedException {
//        redisRepo.save(cif + "_" + tokenType.getCode() + "_" + token, token, timeToLive);
//    }

    private void clean(StaticEnum.TokenType tokenType) {
        map.get(tokenType)
                .setId(null)
                .setHeaderParams(null)
                .setClaims(null);
    }
}
